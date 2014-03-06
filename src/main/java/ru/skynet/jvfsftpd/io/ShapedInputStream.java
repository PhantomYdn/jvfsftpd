package ru.skynet.jvfsftpd.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShapedInputStream extends FilterInputStream
{
    private long bandwidth;
    private boolean countSkip = false;
    /**
     * Constructor for ShapeInputStream
     * @param in - input stream
     * @param bandwidth - bandwidth of the stream: bytes/sec
     */
    public ShapedInputStream(InputStream in, long bandwidth)
    {
        super(in);
        this.bandwidth = bandwidth;
    }
    
    public ShapedInputStream(InputStream in, long bandwidth, boolean countSkip)
    {
        super(in);
        this.bandwidth = bandwidth;
        this.countSkip = countSkip;
    }

    @Override
    public int read() throws IOException
    {
        Shaper.start();
        int ret = in.read();
        Shaper.stop(1,bandwidth);
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        Shaper.start();
        int ret = in.read(b, off, len);
        Shaper.stop(ret,bandwidth);
        return ret;
    }

    @Override
    public long skip(long n) throws IOException
    {
        if(countSkip)
        {
            Shaper.start();
            long ret = super.skip(n);
            Shaper.stop(ret, bandwidth);
            return ret;
        }
        else
        {
            return super.skip(n);
        }
    }
    
    

}
