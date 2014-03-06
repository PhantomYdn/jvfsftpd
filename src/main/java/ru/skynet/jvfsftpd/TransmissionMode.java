
package ru.skynet.jvfsftpd;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;

import java.util.Hashtable;


public abstract class TransmissionMode
{

	private static Hashtable transmissionModes = new Hashtable();


	public static final TransmissionMode STREAM = new StreamTransmissionMode();


	public static TransmissionMode get(char code)
		{
		return (TransmissionMode)transmissionModes.get(new Character(code));
		}


	private char code;

	protected TransmissionMode(char code)
	{
		this.code = code;

		transmissionModes.put(new Character(code), this);
	}


	public final char getCode()
	{
		return code;
	}


	public abstract void sendFile(InputStream in, Socket s, Representation representation)
		throws IOException;

	public abstract void receiveFile(Socket s, OutputStream out, Representation representation)
		throws IOException;
}
