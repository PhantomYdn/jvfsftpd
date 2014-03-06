package ru.skynet.jvfsftpd.util;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import ru.skynet.jvfsftpd.config.FtpSession;

public class UsernamePasswordHandler implements CallbackHandler
{
	private final String username;
	private final String password;
	public UsernamePasswordHandler(String username, String password)
	{
		this.username=username;
		this.password=password;
	}
	public UsernamePasswordHandler(FtpSession session)
	{
		this.username=session.getUsername();
		this.password=session.getPassword();
	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException
	{
		for (int i = 0; i < callbacks.length; i++)
		{
			Callback callback = callbacks[i];
			if(callback instanceof NameCallback)
			{
				NameCallback nameCallback = (NameCallback)callback;
				nameCallback.setName(username);
			}
			else if(callback instanceof PasswordCallback)
			{
				PasswordCallback passwordCallback = (PasswordCallback)callback;
				passwordCallback.setPassword(password.toCharArray());
			}
			else
			{
				throw new UnsupportedCallbackException(callback);
			}
		}
	}

}
