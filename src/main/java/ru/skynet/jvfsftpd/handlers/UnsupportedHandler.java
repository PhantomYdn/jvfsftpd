package ru.skynet.jvfsftpd.handlers;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;

public class UnsupportedHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException
    {
        throw new CommandException(501, command + " not supported");
    }
  
}
