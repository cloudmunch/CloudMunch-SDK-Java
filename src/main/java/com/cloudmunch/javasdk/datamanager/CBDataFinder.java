package com.cloudmunch.javasdk.datamanager;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CBDataFinder {

	public static JSONObject findFromDeployServer(JSONArray deployServer, String string) throws JSONException {
		// System.out.println("Server to find : " + string);
		for (int i = 0; i < deployServer.length(); i++) {
			JSONObject json = deployServer.getJSONObject(i);
			String[] s = JSONObject.getNames(json);
			for (int j = 0; j < s.length; j++) {
				// System.out.println("Server Found is : " + s[j]);
				if (s[j].equalsIgnoreCase(string)) {
					return json.getJSONObject(s[j]);
				}
			}
		}
		return null;
	}

	public static List<String> getListOfServersFromDeployJSon(JSONArray deployServer) throws JSONException {
		List<String> retValue = new ArrayList<String>();
		for (int i = 0; i < deployServer.length(); i++) {
			JSONObject json = deployServer.getJSONObject(i);
			String[] s = JSONObject.getNames(json);
			for (int j = 0; j < s.length; j++) {
				retValue.add(s[j]);
			}
		}
		return retValue;
	}

	/*public static void main(String[] args) throws IOException, JSONException {
		String s = FileUtils.readFileToString(new File("E:/temp/CBDemo/jcdeployserver.json"));
		JSONArray j = new JSONArray(s);
		System.out.println(findFromDeployServer(j, "API Test"));
	}*/

	public enum DATAFINDER {
		APP, CONFIGURATION
	}

	public static void findConfigurationFromAppDefinition(JSONObject appDefinition, DATAFINDER df) {
		
	}
}
