package com.cloudmunch.javasdk.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class CBDataReader {

	public static JSONObject readAppDefinition(String url) throws JSONException {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(url);
		urlGenerator.setFileName("cbdata.php");
		urlGenerator.addArgument("context", "appdefinition");
		urlGenerator.addArgument("username", "CI");
//		System.out.println(urlGenerator.toString());
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.getContent();
		
		try {
			return new JSONObject(str);
		} catch (Exception e) {
			return new JSONObject();
		}
	}
	
	public static JSONArray readDeployServer(String url) throws JSONException {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(url);
		urlGenerator.setFileName("cbdata.php");
		urlGenerator.addArgument("context", "server");
		urlGenerator.addArgument("username", "CI");
//		System.out.println(urlGenerator.toString());
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.getContent();

		try {
			return new JSONArray(str);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	public static JSONArray readDeployServer(String url,String domain) throws JSONException {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(url);
		urlGenerator.setFileName("cbdata.php");
		urlGenerator.addArgument("context", "server");
		urlGenerator.addArgument("username", "CI");
		urlGenerator.addArgument("domain", domain);
		System.out.println(urlGenerator.toString());
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.getContent();

		try {
			return new JSONArray(str);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
	public static JSONArray updateDeployServer(String url,String domain,String content) throws JSONException {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(url);
		urlGenerator.setFileName("cbdata.php");
		urlGenerator.addArgument("context", "server");
		urlGenerator.addArgument("username", "CI");
		urlGenerator.addArgument("domain", domain);
		System.out.println(urlGenerator.toString());
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.postContent(content);

		try {
			return new JSONArray(str);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
}
