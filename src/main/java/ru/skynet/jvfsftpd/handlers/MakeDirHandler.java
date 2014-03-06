package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the MKD command.
 * 
 * <pre>
 * MKD %SP% %pathname% %CRLF%
 * </pre>
 */
public class MakeDirHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params;


        FileObject dir = getPath(arg); 
        if (dir.exists())
            throw new CommandException(550, arg + ": file exists");
        try
        {
            dir.createFile();
        }
        catch(FileSystemException exc)
        {
            throw new CommandException(550, arg
                    + ": directory could not be created");
        }        
        return FtpReplay.createReplay(257, "\"{0}\" directory created", getRelativePath(dir));
    }

}
