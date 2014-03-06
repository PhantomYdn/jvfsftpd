package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;

/**
 * Command handler for the NLST command. If pathname is not specified, the
 * current directory will be used.
 * 
 * <pre>
 * NLST [%SP% %pathname%] %CRLF%
 * </pre>
 */
public class ListHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        
        FileObject folder = getPath(getPathFromParams(params));

        return command.equalsIgnoreCase("NLST")?getServerPI().dtp.sendNameList(folder):getServerPI().dtp.sendList(folder);
    }
    
    private String getPathFromParams(String args)
    {
        if(args==null) return null;
        String[] params = StringUtils.split(args);
        for(int i=0;i<params.length;i++)
        {
            if(!params[i].startsWith("-")) return params[i];
        }
        return null;
    }

}
