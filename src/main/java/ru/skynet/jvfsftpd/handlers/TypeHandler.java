package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
import ru.skynet.jvfsftpd.Representation;

/**
 * Command handler for the TYPE command. Supported arguments are 'A' for
 * ASCII and 'I' for IMAGE.
 * 
 * <pre>
 * TYPE %SP% %type-code% %CRLF%
 * </pre>
 */

public class TypeHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String arg = params.toUpperCase();
        if (arg.length() != 1)
            throw new CommandException(500, "TYPE: invalid argument '" + arg
                    + "'");
        char code = arg.charAt(0);
        Representation representation = Representation.get(code);
        if (representation == null)
            throw new CommandException(500, "TYPE: invalid argument '" + arg
                    + "'");
        getServerPI().dtp.setRepresentation(representation);
        return FtpReplay.createReplay(200, "Type set to {0}");
    }

}
