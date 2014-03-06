package ru.skynet.jvfsftpd.handlers;

import java.io.IOException;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;

public class PassivateHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        try
        {
            String addr = getServerPI().dtp.passivate();
            return FtpReplay.createReplay(227,"Entering Passive Mode. {0}",addr);
        }
        catch (IOException e)
        {
            throw new CommandException(500, "Can't passivate");
        }
    }

}
