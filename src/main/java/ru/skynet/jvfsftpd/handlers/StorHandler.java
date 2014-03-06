package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the STOR command. The ServerDTP is used to receive
 * the data on the data port.
 * 
 * <pre>
 * STOR %SP% %pathname% %CRLF%
 * </pre>
 */
public class StorHandler extends AbstractHandler
{
    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        FileObject file = getPath(params);
        return getServerPI().dtp.receiveFile(file);
    }
}
