package com.cloudmunch.javasdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.javasdk.plugin.PluginContext;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class CMDataManager {

    private static final String UTF_ENCODE = "UTF-8";
    PluginContext pluginContext = null;
    PluginLogHandler logHandler = null;

    public CMDataManager(PluginContext pc, PluginLogHandler logger) {
	this.pluginContext = pc;
	this.logHandler = logger;
    }

    public String getDataForContext(String url, String apiKey,
	    String queryString) throws Exception {

	if (queryString.trim().isEmpty()) {
	    url = url.concat("?apikey=").concat(
		    URLEncoder.encode(apiKey, UTF_ENCODE));
	} else {
	    url = url.concat("?apikey=")
		    .concat(URLEncoder.encode(apiKey, UTF_ENCODE)).concat("&")
		    .concat(URLEncoder.encode(queryString, UTF_ENCODE));
	}
	String response = doCurl(url);
	this.logHandler.logHandler("INFO", "Response of the curl to " + url
		+ " is " + response);
	try {
	    JSONObject respObj = new JSONObject(response);
	    String responseStatus = getJsonValue(respObj, "request->status");
	    if (!responseStatus.equalsIgnoreCase("SUCCESS")) {
		String message = getJsonValue(respObj, "request->message");
		this.logHandler.logHandler("DEBUG",
			message);
		return message;
	    }
	} catch (JSONException e) {
	    return response;
	}

	return response;

    }

    public String putDataForContext(String url, String apiKey,
	    JSONObject urlParameters, String comment) {

	urlParameters.put("application", pluginContext.getApplication());
	urlParameters.put("job", pluginContext.getPipeline());
	urlParameters.put("domain", pluginContext.getDomain());
	urlParameters.put("run_id", pluginContext.getRunNumber());

	url += "?apikey=" + pluginContext.getApiKey();

	StringBuffer completeString = new StringBuffer();
	this.logHandler.logHandler("info", urlParameters.toString());
	try {

	    String data = URLEncoder.encode("data",
		    UTF_ENCODE)
		    + "="
		    + URLEncoder.encode(urlParameters.toString(), "UTF-8");
	    data += "&"
		    + URLEncoder.encode("apikey", "UTF-8")
		    + "="
		    + URLEncoder.encode(pluginContext.getApiKey(),
			    "UTF-8");
	    data += "&"
		    + URLEncoder.encode("comment", "UTF-8")
		    + "="
		    + URLEncoder.encode(comment,
			    "UTF-8");

	    // String data = URLEncoder.encode("data", "UTF-8")
	    // + URLEncoder.encode(urlParameters.toString(), "UTF-8");

	    URL _url = new URL(url);
	    URLConnection conn = _url.openConnection();
	    conn.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(
		    conn.getOutputStream());
	    wr.write(data);
	    wr.flush();

	    // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(
		    conn.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
		// Process line...
		completeString.append(line);
	    }
	    wr.close();
	    rd.close();
	} catch (Exception e) {
	    this.logHandler.logHandler("debug",
		    "Posting to server " + url + " with parameters "
			    + urlParameters + " failed : " + e.getMessage());

	}

	try {

	    JSONObject respObject = new JSONObject(completeString.toString());
	    String responseStatus = getJsonValue(respObject, "request->status");
	    if (!responseStatus.equalsIgnoreCase("SUCCESS")) {
		String message = getJsonValue(respObject, "request->message");
		return message;
	    }
	} catch (JSONException e) {
	    return completeString.toString();
	}

	return completeString.toString();
    }

    public static String getJsonValue(JSONObject object, String nodeList)
	    throws JSONException {

	String[] keys = StringUtils.split(nodeList, "->");

	System.out.println(" Splits " + ArrayUtils.toString(keys));

	String result = null;

	JSONObject localData = new JSONObject(object.toString());
	boolean found = false;

	for (int i = 0; i < keys.length; i++) {
	    Object sub = null;
	    String string = keys[i];
	    while (localData.has(string) && !found) {
		sub = localData.get(string);
		if (sub instanceof JSONObject) {
		    localData = new JSONObject(sub.toString());
		    if (i == keys.length - 1)
			result = localData.toString();
		} else if (sub instanceof String) {
		    result = sub.toString();
		    found = true;
		}
	    }

	}
	return result;

    }

    /**
     * Check this method implementation again
     * 
     * @param urlString
     * @return
     */
    public String doCurl(String urlString) {
	String responseString = null;
	try {
	    URL url = new URL(urlString);
	    URLConnection connection = url.openConnection();

	    connection.setRequestProperty("Accept-Charset",
		    "UTF-8");
	    InputStream responseStream = connection.getInputStream();
	    responseString = IOUtils.toString(responseStream);
	    responseStream.close();

	} catch (Exception e) {

	}
	return responseString;
    }
}
