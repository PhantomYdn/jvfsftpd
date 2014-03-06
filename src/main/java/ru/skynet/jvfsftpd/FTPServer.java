package ru.skynet.jvfsftpd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.skynet.jvfsftpd.config.FtpConfiguration;

public class FTPServer
{
    /**
     * The version number of  jvfsftpd.
     */
    public static final String VERSION = "0.9.0";
    
    private ServerSocket serverSocket = null;
    
    private final static Log log = LogFactory.getLog(FTPServer.class);
         
    public static void main(String[] args) throws Exception
    {
        new FTPServer().start();
    }

    public void start() throws Exception
    {
        serverSocket = new ServerSocket(FtpConfiguration.getConfiguration().getServerPort(),
                                        50,
                                        FtpConfiguration.getConfiguration().getBindAddress());
        System.out.println("JVFSFTPd v."+VERSION+" started");
        while (serverSocket!=null)
        {
            Socket clientSocket = serverSocket.accept();
            ServerPI pi = new ServerPI(clientSocket);
            pi.start();
        }
    }       
    
    public void forkStart() throws Exception
    {
        new Thread(new Runnable()
        {

            public void run()
            {
                try
                {
                    start();
                }
                catch (Exception e)
                {
                    log.error("Can't start ftp thread", e);
                }
            }
    
        }).start();
    }
    
    public void stop()
    {
        try
        {
            serverSocket.close();
            serverSocket=null;
        }
        catch (IOException e)
        {
            //TODO check allowance of empty catch
        }
    }
}
