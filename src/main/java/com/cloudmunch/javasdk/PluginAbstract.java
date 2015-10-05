package com.cloudmunch.javasdk;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.cloudmunch.javasdk.arguments.ArgumentParser;
import com.cloudmunch.javasdk.util.PluginContext;


public abstract class PluginAbstract {
	
	public static JSONObject inputParams = new JSONObject();
	public static JSONObject intDetails = new JSONObject();
	
	public Date startdate=null;
	
	public PluginContext pc=null;
	
	public abstract void  process();
	 
	 public void initialize(String[] args){
		 startdate=new Date(System.currentTimeMillis());
		 System.out.println(startdate);
		processInput(args);
		
		 
	 }
	 public void processInput(String[] args){
		 ArgumentParser.parseArguments(args);
		 inputParams= ArgumentParser.inputParams;
		  pc=new PluginContext();
		  if(inputParams.has("masterurl")){
		 pc.setMasterurl(inputParams.getString("masterurl"));
		  }
		  if(inputParams.has("masterurl")){
		 pc.setCloudproviders(inputParams.getString("cloudproviders"));
		  }
		 pc.setDomain(inputParams.getString("domain"));
		 pc.setProjectName(inputParams.getString("projectName"));
		 pc.setJobname(inputParams.getString("jobname"));
		 JSONObject js =new JSONObject(inputParams.getString("cloudproviders"));
		 intDetails=(JSONObject)js.get(inputParams.getString("providername"));
		 
		 
		 
		 
		 
		 
	 }
	 
	 
	


}
