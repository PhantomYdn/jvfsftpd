package ru.skynet.jvfsftpd.util;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

public class DeligateConfiguration extends Configuration
{
	private final Configuration config;
	private final Map<String, AppConfigurationEntry[]> addon;
	
	public DeligateConfiguration(Configuration config, String domain, AppConfigurationEntry[] appConfigurationEntries)
	{
		this.config=config;
		this.addon=new HashMap<String, AppConfigurationEntry[]>();
		this.addon.put(domain, appConfigurationEntries);
	}
	public DeligateConfiguration(Configuration config, Map<String, AppConfigurationEntry[]> addon)
	{
		this.config=config;
		this.addon=addon;
	}
	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name)
	{
		AppConfigurationEntry[] ret = addon.get(name);
		if(ret==null && config!=null)
		{
			ret = config.getAppConfigurationEntry(name);
		}			
		return ret!=null?ret:addon.get("other");
	}

	@Override
	public void refresh()
	{
		if(config!=null) config.refresh();
	}
	
}
