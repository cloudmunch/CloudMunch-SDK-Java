package com.cloudmunch.javasdk.util;

public class PluginContext {
	
	private String masterurl;
	
	private String cloudproviders;
	
	public String getCloudproviders() {
		return cloudproviders;
	}

	public void setCloudproviders(String cloudproviders) {
		this.cloudproviders = cloudproviders;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	private String domain;
	
	private String projectName;
	
	private String jobname;
	
	

	public String getMasterurl() {
		return masterurl;
	}

	public void setMasterurl(String masterurl) {
		this.masterurl = masterurl;
	}

}
