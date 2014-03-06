package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FTPServer;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the SYST command.
 * 
 * <pre>
 * SYST %CRLF%
 * </pre>
 */
public class SystemHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        return FtpReplay.createReplay(215, "UNIX - JVFSFTPd v."+FTPServer.VERSION);
    }

}
