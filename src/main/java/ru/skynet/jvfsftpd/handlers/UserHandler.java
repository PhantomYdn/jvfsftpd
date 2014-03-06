package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;


/**
 * Command handler for the USER command.
 *
 * <pre>USER %SP% %username% %CRLF%</pre>
 */

public class UserHandler extends AbstractHandler
{
    
    public boolean requireLogin()
    {
        return false;
    }

    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException
    {	
    	log.debug("test");
        getSession().setUsername(params);
        return FtpReplay.createReplay(331, "Password required for {0}.", params);
    }
   
}
