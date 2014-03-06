package ru.skynet.jvfsftpd;

import java.text.MessageFormat;

public class FtpReplay
{
/*	private static Map<Integer, String> templ = new HashMap<Integer, String>();
	static
	{
		//TODO: Fill it
	}*/
	
	private final int code;
	private final String message;
	
	private FtpReplay(int code, String message)
	{
		this.code=code;
		this.message=message;
	}
	
	public static FtpReplay createReplay(int code, String template, Object... args)
	{
		String message = MessageFormat.format(template, args);
		return new FtpReplay(code, message);
	}
	
	/*public static FtpReplay createFtpReplayFromTemplate(int code, Object... args)
	{
		String template = templ.get(code);
		if(template!=null)
		{
			String message = MessageFormat.format(template, args);
			return new FtpReplay(code, message);
		}
		else
		{
			log.warn("Code '"+code+"' hasn't template. Fix it.");
			String message = "";
			for (int i = 0; i < args.length; i++)
			{
				message+=args[i]+" ";				
			}
			message=message.substring(0, message.length()-1);
			return createCustomReplay(code, message);
		}
	}*/
	
	public static FtpReplay createCustomReplay(int code, String message)
	{
		return new FtpReplay(code, message);
	}

	public int getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}
	
	
}
