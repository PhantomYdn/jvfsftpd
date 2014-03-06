package ru.skynet.jvfsftpd.config.clconfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

import ru.skynet.jvfsftpd.IncorrectConfigurationException;
import ru.skynet.jvfsftpd.config.FtpConfiguration;
import ru.skynet.jvfsftpd.config.FtpSession;
import ru.skynet.jvfsftpd.util.Utils;

public class CLFtpConfiguration extends FtpConfiguration
{
	public static final String PORT_PROPERTY = "port";
	public static final String BINDADDRESS_PROPERTY = "bind";
	public static final String BANDWIDTH_IN_PROPERTY="bandwidthin";
	public static final String BANDWIDTH_OUT_PROPERTY="bandwidthout";
	public static final String SECURITY_DOMAIN_PROPERTY="securitydomain";
	public static final String BASE_DIR_PROPERTY="basedir";
    @Override
    public int getServerPort()
    {
        String portStr = System.getProperty(PORT_PROPERTY, "21");
        return Integer.parseInt(portStr);
    }

    @Override
    public InetAddress getBindAddress()
    {
        try
		{
			String bindStr = System.getProperty(BINDADDRESS_PROPERTY,"0.0.0.0");
			return Utils.getInetAddress(bindStr);
		} 
        catch (UnknownHostException e)
		{
			throw new IncorrectConfigurationException(e);
		}
    }

	@Override
	public long getBandwidthInput(FtpSession session)
	{
		String bandwidthStr = System.getProperty(BANDWIDTH_IN_PROPERTY, "-1");
		return Long.parseLong(bandwidthStr);
	}

	@Override
	public long getBandwidthOutput(FtpSession session)
	{
		String bandwidthStr = System.getProperty(BANDWIDTH_OUT_PROPERTY, "-1");
		return Long.parseLong(bandwidthStr);
	}

	@Override
	public String getSecurityDomain()
	{
		return System.getProperty(SECURITY_DOMAIN_PROPERTY);
	}

	@Override
	public FileObject getInitialBaseDir(FtpSession session)
	{
		String baseDirStr = System.getProperty(BASE_DIR_PROPERTY);
		if(baseDirStr==null) throw new IncorrectConfigurationException("Base dir is not specified");
		try
		{			
			FileSystemManager fsManager = VFS.getManager();
			return fsManager.resolveFile(baseDirStr);
		} 
		catch (FileSystemException e)
		{
			throw new IncorrectConfigurationException(e);
		}
	}

	@Override
	public FileObject getInitialCurrentDir(FtpSession session)
	{
		return getInitialBaseDir(session);
	}

}
