package org.bladerunnerjs.logging;

import java.util.IllegalFormatException;

public class SLF4JLogger implements Logger
{
	private final org.slf4j.Logger slf4jLogger;
	private final String name;

	public SLF4JLogger(org.slf4j.Logger slf4jLogger, String name)
	{
		this.slf4jLogger = slf4jLogger;
		this.name = name;
	}
	
	@Override
	public void error(String message, Object... params)
	{
		if (slf4jLogger.isErrorEnabled())
		{
			slf4jLogger.error( getFormattedString(message, params) );
		}
	}

	@Override
	public void warn(String message, Object... params)
	{
		if (slf4jLogger.isWarnEnabled())
		{
			slf4jLogger.warn( getFormattedString(message, params) );
		}
	}

	@Override
	public void info(String message, Object... params)
	{
		if (slf4jLogger.isInfoEnabled())
		{
			slf4jLogger.info( getFormattedString(message, params) );
		}
	}

	@Override
	public void debug(String message, Object... params)
	{
		if (slf4jLogger.isDebugEnabled())
		{
			slf4jLogger.debug( getFormattedString(message, params) );
		}
	}
	
	private String getFormattedString(String message, Object... params)
	{
		try {
			return (params.length == 0) ? message : String.format(message, params);
		}
		catch (IllegalFormatException ex) /* IllegalFormatException is a runtime exception */
		{
			slf4jLogger.error("Attempted to log a message but received a " + ex.getClass().getCanonicalName() + ".\n"+
					"Message: " + message + "\n"+
					"Params: " + params);
			throw ex;
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void println(String message, Object... params)
	{
		System.out.println( getFormattedString(message, params) );
	}

	@Override
	public void console(String message, Object... params)
	{
		println(message, params);
	}

}
