package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.*;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the CWD command.
 * 
 * <pre>
 * CWD %SP% %pathname% %CRLF%
 * </pre>
 */
public class ChangeDirHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException
    {
        String arg = params;

        String newDir = params;
        if (newDir.length() == 0)
            newDir = "/";
        
        FileObject file = getPath( newDir); 
        if (!file.exists())
            throw new CommandException(550, arg + ": no such directory");
        if (!file.getType().equals(FileType.FOLDER))
            throw new CommandException(550, arg + ": not a directory");

        getSession().setCurrentDir(file);
//        log.debug("new cwd = " + serverPI.currentDir);
        log.debug("new cwd = " + getSession().getCurrentDir());
        return FtpReplay.createReplay(250, "{0} command successful.", command);
    }    
}
