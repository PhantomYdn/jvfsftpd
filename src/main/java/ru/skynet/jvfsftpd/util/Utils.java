package ru.skynet.jvfsftpd.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;

public class Utils
{
    public static InetAddress getInetAddress(String IP) throws UnknownHostException
    {
        if(IP==null || IP.equals("0.0.0.0") || IP.equalsIgnoreCase("localhost"))
            return null;
        String[] IPStr = StringUtils.split(IP,'.');
        if(IPStr.length!=4) throw new UnknownHostException("Can't find host "+IP);
        byte[] addr = new byte[4];
        for(int i=0;i<4;i++)
        {
            addr[i]=(byte)Integer.parseInt(IPStr[i]);
        }
        return InetAddress.getByAddress(addr);
    }
}
