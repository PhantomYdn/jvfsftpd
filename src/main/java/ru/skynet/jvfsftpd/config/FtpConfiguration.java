package ru.skynet.jvfsftpd.config;

import java.io.File;
import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;

import ru.skynet.jvfsftpd.FtpCommand;
import ru.skynet.jvfsftpd.Handler;
import ru.skynet.jvfsftpd.HandlersConfiguration;
import ru.skynet.jvfsftpd.IncorrectConfigurationException;
import ru.skynet.jvfsftpd.config.clconfig.CLFtpConfiguration;
import ru.skynet.jvfsftpd.config.fileconfig.FileFtpConfiguration;

public abstract class FtpConfiguration
{      
	protected final Log log = LogFactory.getLog(this.getClass());
	public static final String DEFAULT_CONFIG_FILE_NAME = "ftpd.xml";
	public static final String CONFIG_FILE_PROPERTY = "configfile";
	public static final String CONFIG_CLASS_PROPERTY = "configclass";
    private static FtpConfiguration currentConfiguration = null;
    protected static SessionManager sessionManager = null;
    
    public static FtpConfiguration getConfiguration()
    {
        if(currentConfiguration==null)
        {
            String configClass = System.getProperty(FtpConfiguration.CONFIG_CLASS_PROPERTY);
            if(configClass!=null)
            {
                try
                {
                    Class clss = Class.forName(configClass);
                    currentConfiguration = (FtpConfiguration)clss.newInstance();
                }
                catch (Exception e)
                {                
                    throw new IncorrectConfigurationException("Config class "+configClass+" not found");
                }
            }
            else
            {
                String configFileName = System.getProperty(FtpConfiguration.CONFIG_FILE_PROPERTY,
                        FtpConfiguration.DEFAULT_CONFIG_FILE_NAME);
                File configFile = new File(configFileName);
                if(configFile.exists())
                {
                    currentConfiguration = new FileFtpConfiguration(configFile);
                }
                else
                {
                    currentConfiguration = new CLFtpConfiguration();
                }
            }
        }
        return currentConfiguration;
    }    
    public SessionManager getSessionManager()
    {
    	if(sessionManager==null)
    	{
    		sessionManager=new DefaultSessionManager();
    	}
    	return sessionManager;
    }
    
    public Handler getHandlerFor(String command)
    {
    	if(command==null) 
    		return HandlersConfiguration.getHandlerFor(FtpCommand.UNSUPPORTED);
    	command = command.toUpperCase();
    	FtpCommand ftpCommand = null;
    	try
		{
			ftpCommand = FtpCommand.valueOf(command);
		} 
    	catch (IllegalArgumentException e)
		{
			ftpCommand = FtpCommand.UNSUPPORTED;
		}
    	return HandlersConfiguration.getHandlerFor(ftpCommand);
    }
    
    public int getServerPort()
    {
    	return 21;
    }
    public InetAddress getBindAddress()
    {
    	return null;
    }
    public String getSecurityDomain()
    {
    	return null;
    }
    public long getBandwidthInput(FtpSession session)
    {
    	return -1;
    }
    public long getBandwidthOutput(FtpSession session)
    {
    	return -1;
    }
    public abstract FileObject getInitialBaseDir(FtpSession session);
    public abstract FileObject getInitialCurrentDir(FtpSession session);
}
