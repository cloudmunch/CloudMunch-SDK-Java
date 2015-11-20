package com.cloudmunch.javasdk.plugin.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PluginLogHandler {

    public enum LogModes {
	DEBUG, INFO, ERROR, WARN;
    }

    public static boolean validLogLevel(String logLevel) {

	for (LogModes c : LogModes.values()) {
	    if (c.name().equals(logLevel)) {
		return true;
	    }
	}
	return false;
    }

    public static void logHandler(String loglevel, String message) {
	SimpleDateFormat dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");
	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	String currentTime = dateFormat.format(new Date(System
		.currentTimeMillis()));

	LogModes logMode = LogModes.valueOf(loglevel);

	String logLevelWithDate = "[" + loglevel + "]" + " ["
		+ currentTime + "] ";

	if (null == logMode) {
	    System.err.println(logLevelWithDate + "Unknow logging mode "
		    + loglevel + " : " + message);
	} else {
	    switch (logMode) {
	    case DEBUG: {
		if (debugEnabled())
		    System.out.println(logLevelWithDate + message);
		break;
	    }
	    case INFO: {
		System.out.println(logLevelWithDate + message);
		break;
	    }
	    case WARN: {
		System.out.println(logLevelWithDate + message);
		break;
	    }
	    case ERROR: {
		System.err.println(logLevelWithDate + message);
		break;
	    }
	    }
	}

    }

    private static boolean debugEnabled() {
	return false;
    }

    public static void dummyerrorHandler() {

    }
}