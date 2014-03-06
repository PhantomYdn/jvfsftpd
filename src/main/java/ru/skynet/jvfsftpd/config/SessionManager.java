package ru.skynet.jvfsftpd.config;

import javax.security.auth.login.LoginException;

public interface SessionManager
{
    public void initSession(FtpSession session);
    public void reinitSession(FtpSession session) throws LoginException;
    public void login(FtpSession session) throws LoginException;
    public void logout(FtpSession session) throws LoginException;
    public void destroySession(FtpSession session);
}
