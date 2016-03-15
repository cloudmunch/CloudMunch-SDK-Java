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
	String providerName = null;

	if (parameters.has(providernameArgName)) {
	    providerName = parameters.getString(providernameArgName);
	}

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

    public JSONObject getService(JSONObject parameters) {

	String argName = "cloudproviders";
	String cloudProvidersString = null;
	if (parameters.has(argName)) {
	    cloudProvidersString = parameters.get(argName).toString();
	}

	JSONObject cloudProviders = new JSONObject(cloudProvidersString);

	String providernameArgName = "providername";
	String providerName = null;

	if (parameters.has(providernameArgName)) {
	    providerName = parameters.getString(providernameArgName);
	}

	pluginLogger.logHandler("DEBUG", "Provider Name: " + providerName);

	if (null != providerName && providerName.trim()
		.length() > 0) {
	    JSONObject cloudProviderDetails = cloudProviders
		    .getJSONObject(providerName);
	    return cloudProviderDetails;
	}

	return null;
    }
}
