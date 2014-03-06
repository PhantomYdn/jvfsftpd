package ru.skynet.jvfsftpd.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the MDTM command.
 * 
 * <pre>
 * MDTM %SP% %pathname% %CRLF%
 * </pre>
 */
public class MDTMHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params;
        FileObject file = getPath(arg); 
        log.debug(file.getName()+" "+file.exists());
        if (!file.exists())
            throw new CommandException(550, arg + ": no such file");

        Date date = new Date(file.getContent().getLastModifiedTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateStr = dateFormat.format(date);
        return FtpReplay.createReplay(213, dateStr);
    }

}
