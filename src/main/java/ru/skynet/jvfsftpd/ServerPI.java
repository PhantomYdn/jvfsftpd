package ru.skynet.jvfsftpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.skynet.jvfsftpd.config.FtpConfiguration;
import ru.skynet.jvfsftpd.config.FtpSession;

public class ServerPI extends Thread
{
	private static final Log log = LogFactory.getLog(ServerPI.class);
    public Socket clientSocket;
    public BufferedReader reader;
    public PrintWriter writer;
    public ServerDTP dtp;

    public ServerPI(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(new InputStreamReader(clientSocket
                .getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(clientSocket
                .getOutputStream()), true);

        dtp = new ServerDTP(this);        
    }       

    public void run()
    {
        try
        {
            // The actual interpreter loop.
            //
            clientLoop();
        }
        catch (Exception e)
        {
            log.error("clientLoop failed: ",e);
        }
        finally
        {
            try
            {
                clientSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void clientLoop() throws Exception
    {
        reply(220, "JVFSFTPd v."+FTPServer.VERSION+" ready.");
        FtpSession.getSession().setServerPI(this);
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            int indx = line.indexOf(' ');
            
            String command = indx>=0?line.substring(0, indx):line;
            String params = indx>=0?line.substring(indx+1):null;

            if ("PASS".equalsIgnoreCase(command))
//                log.info("PASS ********");
                log.debug("PASS ********");
            else
//                log.info(line);
                log.debug("<"+line);

            try
            {            	
            	try
				{
					Handler handler = FtpConfiguration.getConfiguration().getHandlerFor(command);
//	                log.println(command+" "+params);
	                if(handler.requireLogin()) checkLogin();
	                FtpReplay replay = handler.handle(command, params);
	                if (replay(replay) == 221)
	                    return;
				} 
            	catch (IllegalArgumentException e)
				{
					
				}                
            }
            catch(IllegalArgumentException e)
            {
            	
            }
            catch (CommandException e)
            {
                reply(e.getCode(), e.getText());
            }          
            catch (Exception e)
            {
                log.error("Exception invoking " + command
                      + " command handler: ", e);
            }
        }
    }


    public int reply(int code, String text)
    {
        log.debug(">"+code + " " + text);
        writer.println(code + " " + text);
        return code;
    }
    
    public int replay(FtpReplay replay)
    {
    	log.debug(">"+replay.getCode() + " " + replay.getMessage());
        writer.println(replay.getCode() + " " + replay.getMessage());
        return replay.getCode();
    }
    
    private void checkLogin() throws CommandException
    {
        if (!FtpSession.getSession().isLogged())
            throw new CommandException(530, "Please login with USER and PASS.");
    }
}
