package ru.skynet.jvfsftpd.io;

class Shaper
{
    private static ThreadLocal<Long> tl = new ThreadLocal<Long>();
    private static ThreadLocal<Long> addon = new ThreadLocal<Long>();
    public static final int DELTA = 5;
    static
    {
    	addon.set((long)0);
    }
    public static void start()
    {
        tl.set(System.currentTimeMillis());
    }
    
    public static void stop(long size, long bandwidth)
    {
        long used = System.currentTimeMillis() - tl.get();
        long mustBeUsed = 1000*size/bandwidth+1;
        long toSleep = mustBeUsed-used+addon.get();
        if(toSleep>DELTA)
        {
        	//System.out.println("size="+size+" bandwidth="+bandwidth+" toSleep="+toSleep);
            try
            {
            	Thread.sleep(toSleep);
            }
            catch (InterruptedException e)
            {
                //NOP
            }
            addon.set((long)0);
        }
        else
        {
        	addon.set(toSleep);
        }
    }
}
