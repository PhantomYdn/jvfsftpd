package ru.skynet.jvfsftpd.handlers;

import javax.security.auth.login.LoginException;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
import ru.skynet.jvfsftpd.ServerDTP;

/**
 * Command handler for the REIN command.
 * 
 * <pre>
 * REIN %CRLF%
 * </pre>
 */
public class ReinitializeHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        try
        {
            if(getSession().getLoginContext()!=null)getSession().getLoginContext().logout();
            getSession().setUsername(null);
            getSession().setPassword(null);
            getSession().setCurrentDir(getSession().getBaseDir());
            getServerPI().dtp = new ServerDTP(getServerPI());
            return FtpReplay.createReplay(220, "Service ready for new user.");
        }
        catch (LoginException e)
        {
            return FtpReplay.createReplay(530, "User {0} cannot log out.", getSession().getUsername());
        }
        
    }

}
