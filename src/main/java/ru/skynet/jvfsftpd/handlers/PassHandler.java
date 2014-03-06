package ru.skynet.jvfsftpd.handlers;

import javax.security.auth.login.LoginException;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
import ru.skynet.jvfsftpd.config.FtpConfiguration;

/**
 * Command handler for the PASS command. A USER command must have been
 * read first.
 *
 * <pre>PASS %SP% %password% %CRLF%</pre>
 */

public class PassHandler extends AbstractHandler
{    
    public boolean requireLogin()
    {        
        return false;
    }

    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException
    {        
        if (getSession().getUsername() == null)
            throw new CommandException(503, "Login with USER first.");
        getSession().setPassword(params);
        try
        {
            FtpConfiguration.getConfiguration().getSessionManager().login(getSession());
            return FtpReplay.createReplay(230, "User {0} logged in.", getSession().getUsername());
        }
        catch (LoginException e)
        {
            return FtpReplay.createReplay(530, "User {0} cannot log in.",getSession().getUsername());
        }
    }
   
}
