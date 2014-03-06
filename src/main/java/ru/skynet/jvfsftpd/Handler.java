package ru.skynet.jvfsftpd;

import org.apache.commons.vfs.FileSystemException;

public interface Handler
{
    public boolean requireLogin();
    public FtpReplay handle(String command, String params) throws CommandException, FileSystemException;
}
