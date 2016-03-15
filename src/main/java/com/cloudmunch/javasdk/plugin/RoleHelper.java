package com.cloudmunch.javasdk.plugin;

import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class RoleHelper {

    private PluginContext pluginContext = null;
    private PluginLogHandler pluginLogHandler = null;

    public RoleHelper(PluginContext appContext,
	    PluginLogHandler logHandler)
    {
	pluginContext = appContext;
	pluginLogHandler = logHandler;

    }

}
