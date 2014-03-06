
package ru.skynet.jvfsftpd;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;


public class StreamTransmissionMode
	extends TransmissionMode
{
	private static final int BUFSIZ = 1024;

	StreamTransmissionMode()
	{
		super('S');
	}

	public void sendFile(InputStream in, Socket s, Representation representation)
		throws IOException
		{
		OutputStream out = representation.getOutputStream(s);
		byte buf[] = new byte[BUFSIZ];
		int nread;
		while ((nread = in.read(buf)) > 0)
		{
			out.write(buf, 0, nread);
		}
		out.close();
		}


	public void receiveFile(Socket s, OutputStream out, Representation representation)
		throws IOException
		{
		InputStream in = representation.getInputStream(s);
		byte buf[] = new byte[BUFSIZ];
		int nread;
		while ((nread = in.read(buf, 0, BUFSIZ)) > 0)
			{
			out.write(buf, 0, nread);
			}
		in.close();
		}
}
