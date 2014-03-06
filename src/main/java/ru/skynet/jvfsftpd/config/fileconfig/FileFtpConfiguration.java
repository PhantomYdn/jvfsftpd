package ru.skynet.jvfsftpd.config.fileconfig;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.spi.LoginModule;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.skynet.jvfsftpd.IncorrectConfigurationException;
import ru.skynet.jvfsftpd.config.FtpConfiguration;
import ru.skynet.jvfsftpd.config.FtpSession;
import ru.skynet.jvfsftpd.util.DeligateConfiguration;
import ru.skynet.jvfsftpd.util.Utils;

public class FileFtpConfiguration extends FtpConfiguration
{
	private final Document doc;
	private static final XPath xpath = XPathFactory.newInstance().newXPath();
	private String securityDomain=null;
	
    public Document getDoc()
	{
		return doc;
	}

	public FileFtpConfiguration(File configFile)
    {
    	try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configFile);
			String useUsers = evalXPath("/ftpd/use-users/text()");
			if(!StringUtils.isEmpty(useUsers))
			{
				boolean use = Boolean.parseBoolean(useUsers);
				if(use)
				{
					securityDomain = RandomStringUtils.randomAlphabetic(5);
					AppConfigurationEntry entry = new AppConfigurationEntry(FileConfigLoginModule.class.getName(),LoginModuleControlFlag.REQUIRED, new HashMap());
					DeligateConfiguration deligateConfiguration = new DeligateConfiguration(getJAASConfiguration(),securityDomain,new AppConfigurationEntry[]{entry});
					Configuration.setConfiguration(deligateConfiguration);
				}
			}
		} 
    	catch (Exception e)
		{
			throw new IncorrectConfigurationException(e);
		} 
    }
	
	private Configuration getJAASConfiguration()
	{
		try
		{
			return Configuration.getConfiguration();
		} 
		catch (RuntimeException e)
		{
			return null;
		}
	}

    @Override
    public int getServerPort()
    {
    	String portStr = evalXPath("/ftpd/port/text()");
    	return StringUtils.isEmpty(portStr)?super.getServerPort():Integer.parseInt(portStr);
    }

    @Override
    public InetAddress getBindAddress()
    {
    	try
		{
			String bindStr = evalXPath("/ftpd/bind/text()");
			return StringUtils.isEmpty(bindStr)?super.getBindAddress():Utils.getInetAddress(bindStr);
		}
    	catch (UnknownHostException e)
		{
			throw new IncorrectConfigurationException(e);
		}
    }

	@Override
	public long getBandwidthInput(FtpSession session)
	{
		String expr = "/ftpd/user[@name=\""+session.getUsername()+"\"]/bandwidth/input/text()";
		String bandStr = evalXPath(expr);
		if(StringUtils.isEmpty(bandStr))
		{
			bandStr = evalXPath("/ftpd/default/bandwidth/input/text()");
		}
		return StringUtils.isEmpty(bandStr)?super.getBandwidthInput(session):Long.parseLong(bandStr);
	}

	@Override
	public long getBandwidthOutput(FtpSession session)
	{
		String expr = "/ftpd/user[@name=\""+session.getUsername()+"\"]/bandwidth/output/text()";
		String bandStr = evalXPath(expr);
		if(StringUtils.isEmpty(bandStr))
		{
			bandStr = evalXPath("/ftpd/default/bandwidth/output/text()");
		}
		return StringUtils.isEmpty(bandStr)?super.getBandwidthInput(session):Long.parseLong(bandStr);
	}

	@Override
	public String getSecurityDomain()
	{
		if(securityDomain==null)
		{
			securityDomain = evalXPath("/ftpd/security-domain/text()");
		}
		return securityDomain;
	}

	@Override
	public FileObject getInitialBaseDir(FtpSession session)
	{
		String expr = "/ftpd/user[@name=\""+session.getUsername()+"\"]/basedir/text()";
		String dirStr = evalXPath(expr);
		if(StringUtils.isEmpty(dirStr))
		{
			dirStr = evalXPath("/ftpd/default/basedir/text()");
		}
		if(StringUtils.isEmpty(dirStr) )
			throw new IncorrectConfigurationException("Basedir is not set");
		try
		{			
			FileSystemManager fsManager = VFS.getManager();
			return fsManager.resolveFile(dirStr);
		} 
		catch (FileSystemException e)
		{
			throw new IncorrectConfigurationException(e);
		}
	}

	@Override
	public FileObject getInitialCurrentDir(FtpSession session)
	{
		String expr = "/ftpd/user[@name=\""+session.getUsername()+"\"]/initialdir/text()";
		String dirStr = evalXPath(expr);
		if(StringUtils.isEmpty(dirStr))
		{
			dirStr = evalXPath("/ftpd/default/initialdir/text()");
		}
		if(StringUtils.isEmpty(dirStr))
			return getInitialBaseDir(session);
		try
		{			
			FileSystemManager fsManager = VFS.getManager();
			return fsManager.resolveFile(dirStr);
		} 
		catch (FileSystemException e)
		{
			throw new IncorrectConfigurationException(e);
		}
	}
	
	private String evalXPath(String expr, String def)
	{
		return StringUtils.defaultString(evalXPath(expr), def);
	}
	
	private String evalXPath(String expr)
	{
		try
		{
			return (String) xpath.evaluate(expr, doc, XPathConstants.STRING);
		}
		catch (XPathExpressionException e)
		{
			throw new IncorrectConfigurationException(e);
		}
	}

}
