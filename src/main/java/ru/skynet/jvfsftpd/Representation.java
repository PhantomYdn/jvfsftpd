
package ru.skynet.jvfsftpd;

import org.apache.commons.vfs.*;

import ru.skynet.jvfsftpd.config.FtpConfiguration;
import ru.skynet.jvfsftpd.config.FtpSession;
import ru.skynet.jvfsftpd.io.ShapedInputStream;
import ru.skynet.jvfsftpd.io.ShapedOutputStream;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;

import java.util.Hashtable;

/**
 * A File is transferred to and from the FTP server using a particular
 * representation. The server can be told which representation type to use
 * with the TYPE command. Each subclass of Representation handles the
 * conversion of data between that type and the server's storage
 * representation.
 */
public abstract class Representation
{
	/**
	 * A code to representation map.
	 */
	private static Hashtable representations = new Hashtable();

	/**
	 * The ASCII representation.
	 */
	public static final Representation ASCII = new AsciiRepresentation();

	/**
	 * The IMAGE representation.
	 */
	public static final Representation IMAGE = new ImageRepresentation();

	/**
	 * @return the Representation indicated by the code argument.
	 */
	public static Representation get(char code)
		{
		return (Representation)representations.get(new Character(code));
		}

	/**
	 * The name of the representation.
	 */
	private String name;

	/**
	 * The character code for this representation.
	 */
	private char code;

	protected Representation(String name, char code)
	{
    	this.name = name;
    	this.code = code;
    
    	representations.put(new Character(code), this);
	}

	/**
	 * @return the name of this representation type.
	 */
	public final String getName()
		{
		return name;
		}

	/**
	 * @return the character code for this representation type.
	 */
	public final char getCode()
	{
	    return code;
	}

	/**
	 * @return an input stream to read data from the socket. Data will be
	 * translated from this representation to the server's representation.
	 */
    protected abstract InputStream getInputStream(InputStream in) throws IOException;       
    
	public final InputStream getInputStream(Socket socket)
		throws IOException
    {
        long bandwidth = FtpConfiguration.getConfiguration().getBandwidthInput(FtpSession.getSession());
        return getInputStream(socket, bandwidth);
    }
        
    private InputStream getInputStream(Socket socket, long bandwidth)
        throws IOException
    {
    	if(bandwidth<=0)
    	{
    		return getInputStream(socket.getInputStream());
    	}
    	else
    	{
    		return getInputStream(new ShapedInputStream(socket.getInputStream(), bandwidth));
    	}        
    }

	/**
	 * @return an output stream to write data to the socket. Data will be
	 * translated from the server's representation to this representation.
	 */
    
    protected abstract OutputStream getOutputStream(OutputStream out) throws IOException;
    
	public final OutputStream getOutputStream(Socket socket) throws IOException
    {
		long bandwidth = FtpConfiguration.getConfiguration().getBandwidthOutput(FtpSession.getSession());
        return getOutputStream(socket, bandwidth);
    }
    
    private OutputStream getOutputStream(Socket socket, long bandwidth) throws IOException
    {
    	if(bandwidth<=0)
    	{
    		return getOutputStream(socket.getOutputStream());
    	}
    	else
    	{
    		return getOutputStream(new ShapedOutputStream(socket.getOutputStream(), bandwidth));
    	}        
    }

	/**
	 * @return the size that the specified file would have in this
	 * representation.
	 */
	public abstract long sizeOf(FileObject file)
		throws IOException;
}
