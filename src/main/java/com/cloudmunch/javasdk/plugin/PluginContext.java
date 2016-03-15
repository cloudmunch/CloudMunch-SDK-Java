package com.cloudmunch.javasdk.plugin;

public class PluginContext {

    private String masterUrl;
    private String domain;
    private String application;
    private String ciJobName;
    private String workspace;
    private String stepID;
    private String pipeline;
    private String stepName;
    private String stepReportsLocation;
    private String archiveLocation;
    private String targetServer;
    private String cloudProviders;
    private String runNumber;
    private String apiKey;

    /**
     * @return the masterUrl
     */
    public String getMasterUrl() {
	return masterUrl;
    }

    /**
     * @param masterUrl
     *            the masterUrl to set
     */
    public void setMasterUrl(String masterUrl) {
	this.masterUrl = masterUrl;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
	return domain;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {
	this.domain = domain;
    }

    /**
     * @return the application
     */
    public String getApplication() {
	return application;
    }

    /**
     * @param application
     *            the application to set
     */
    public void setApplication(String application) {
	this.application = application;
    }

    /**
     * @return the ciJobName
     */
    public String getCiJobName() {
	return ciJobName;
    }

    /**
     * @param ciJobName
     *            the ciJobName to set
     */
    public void setCiJobName(String ciJobName) {
	this.ciJobName = ciJobName;
    }

    /**
     * @return the workspace
     */
    public String getWorkspace() {
	return workspace;
    }

    /**
     * @param workspace
     *            the workspace to set
     */
    public void setWorkspace(String workspace) {
	this.workspace = workspace;
    }

    /**
     * @return the stepID
     */
    public String getStepID() {
	return stepID;
    }

    /**
     * @param stepID
     *            the stepID to set
     */
    public void setStepID(String stepID) {
	this.stepID = stepID;
    }

    /**
     * @return the stepName
     */
    public String getStepName() {
	return stepName;
    }

    /**
     * @param stepName
     *            the stepName to set
     */
    public void setStepName(String stepName) {
	this.stepName = stepName;
    }

    /**
     * @return the stepReportsLocation
     */
    public String getStepReportsLocation() {
	return stepReportsLocation;
    }

    /**
     * @param stepReportsLocation
     *            the stepReportsLocation to set
     */
    public void setStepReportsLocation(String stepReportsLocation) {
	this.stepReportsLocation = stepReportsLocation;
    }

    /**
     * @return the archiveLocation
     */
    public String getArchiveLocation() {
	return archiveLocation;
    }

    /**
     * @param archiveLocation
     *            the archiveLocation to set
     */
    public void setArchiveLocation(String archiveLocation) {
	this.archiveLocation = archiveLocation;
    }

    /**
     * @return the targetServer
     */
    public String getTargetServer() {
	return targetServer;
    }

    /**
     * @param targetServer
     *            the targetServer to set
     */
    public void setTargetServer(String targetServer) {
	this.targetServer = targetServer;
    }

    /**
     * @return the cloudProviders
     */
    public String getCloudProviders() {
	return cloudProviders;
    }

    /**
     * @param cloudProviders
     *            the cloudProviders to set
     */
    public void setCloudProviders(String cloudProviders) {
	this.cloudProviders = cloudProviders;
    }

    /**
     * @return the pipeline
     */
    public String getPipeline() {
	return pipeline;
    }

    /**
     * @param pipeline
     *            the pipeline to set
     */
    public void setPipeline(String pipeline) {
	this.pipeline = pipeline;
    }

    /**
     * @return the runNumber
     */
    public String getRunNumber() {
	return runNumber;
    }

    /**
     * @param runNumber
     *            the runNumber to set
     */
    public void setRunNumber(String runNumber) {
	this.runNumber = runNumber;
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
	return apiKey;
    }

    /**
     * @param apiKey
     *            the apiKey to set
     */
    public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
    }

}
