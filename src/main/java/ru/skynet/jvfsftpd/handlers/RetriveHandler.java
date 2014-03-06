package ru.skynet.jvfsftpd.handlers;


import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;


/**
 * Command handler for the RETR command. The ServerDTP is used to send the
 * data to the user.
 * 
 * <pre>
 * RETR %SP% %pathname% %CRLF%
 * </pre>
 */
public class RetriveHandler extends AbstractHandler
{
    
    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        FileObject file = getPath(params); 
//        log.debug("handle_retr: " + path);
        log.debug("handle_retr: " + file.getName());

        return getServerPI().dtp.sendFile(file);
    }

}
