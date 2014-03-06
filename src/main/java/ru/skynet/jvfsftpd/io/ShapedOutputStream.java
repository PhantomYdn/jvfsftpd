package ru.skynet.jvfsftpd.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ShapedOutputStream extends FilterOutputStream
{
    private long bandwidth;
    
    public ShapedOutputStream(OutputStream out, long bandwidth)
    {
        super(out);
        this.bandwidth = bandwidth;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        Shaper.start();
        out.write(b,off,len);
        Shaper.stop(len,bandwidth);
    }

    @Override
    public void write(int b) throws IOException
    {
        Shaper.start();
        out.write(b);
        Shaper.stop(1,bandwidth);
    }       
}
