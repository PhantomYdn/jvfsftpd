package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the PWD command.
 * 
 * <pre>
 * PWD %CRLF%
 * </pre>
 */
public class CurentDirHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        log.debug("PWD: "+getSession().getCurrentDir());
        return FtpReplay.createReplay(257, "\"{0}\" is current directory.",getCurrentPath());
    }
}
