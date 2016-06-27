package com.cloudmunch.javasdk;

import com.cloudmunch.javasdk.plugin.PluginContext;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class ServerHelper {

    private PluginContext pluginContext = null;
    private PluginLogHandler logger = null;
    private CMDataManager cmDataManager = null;

    public ServerHelper(PluginContext pc, PluginLogHandler pluginLog) {
	pluginContext = pc;
	logger = pluginLog;
	cmDataManager = new CMDataManager(pluginContext, logger);
    }

}
