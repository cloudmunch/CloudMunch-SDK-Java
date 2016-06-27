package com.cloudmunch.javasdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.javasdk.plugin.PluginContext;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;

public class CMDataManager {

    private static final String UTF_ENCODE = "UTF-8";
    PluginContext pluginContext = null;
    PluginLogHandler logHandler = null;
    private final String USER_AGENT = "Mozilla/5.0";

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
	String response = do_curl_call(url, new ArrayList<NameValuePair>(), "GET");
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
	    } else {
		response = getJsonValue(respObj, "data");
	    }
	} catch (JSONException e) {
	    return response;
	}

	return response;

    }

    public String updateDataForContext(String url, String apiKey,
	    JSONObject urlParameters, String comment) {
	urlParameters.put("application_id", pluginContext.getApplication());
	urlParameters.put("pipeline_id", pluginContext.getPipeline());
	urlParameters.put("domain", pluginContext.getDomain());
	urlParameters.put("run_id", pluginContext.getRunNumber());

	url += "?apikey=" + pluginContext.getApiKey();

	List<NameValuePair> params = new ArrayList<NameValuePair>();
	String encodedData = urlParameters.toString();
	try {
	    encodedData = URLEncoder.encode(urlParameters.toString(), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	params.add(new BasicNameValuePair("data", encodedData));
	params.add(new BasicNameValuePair("comment", comment.trim()));

	String response = "";

	try {
	    response = do_curl_call(url, params, "PATCH");
	} catch (Exception e) {
	    e.printStackTrace();
	}
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

    // // // default data to be updated for all updates
    // // $dat=array("data"=>$this->json_object($data));
    // // if (!is_null($comment) && strlen($comment) > 0) {
    // // $dat[comment] = $comment;
    // // }
    // //
    // // $dat=$this->json_string($this->json_object($dat));
    // //
    // // //echo "data : " . $dat;
    // // $url=$url."?apikey=".$apikey;
    // // $result=$this->do_curl($url, null, "PATCH", $dat, null);
    // //
    // // $result=$result["response"];
    // // $result=json_decode($result);
    // //
    // // if(($result==null) ||($result->request->status !== "SUCCESS")){
    // // $this->logHelper->log(ERROR, $result->request->message);
    // // $this->logHelper->log (ERROR,"Not able to patch data to cloudmunch");
    // // return false;
    // // }
    // //
    // return $result;
    // }

    public String putDataForContext(String url, String apiKey,
	    JSONObject urlParameters, String comment) {

	urlParameters.put("application_id", pluginContext.getApplication());
	urlParameters.put("pipeline_id", pluginContext.getPipeline());
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

    public void notifyUsersInCloudmunch(String serverurl, String message,
	    JSONObject contextarray, String domain)
	    throws Exception {

	String url = serverurl + "/cbdata.php"
		+ "?"
		+ "action=NOTIFY"
		+ "&"
		+ "to=*"
		+ "&"
		+ "message=" + URLEncoder.encode(message, "UTF-8")
		+ "&"
		+ "domain=" + domain
		+ "&"
		+ "username=CI";

	String userContext = "usercontext="
		+ URLEncoder.encode(contextarray.toString(), "UTF-8");

	StringBuffer completeString = new StringBuffer();

	URL _url = new URL(url);
	URLConnection conn = _url.openConnection();
	conn.setDoOutput(true);
	OutputStreamWriter wr = new OutputStreamWriter(
		conn.getOutputStream());
	wr.write(userContext);
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

	try {

	    JSONObject respObject = new JSONObject(completeString.toString());
	    String responseStatus = getJsonValue(respObject, "request->status");
	    if (!responseStatus.equalsIgnoreCase("SUCCESS")) {
		String respMessage = getJsonValue(respObject,
			"request->message");
		this.logHandler.logHandler("error",
			"Failed the notification to server : " + respMessage);
	    }
	} catch (JSONException e) {
	    this.logHandler.logHandler("error", e.getMessage());
	}

    }

    // HTTP GET request
    private String sendGet(String url) throws Exception {

	CloseableHttpClient client = HttpClients.createDefault();
	HttpGet request = new HttpGet(url);

	// add request header
	request.addHeader("User-Agent", USER_AGENT);

	HttpResponse response = client.execute(request);

	System.out.println("\nSending 'GET' request to URL : " + url);
	System.out.println("Response Code : " +
		response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
	    result.append(line);
	}

	return result.toString();

    }

    private String sendPatch(String url, List<NameValuePair> urlParameters)
	    throws Exception {
	CloseableHttpClient client = HttpClients.createDefault();
	HttpPatch patch = new HttpPatch(url);
	// add header
	patch.setHeader("User-Agent", USER_AGENT);

	patch.setEntity(new UrlEncodedFormEntity(urlParameters));

	HttpResponse response = client.execute(patch);
	System.out.println("\nSending 'PATCH' request to URL : " + url);
	System.out.println("Post parameters : " + patch.getEntity());
	System.out.println("Response Code : " +
		response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
	    result.append(line);
	}

	return result.toString();
    }

    // HTTP POST request
    private String sendPost(String url, List<NameValuePair> urlParameters)
	    throws Exception {

	CloseableHttpClient client = HttpClients.createDefault();
	HttpPost post = new HttpPost(url);

	// add header
	post.setHeader("User-Agent", USER_AGENT);

	post.setEntity(new UrlEncodedFormEntity(urlParameters));

	HttpResponse response = client.execute(post);
	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + post.getEntity());
	System.out.println("Response Code : " +
		response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
	    result.append(line);
	}

	return result.toString();

    }

    public String do_curl_call(String url, List<NameValuePair> urlParams,
	    String reqType) throws Exception {

	if (reqType.equalsIgnoreCase("GET")) {
	    return sendGet(url);
	} else if (reqType.equalsIgnoreCase("POST")) {
	    return sendPost(url, urlParams);
	} else if (reqType.equalsIgnoreCase("PATCH")) {
	    return sendPatch(url, urlParams);
	}
	return "";
    }

}
