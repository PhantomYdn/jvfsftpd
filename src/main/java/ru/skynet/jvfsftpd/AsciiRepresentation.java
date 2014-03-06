
package ru.skynet.jvfsftpd;

import org.apache.commons.vfs.*;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AsciiRepresentation
	extends Representation
{
	AsciiRepresentation()
		{
		super("ascii", 'A');
		}

	protected InputStream getInputStream(InputStream in)
		throws IOException
	{
	    return new AsciiInputStream(in);
	}

    protected OutputStream getOutputStream(OutputStream out)
		throws IOException
	{
	    return new AsciiOutputStream(out);
	}

	public long sizeOf(FileObject file)
		throws IOException
		{
		InputStream in = file.getContent().getInputStream();

		long count = 0;

		try
			{
			int c;
			while ((c = in.read()) != -1)
				{
				if (c == '\r')
					continue;
				if (c == '\n')
					count++;
				count++;
				}
			}
		finally
			{
			in.close();
			}

		return count;
		}
}

class AsciiInputStream
	extends FilterInputStream
{
	public AsciiInputStream(InputStream in)
		{
		super(in);
		}

	public int read()
		throws IOException
		{
		int c;
		if ((c = in.read()) == -1)
			return c;
		if (c == '\r')
			{
			if ((c = in.read()) == -1)
				return c;
			}
		return c;
		}

	public int read(byte data[], int off, int len)
		throws IOException
		{
		if (len <= 0)
			return 0;

		int c;
		
		if ((c = read()) == -1)
			return -1;
		else
			data[off] = (byte)c;

		int i = 1;
		try
			{
			for (; i < len; i++)
				{
				if ((c = read()) == -1)
					break;
				if (c == '\r')
					{
					if ((c = in.read()) == -1)
						break;
					}
				data[off + i] = (byte)c;
				}
			}
		catch (IOException e)
			{
			}

		return i;
		}
}

/**
 * This output stream converts "\n" to "\r\n" before writing the data.
 */
class AsciiOutputStream
	extends FilterOutputStream
{
	public AsciiOutputStream(OutputStream out)
		{
		super(out);
		}

	public void write(int b)
		throws IOException
		{
		if (b == '\r')
			return;
		if (b == '\n')
			out.write('\r');
		out.write(b);
		}

	public void write(byte data[], int off, int len)
		throws IOException
		{
		for (int i = 0; i < len; i++)
			{
			byte b = data[off + i];
			if (b == '\n')
				out.write('\r');
			out.write(b);
			}
		}
}
