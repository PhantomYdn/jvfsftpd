package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the DELE command.
 * 
 * <pre>
 * DELE %SP% %pathname% %CRLF%
 * </pre>
 */
public class DeleteHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params;

        FileObject file = getPath(arg); 
        if (!file.exists())
            throw new CommandException(550, arg + ": file does not exist");
        if (command.equalsIgnoreCase("RMD") && !file.getType().equals(FileType.FOLDER))
            throw new CommandException(550, arg + ": not a directory");
        if (!file.delete())
            throw new CommandException(550, arg + ": could not delete file");
        return FtpReplay.createReplay(250, "{0} command successful", command);
    }
}
