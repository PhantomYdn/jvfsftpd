package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;
import ru.skynet.jvfsftpd.TransmissionMode;
/**
 * Command handler for the MODE command. The only supported argument is 'S'
 * for STREAM.
 * 
 * <pre>
 * MODE %SP% %mode-code% %CRLF%
 * </pre>
 */
public class ModeHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException
    {
        String arg = params.toUpperCase();
        if (arg.length() != 1)
            throw new CommandException(500, "MODE: invalid argument '" + arg
                    + "'");
        char code = arg.charAt(0);
        TransmissionMode mode = TransmissionMode.get(code);
        if (mode == null)
            throw new CommandException(500, "MODE: invalid argument '" + arg
                    + "'");
        getServerPI().dtp.setTransmissionMode(mode);
        return FtpReplay.createReplay(200, "MODE {0} ok.", arg);
    }

}
