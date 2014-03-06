package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
/**
 * Command handler for the STRU command. The only supported argument is 'F'
 * for file structure.
 * 
 * <pre>
 * STRU %SP% %structure-code% %CRLF%
 * </pre>
 */
public class STRUHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params.toUpperCase();
        try
        {
            if (arg.length() != 1)
                throw new Exception();
            char stru = arg.charAt(0);
            switch (stru)
            {
            case 'F':
                getServerPI().dtp.setDataStructure(stru);
                break;
            default:
                throw new Exception();
            }
        }
        catch (Exception e)
        {
            throw new CommandException(500, "STRU: invalid argument '" + arg
                    + "'");
        }
        return FtpReplay.createReplay(200, "STRU {0} ok.",arg);
    }

}
