package com.cloudmunch.javasdk.plugin;

import java.net.URLEncoder;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cloudmunch.javasdk.CMDataManager;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class EnvironmentHelper {

    private PluginContext pluginContext = null;
    private CMDataManager cmDataManager = null;
    private PluginLogHandler pluginLogHandler = null;
    private RoleHelper roleHelper = null;
    private String defaultRole = "default";

    /**
     * This is a helper class for environments. User can manage environments in
     * cloudmunch using this helper
     * 
     * @param appContext
     * @param logHandler
     */
    public EnvironmentHelper(PluginContext appContext,
	    PluginLogHandler logHandler) {

	this.pluginContext = appContext;
	this.pluginLogHandler = logHandler;
	this.cmDataManager = new CMDataManager(pluginContext, pluginLogHandler);
	this.roleHelper = new RoleHelper(pluginContext, pluginLogHandler);

    }

    /**
     * Gets the list of existing environments for the given application.
     *
     * @param filter
     *            the filter
     * @return the existing environments
     */
    public JSONArray getExistingEnvironmentsList(JSONObject filter) {

	JSONArray environments = new JSONArray();

	String queryString = "";
	if (filter != null && filter.length() > 0) {
	    queryString = "filter=" + filter.toString();
	}

	String serverURL = pluginContext.getMasterUrl() + "/applications/"
		+ pluginContext.getApplication() + "/environments";
	try {
	    String response = cmDataManager.getDataForContext(serverURL,
		    pluginContext.getApiKey(), queryString);

	    JSONObject respObj = new JSONObject(response);
	    if (respObj.has("data")) {
		environments = respObj.getJSONArray("data");
	    }

	} catch (Exception e) {
	    pluginLogHandler.logHandler("ERROR",
		    "Could not retreive data from cloudmunch , exception occurred - "
			    + e.getMessage());
	    e.printStackTrace();
	    return null;
	}
	return environments;
    }

    /**
     * Gets the details of given Environment.
     *
     * @param environmentID
     *            the environment id
     * @param filter
     *            the filter
     * @return the environment details
     */
    public JSONObject getEnvironmentDetails(String environmentID,
	    JSONObject filter) {
	JSONObject envDetails = new JSONObject();
	String queryString = "";
	if (filter != null && filter.length() > 0) {
	    queryString = "filter=" + filter.toString();
	}

	String serverURL = pluginContext.getMasterUrl() + "/applications/"
		+ pluginContext.getApplication() + "/environments/"
		+ environmentID;
	try {
	    String response = cmDataManager.getDataForContext(serverURL,
		    pluginContext.getApiKey(), queryString);

	    JSONObject respObj = new JSONObject(response);
	    if (respObj.has("data")) {
		envDetails = respObj.getJSONObject("data");
	    }

	} catch (Exception e) {
	    pluginLogHandler.logHandler("ERROR",
		    "Could not retreive data from cloudmunch for environment "
			    + environmentID
			    + " , exception occurred - "
			    + e.getMessage());
	    e.printStackTrace();
	    return null;
	}

	return envDetails;

    }

    public void addEnvironment(String environmentName,
	    String environmentStatus, JSONObject environmentData) {
	if (environmentName.trim().isEmpty()
		|| environmentStatus.trim().isEmpty()) {
	    pluginLogHandler.logHandler("ERROR",
		    "Environment name and status need to be provided");
	    return;
	}

	String[] statuscon = { "STATUS_CREATION_IN_PROGRESS",
		"STATUS_RUNNING",
		"STATUS_STOPPED",
		"STATUS_STOPPED_WITH_ERRORS",
		"STATUS_RUNNING_WITH_WARNINGS",
		"STATUS_ACTION_IN_PROGRESS " };

	if (!ArrayUtils.contains(statuscon, environmentStatus)) {
	    pluginLogHandler.logHandler("ERROR",
		    "Invalid status provided, valid values are "
			    + "STATUS_CREATION_IN_PROGRESS" + ", "
			    + "STATUS_RUNNING" + ", "
			    + "STATUS_STOPPED" + ", "
			    + "STATUS_ACTION_IN_PROGRESS" + ", "
			    + "STATUS_RUNNING_WITH_WARNINGS" + " and "
			    + "STATUS_STOPPED_WITH_ERRORS");
	    return;
	}

	environmentData.put("name", environmentName);
	environmentData.put("status", environmentStatus);

	String comment = "Adding environment with name " + environmentName;

	String serverUrl = pluginContext.getMasterUrl() + "/applications/"
		+ pluginContext.getApplication() + "/environments";

	String response = cmDataManager.putDataForContext(serverUrl,
		pluginContext.getApiKey(), environmentData, comment);
	pluginLogHandler.logHandler("debug", "Response " + response);

	JSONObject envDetails = new JSONObject();

	try {
	    JSONObject respObj = new JSONObject(response);
	    if (respObj.has("data")) {
		envDetails = respObj.getJSONObject("data");
		this.pluginLogHandler.logHandler("DEBUG",
			"Environment ID Created is "
				+ envDetails.get("id").toString());
	    }
	} catch (Exception e) {

	    this.pluginLogHandler.logHandler("ERROR",
		    "Unable to create environment, response from server is "
			    + response);
	}

    }

    public void updateEnvironment(String envId, JSONObject environmentData,
	    String comment) {
	String serverUrl = pluginContext.getMasterUrl() + "/applications/"
		+ pluginContext.getApplication() + "/environments/" + envId;

	cmDataManager.putDataForContext(serverUrl,
		pluginContext.getApiKey(), environmentData,
		"updating data for environment " + envId);

    }

    public void updateEnvironmentURL(String envId, String environmentURL) {
	if (environmentURL == null || environmentURL.trim().isEmpty()) {
	    pluginLogHandler
		    .logHandler("ERROR",
			    "Environment URL is not provided to update environment details");
	    return;
	}

	String comment = "Setting application URL";
	JSONObject data = new JSONObject();
	data.put("application_url", environmentURL);
	updateEnvironment(envId, data, comment);
    }
}
