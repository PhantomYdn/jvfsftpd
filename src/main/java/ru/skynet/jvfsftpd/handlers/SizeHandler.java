package ru.skynet.jvfsftpd.handlers;

import java.io.IOException;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
import ru.skynet.jvfsftpd.Representation;
/**
 * Command handler for the SIZE command.
 * 
 * <pre>
 * SIZE %SP% %pathname% %CRLF%
 * </pre>
 */
public class SizeHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params;
        FileObject file = getPath(arg); 

        if (!file.exists())
            throw new CommandException(550, arg + ": no such file");
        if (file.getType().equals(FileType.FOLDER))
            throw new CommandException(550, arg + ": not a plain file");

        Representation representation = getServerPI().dtp.getRepresentation();
        long size;
        try
        {
            size = representation.sizeOf(file);
        }
        catch (IOException e)
        {
            throw new CommandException(550, e.getMessage());
        }

        // XXX: in ascii mode, we must count newlines properly
        return FtpReplay.createReplay(213, "{0}",size);
    }

}
