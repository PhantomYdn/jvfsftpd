package ru.skynet.jvfsftpd.handlers;

import javax.security.auth.login.LoginException;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;

/**
 * Command handler for the QUIT command.
 * 
 * <pre>
 * QUIT %CRLF%
 * </pre>
 */
public class QuitHandler extends AbstractHandler
{
    public boolean requireLogin()
    {
        return false;
    }

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        try
        {
            if(getSession().getLoginContext()!=null) 
                getSession().getLoginContext().logout();
            getSession().setUsername(null);
            getSession().setPassword(null);
            return FtpReplay.createReplay(221, "Goodbye.");
        }
        catch (LoginException e)
        {
            return FtpReplay.createReplay(530, "User {0} cannot log out.", getSession().getUsername());
        }
    }

}
