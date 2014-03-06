package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the NOOP command.
 * 
 * <pre>
 * NOOP %CRLF%
 * </pre>
 */
public class NoopHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        return FtpReplay.createReplay(200, "{0} command successful.", command);
    }

}
