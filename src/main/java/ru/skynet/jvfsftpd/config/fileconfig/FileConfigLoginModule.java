package ru.skynet.jvfsftpd.config.fileconfig;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.skynet.jvfsftpd.config.FtpConfiguration;

public class FileConfigLoginModule implements LoginModule
{
	private static final Log log = LogFactory.getLog(FileConfigLoginModule.class);
	private static final XPath xpath = XPathFactory.newInstance().newXPath();
	
	private Subject subject=null;
	private CallbackHandler callbackHandler=null;
	private Map<String, ?> sharedState=null;
	private Map<String, ?> options=null;
	
	public boolean abort() throws LoginException
	{
		return true;
	}

	public boolean commit() throws LoginException
	{
		return true;
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
	{
		this.subject=subject;
		this.callbackHandler=callbackHandler;
		this.sharedState=sharedState;
		this.options=options;
	}

	public boolean login() throws LoginException
	{
		try
		{
			NameCallback nameCallback = new NameCallback("Please enter loginname");
			PasswordCallback passwordCallback = new PasswordCallback("Please enter password",false);
			Callback[] callbacks = new Callback[]{nameCallback,passwordCallback};
			callbackHandler.handle(callbacks);
			String name = nameCallback.getName();
			String expr = "/ftpd/user[@name=\""+name+"\"]";
			Element user = (Element)xpath.evaluate(expr, getDocument(), XPathConstants.NODE);
			if(user==null) return false;
			String pass = user.getAttribute("password");
			String enteredPass = new String(passwordCallback.getPassword());
			if(pass==null) return true;
			else return pass.equals(enteredPass);
		} 
		catch (Exception e)
		{
			log.error("Exception during login", e);
			throw new LoginException(e.getMessage());
		}
	}
	
	private Document getDocument()
	{
		return ((FileFtpConfiguration)FtpConfiguration.getConfiguration()).getDoc();
	}

	public boolean logout() throws LoginException
	{
		return true;
	}
}
