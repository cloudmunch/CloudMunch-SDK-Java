package com.cloudmunch.javasdk.plugin;

import org.json.JSONObject;

import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class IntegrationHelper {

     private PluginLogHandler pluginLogger = null;
    
     public IntegrationHelper(PluginLogHandler log) {
     pluginLogger = log;
     }

    public JSONObject
	    getIntegration(JSONObject parameters, JSONObject integrations) {
	String providernameArgName = "providername";
	String providerName = parameters.getString(providernameArgName);

	pluginLogger.logHandler("DEBUG", "Provider Name: " + providerName);

	if (null != providerName && providerName.trim()
		.length() > 0) {
	    String confKey = "configuration";
	    JSONObject providerDetails = integrations.getJSONObject(
		    providerName).getJSONObject(confKey);
	    return providerDetails;
	}

	return null;

    }

}
