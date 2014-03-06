package ru.skynet.jvfsftpd;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.skynet.jvfsftpd.handlers.*;

public final class HandlersConfiguration
{
    private static Map<FtpCommand, Class> handlers = new HashMap<FtpCommand, Class>();
    
    private static Log log = LogFactory.getLog(HandlersConfiguration.class);
    
    static
    {
        addHandler(FtpCommand.USER, UserHandler.class);
        addHandler(FtpCommand.PASS, PassHandler.class);
        addHandler(FtpCommand.CWD, ChangeDirHandler.class);
        addHandler(FtpCommand.QUIT, QuitHandler.class);
        addHandler(FtpCommand.REIN, ReinitializeHandler.class);
        addHandler(FtpCommand.PORT, PortHandler.class);
        addHandler(FtpCommand.TYPE, TypeHandler.class);
        addHandler(FtpCommand.STRU, STRUHandler.class);
        addHandler(FtpCommand.MODE, ModeHandler.class);
        addHandler(FtpCommand.RETR, RetriveHandler.class);
        addHandler(FtpCommand.STOR, StorHandler.class);
        addHandler(FtpCommand.DELE, DeleteHandler.class);
        addHandler(FtpCommand.RMD, DeleteHandler.class);
        addHandler(FtpCommand.MKD, MakeDirHandler.class);
        addHandler(FtpCommand.LIST, ListHandler.class);
        addHandler(FtpCommand.NLST, ListHandler.class);
        addHandler(FtpCommand.SYST, SystemHandler.class);
        addHandler(FtpCommand.NOOP, NoopHandler.class);
        addHandler(FtpCommand.SIZE, SizeHandler.class);
        addHandler(FtpCommand.MDTM, MDTMHandler.class);
        addHandler(FtpCommand.PWD, CurentDirHandler.class);
        addHandler(FtpCommand.XPWD, CurentDirHandler.class);
        addHandler(FtpCommand.PASV, PassivateHandler.class);
        addHandler(FtpCommand.UNSUPPORTED, UnsupportedHandler.class);
    }
    
    public static void loadConfig(File file) throws IncorrectConfigurationException
    {
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();       
            Document doc = builder.parse(file);
            Element root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("HANDLER");
            for(int i=0;i<list.getLength();i++)
            {
                Element handlerElement = (Element)list.item(i);
                String commandName = handlerElement.getAttribute("command");
                FtpCommand command = FtpCommand.valueOf(commandName);
                if(command==null)
                {
                    log.warn("Command "+ commandName+" is not supported by ftpd");
                    continue;
                }
                String clssStr = handlerElement.getAttribute("class");
                Class clss = null;
                try
                {
                    clss = Class.forName(clssStr);
                    addHandler(command, clss);
                }
                catch (ClassNotFoundException e)
                {
                    log.warn("Class "+clssStr+" was not found for command: "+commandName);
                    continue;
                }            
            }
        }
        catch (ParserConfigurationException e)
        {
            throw new IncorrectConfigurationException(e);
        }
        catch (SAXException e)
        {
            throw new IncorrectConfigurationException(e);
        }
        catch (IOException e)
        {
            throw new IncorrectConfigurationException(e);
        }
    }
    
    private static void addHandler(FtpCommand command, Class clss)
    {
        if(!Handler.class.isAssignableFrom(clss)) 
            throw new IncorrectConfigurationException("Class must be assignable for Handler.class");
        handlers.put(command, clss);
        
    }
    
    
    public static Handler getHandlerFor(FtpCommand command)
    {        
        Class clss = null;
        try
        {
            clss = command!=null?handlers.get(command):null;
            if(clss==null) clss = handlers.get(FtpCommand.UNSUPPORTED);
            return (Handler)clss.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("Can't instantiate handler: "+clss.getName());
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Can't instantiate handler: "+clss.getName());
        }
    }
}
