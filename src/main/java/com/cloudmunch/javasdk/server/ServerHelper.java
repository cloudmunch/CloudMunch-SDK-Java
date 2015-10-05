package com.cloudmunch.javasdk.server;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloudmunch.javasdk.datamanager.CBDataFinder;
import com.cloudmunch.javasdk.datamanager.CBDataReader;
import com.cloudmunch.javasdk.util.PluginContext;


public class ServerHelper {
	private PluginContext plugincontext;
	
	ServerHelper(PluginContext pc){
		plugincontext=pc;
	}
	
public Server getServer(String servername){
	JSONArray deployServer=CBDataReader.readDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain());
	 JSONObject serverDetail = CBDataFinder.findFromDeployServer(deployServer, servername);
	 Server server=new Server();
	 server.setAppName(serverDetail.getString("appName"));
	 server.setAssetname(serverDetail.getString("assetname"));
	 server.setAssettype(serverDetail.getString("assettype"));
	 server.setBuild(serverDetail.getString("build"));
	 server.setBuildLocation(serverDetail.getString("buildLoc"));
	 server.setCI(serverDetail.getString("CI"));
	 server.setCmserver(serverDetail.getString("cmserver"));
	 server.setDeploymentStatus(serverDetail.getString("deploymentStatus"));
	 server.setDeployTempLoc(serverDetail.getString("deployTempLoc"));
	 server.setDescription(serverDetail.getString("description"));
	 server.setDns(serverDetail.getString("dnsName"));
	 server.setDomainName(serverDetail.getString("domainName"));
	 
	 server.setImageID(serverDetail.getString("amiID"));
	 server.setInstanceId(serverDetail.getString("instanceId"));
	 server.setInstancesize(serverDetail.getString("instancesize"));
	 server.setLauncheduser(serverDetail.getString("username"));
	 server.setLoginUser(serverDetail.getString("loginUser"));
	 server.setPassword(serverDetail.getString("password"));
	 server.setPrivateKeyLoc(serverDetail.getString("privateKeyLoc"));
	 server.setProvider(serverDetail.getString("provider"));
	 server.setPublicKeyLoc(serverDetail.getString("publicKeyLoc"));
	 server.setRegion(serverDetail.getString("region"));
	 server.setServername(serverDetail.getString(servername));
	 server.setServerType(serverDetail.getString("serverType"));
	 server.setSshport(serverDetail.getString("sshport"));
	 server.setStarttime(serverDetail.getString("starttime"));
	 server.setStatus(serverDetail.getString("status"));
	 server.setTier(serverDetail.getString("tier"));
	 return server;
}

public void  addServer(Server newserver,boolean $docker ){
	JSONArray deployServer=CBDataReader.readDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain());
	
	JSONObject serverObj=new JSONObject();
	serverObj.append("description", newserver.getDescription());
	serverObj.append("dnsName", newserver.getDns());
	serverObj.append("domainName", newserver.getDomainName());
	serverObj.append("emailID", newserver.getEmailID());
	serverObj.append("CI", newserver.getCI());
	serverObj.append("deploymentStatus", newserver.getDeploymentStatus());
	serverObj.append("description", newserver.getDescription());
	serverObj.append("instanceId", newserver.getInstanceId());
	serverObj.append("amiID", newserver.getImageID());
	serverObj.append("username", newserver.getEmailID());
	serverObj.append("build", newserver.getBuild());
	serverObj.append("appName", newserver.getAppName());
	serverObj.append("deployTempLoc", newserver.getDeployTempLoc());
	serverObj.append("buildLoc", newserver.getBuildLocation());
	serverObj.append("privateKeyLoc", newserver.getPrivateKeyLoc());
	serverObj.append("publicKeyLoc", newserver.getPublicKeyLoc());
	serverObj.append("loginUser", newserver.getLoginUser());
	serverObj.append("serverType", newserver.getServerType());
	serverObj.append("assettype", newserver.getAssettype());
	serverObj.append("status", newserver.getStatus());
	serverObj.append("starttime", newserver.getStarttime());
	serverObj.append("provider", newserver.getProvider());
	serverObj.append("region", newserver.getRegion());
	serverObj.append("cmserver", newserver.getCmserver());
	serverObj.append("assetname", newserver.getAssetname());
	serverObj.append("instancesize", newserver.getInstancesize());
	serverObj.append("password", newserver.getPassword());
	serverObj.append("sshport", newserver.getSshport());
	serverObj.append("password", newserver.getPassword());
	serverObj.append("sshport", newserver.getSshport());
	serverObj.append("tier", newserver.getTier());
	JSONObject mobj = new JSONObject();
	mobj.put(newserver.getServername(),serverObj);
	deployServer.put(mobj);
	String data=deployServer.toString();
	CBDataReader.updateDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain(),data);

	
}
public void updateServer(Server newserver,boolean $docker ){
	JSONArray deployServer=CBDataReader.readDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain());
	for(int i=0;i<=deployServer.length();i++ ){
		JSONObject js=(JSONObject)deployServer.get(i);
		if(js.has(newserver.getServername())){
			deployServer.remove(i);
		}
	}
	
	
	JSONObject serverObj=new JSONObject();
	serverObj.append("description", newserver.getDescription());
	serverObj.append("dnsName", newserver.getDns());
	serverObj.append("domainName", newserver.getDomainName());
	serverObj.append("emailID", newserver.getEmailID());
	serverObj.append("CI", newserver.getCI());
	serverObj.append("deploymentStatus", newserver.getDeploymentStatus());
	serverObj.append("description", newserver.getDescription());
	serverObj.append("instanceId", newserver.getInstanceId());
	serverObj.append("amiID", newserver.getImageID());
	serverObj.append("username", newserver.getEmailID());
	serverObj.append("build", newserver.getBuild());
	serverObj.append("appName", newserver.getAppName());
	serverObj.append("deployTempLoc", newserver.getDeployTempLoc());
	serverObj.append("buildLoc", newserver.getBuildLocation());
	serverObj.append("privateKeyLoc", newserver.getPrivateKeyLoc());
	serverObj.append("publicKeyLoc", newserver.getPublicKeyLoc());
	serverObj.append("loginUser", newserver.getLoginUser());
	serverObj.append("serverType", newserver.getServerType());
	serverObj.append("assettype", newserver.getAssettype());
	serverObj.append("status", newserver.getStatus());
	serverObj.append("starttime", newserver.getStarttime());
	serverObj.append("provider", newserver.getProvider());
	serverObj.append("region", newserver.getRegion());
	serverObj.append("cmserver", newserver.getCmserver());
	serverObj.append("assetname", newserver.getAssetname());
	serverObj.append("instancesize", newserver.getInstancesize());
	serverObj.append("password", newserver.getPassword());
	serverObj.append("sshport", newserver.getSshport());
	serverObj.append("password", newserver.getPassword());
	serverObj.append("sshport", newserver.getSshport());
	serverObj.append("tier", newserver.getTier());
	JSONObject mobj = new JSONObject();
	mobj.put(newserver.getServername(),serverObj);
	deployServer.put(mobj);
	String data=deployServer.toString();
	CBDataReader.updateDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain(),data);

	
}

public void  deleteServer(String serverName){
	JSONArray deployServer=CBDataReader.readDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain());
	for(int i=0;i<=deployServer.length();i++ ){
		JSONObject js=(JSONObject)deployServer.get(i);
		if(js.has(serverName)){
			deployServer.remove(i);
		}
	}
	String data=deployServer.toString();
	CBDataReader.updateDeployServer(plugincontext.getMasterurl(),plugincontext.getDomain(),data);
	
}

}
