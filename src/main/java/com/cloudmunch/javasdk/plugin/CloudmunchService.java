package com.cloudmunch.javasdk.plugin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.javasdk.CMDataManager;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class CloudmunchService {

    private PluginContext pluginContext = null;
    private CMDataManager cmDataManager = null;
    private PluginLogHandler pluginLogHandler = null;

    public CloudmunchService(PluginContext context, PluginLogHandler logger) {
	this.pluginContext = context;
	this.pluginLogHandler = logger;
	cmDataManager = new CMDataManager(context, pluginLogHandler);
    }

    /**
     * This method is to invoke notification on cloudmunch.
     * 
     * @param message
     * @param context
     * @param id
     * @throws Exception
     */
    public void notifyUsers(String message, String context, String id)
	    throws Exception {
	JSONObject dataObj = new JSONObject();
	dataObj.put("project", this.pluginContext.getApplication());
	dataObj.put("job", this.pluginContext.getPipeline());
	dataObj.put("context", context);
	dataObj.put("id", id);
	this.cmDataManager.notifyUsersInCloudmunch(
		this.pluginContext.getMasterUrl(), message, dataObj,
		this.pluginContext.getDomain());

    }

    /**
     * @param contextsArray
     *            - associative array with key as context and its id as value
     * @param queryParams
     * @return
     */
    public JSONObject getCustomContextData(JSONObject contextsArray,
	    JSONObject queryParams) {
	String queryString = "";
	String serverUrl = this.pluginContext.getMasterUrl()
		+ "/applications/" + this.pluginContext.getApplication();
	if (contextsArray.length() > 0) {
	    String[] contextsKeys = JSONObject.getNames(contextsArray);
	    for (String contextKey : contextsKeys) {
		String contextValue = contextsArray.getString(contextKey);
		if (!contextValue.trim().isEmpty()) {
		    serverUrl = serverUrl + "/" + contextKey + "/"
			    + contextValue;
		} else {
		    serverUrl = serverUrl + "/" + contextKey;
		    break;
		}
	    }
	    if (null != queryParams && queryParams.length() > 0) {
		String[] queryKeys = JSONObject.getNames(queryParams);
		for (String queryKey : queryKeys) {
		    String value = queryParams.get(queryKey).toString();
		    if (queryKey.equalsIgnoreCase("filter")) {
			try {
			    value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		    if (!queryString.trim().isEmpty()) {
			queryString = queryKey + "=" + value + "&"
				+ queryString;
		    } else {
			queryString = queryKey + "=" + value;
		    }
		}
	    }
	} else {
	    this.pluginLogHandler
		    .logHandler("ERROR",
			    "First parameter is expected to be an object with key value pairs");
	}

	JSONObject respObj = new JSONObject();
	try {
	    String response = this.cmDataManager.getDataForContext(
		    serverUrl,
		    this.pluginContext.getApiKey(), queryString);
	    respObj = new JSONObject(response);
	} catch (Exception e) {
	    this.pluginLogHandler.logHandler(
		    "ERROR",
		    "Could not retrieve data from cloudmunch. "
			    + e.getMessage());
	}
	return respObj;
    }

}
