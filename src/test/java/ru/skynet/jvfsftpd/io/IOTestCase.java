package ru.skynet.jvfsftpd.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

public class IOTestCase extends TestCase
{

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(IOTestCase.class);
    }
    
    public void testShapedInputStream1() throws Exception
    {
        final int bufSize = 512;
        final long bandwidth = 512;
        byte[] buf = new byte[bufSize];
        byte[] toBuf = new byte[32];
        Arrays.fill(buf,(byte)0);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ShapedInputStream sis = new ShapedInputStream(bais,bandwidth);
        long start = System.currentTimeMillis();
        while(sis.read(toBuf)>=0);
        long stop = System.currentTimeMillis();
        //System.out.println(start+"   "+stop);
        float k = (stop - start)/(1000*bufSize/bandwidth);
        System.out.println("k="+k);
        assertTrue(k>=1);      
        assertTrue(k<=1.1);
    }
    
    public void testShapedInputStream2() throws Exception
    {
        final int bufSize = 512;
        final long bandwidth = 512;
        byte[] buf = new byte[bufSize];
        Arrays.fill(buf,(byte)0);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ShapedInputStream sis = new ShapedInputStream(bais,bandwidth);
        long start = System.currentTimeMillis();
        while(sis.read()>=0);
        long stop = System.currentTimeMillis();
        //System.out.println(start+"   "+stop);
        float k = (stop - start)/(1000*bufSize/bandwidth);
        System.out.println("k="+k);
        assertTrue(k>=1);  
        assertTrue(k<=1.1);
    }
   
    
    public void testShapedOutputStream1() throws Exception
    {
        final int bufSize = 512;
        final long bandwidth = 512;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ShapedOutputStream sos = new ShapedOutputStream(baos, bandwidth);
        byte[] buf = new byte[bufSize];
        Arrays.fill(buf, (byte)0);
        long start = System.currentTimeMillis();
        sos.write(buf);
        long stop = System.currentTimeMillis();
        //System.out.println(start+"   "+stop);
        float k = (stop - start)/(1000*bufSize/bandwidth);
        System.out.println("k="+k);
        assertTrue(k>=1);  
        assertTrue(k<=1.1);
    }
    
    public void testShapedOutputStream2() throws Exception
    {
        final int bufSize = 512;
        final long bandwidth = 512;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ShapedOutputStream sos = new ShapedOutputStream(baos, bandwidth);
        long start = System.currentTimeMillis();
        for(int i=0;i<bufSize;i++) sos.write(0);
        long stop = System.currentTimeMillis();
        //System.out.println(start+"   "+stop);
        float k = (stop - start)/(1000*bufSize/bandwidth);
        System.out.println("k="+k);
        assertTrue(k>=1);  
        assertTrue(k<=1.1);
    }

}
