
package ru.skynet.jvfsftpd;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.vfs.FileObject;

/**
 * This class converts data streams to and from IMAGE representation.
 */
public class ImageRepresentation
	extends Representation
{
	ImageRepresentation()
		{
		super("binary", 'I');
		}

	/**
	 * @return an input stream to read data from the socket. Data will be
	 * translated from IMAGE representation to local representation.
	 */
	protected InputStream getInputStream(InputStream in)
		throws IOException
	{
	    return in;
	}

	/**
	 * @return an output stream to write data to the socket. Data will be
	 * translated from local representation to IMAGE representation.
	 */
    protected OutputStream getOutputStream(OutputStream out)
		throws IOException
	{
	    return out;
	}

	/**
	 * @return the size that the specified file would have in this
	 * representation.
	 */
	public long sizeOf(FileObject file)
		throws IOException
		{
		return file.getContent().getSize();
		}
}
