package ru.skynet.jvfsftpd.config;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import ru.skynet.jvfsftpd.util.UsernamePasswordHandler;

public class DefaultSessionManager implements SessionManager
{
    public void initSession(FtpSession session)
    {
        
    }

    public void reinitSession(FtpSession session) throws LoginException
    {
        if(session.getLoginContext()!=null) 
            session.getLoginContext().logout();
        session.setUsername(null);
        session.setPassword(null);
    }
    
    public void destroySession(FtpSession session)
    {
        // TODO Auto-generated method stub

    }

    public void login(FtpSession session) throws LoginException
    {
    	String securityDomain = FtpConfiguration.getConfiguration().getSecurityDomain();
        if(securityDomain==null)
        {          
            session.setLogged(true);
        }
        else
        {           
            UsernamePasswordHandler handler = new UsernamePasswordHandler(session);
            LoginContext lc = new LoginContext(securityDomain, handler);
            lc.login();
            session.setLoginContext(lc);
            session.setLogged(true);
        }
        session.setBaseDir(FtpConfiguration.getConfiguration().getInitialBaseDir(session));
        session.setCurrentDir(FtpConfiguration.getConfiguration().getInitialCurrentDir(session));
    }

    public void logout(FtpSession session) throws LoginException
    {
    	if(session.isLogged())
    	{
    		session.getLoginContext().logout();
    		session.setLogged(false);
    	}
    }

}
