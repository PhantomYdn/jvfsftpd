package ru.skynet.jvfsftpd.config;

import javax.security.auth.login.LoginContext;

import org.apache.commons.vfs.FileObject;

import ru.skynet.jvfsftpd.ServerPI;

public class FtpSession
{
    private FileObject baseDir = null;
    private FileObject currentDir = null;
    
    private String username = null;
    private String password = null;
    private boolean isLogged = false;
    private LoginContext lc = null;
    private ServerPI serverPI = null;
    
    private static InheritableThreadLocal<FtpSession> currentSession = new InheritableThreadLocal<FtpSession>();
    
    private FtpSession()
    {
        
    }
    
    /*public void bindFtpSession()
    {
    	currentSession.set(this);
    }
    
    public void unbindFtpSession()
    {
    	currentSession.set(null);
    }
    
    public static FtpSession unbind()
    {
    	FtpSession session = getSession();
    	if(session!=null) session.unbindFtpSession();
    	return session;
    }*/
    
    public static FtpSession getSession()
    {
        return getSession(false);
    }
    
    public static FtpSession getSession(boolean recreate)
    {
        FtpSession session = currentSession.get();
        if(session==null || recreate)
        {
            session = new FtpSession();            
            FtpConfiguration.getConfiguration().getSessionManager().initSession(session);
            currentSession.set(session);
        }
        return session;
    }

    public FileObject getBaseDir()
    {
        return baseDir;
    }

    public void setBaseDir(FileObject baseDir)
    {
        this.baseDir = baseDir;
    }

    public FileObject getCurrentDir()
    {
        return currentDir;
    }

    public void setCurrentDir(FileObject currentDir)
    {
        this.currentDir = currentDir;
    }    
    
    public boolean isLogged()
    {
        return isLogged;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setLogged(boolean isLogged)
    {
        this.isLogged = isLogged;
    }

    public LoginContext getLoginContext()
    {
        return lc;
    }

    public void setLoginContext(LoginContext lc)
    {
        this.lc = lc;
    }

	public ServerPI getServerPI()
	{
		return serverPI;
	}

	public void setServerPI(ServerPI serverPI)
	{
		this.serverPI = serverPI;
	}   
}
