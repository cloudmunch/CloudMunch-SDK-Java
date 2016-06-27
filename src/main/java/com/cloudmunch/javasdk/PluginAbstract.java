package com.cloudmunch.javasdk;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import com.cloudmunch.argumentparser.ArgumentParser;
import com.cloudmunch.javasdk.plugin.CloudmunchService;
import com.cloudmunch.javasdk.plugin.IntegrationHelper;
import com.cloudmunch.javasdk.plugin.PluginContext;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler;
import com.cloudmunch.javasdk.plugin.logger.PluginLogHandler.LogModes;

/**
 * The Class PluginAbstract.
 * 
 * @author Leela
 * 
 *         An abstract base class for Cloudmunch Plugin Object, providing
 *         methods to read parameters, create plugin context object
 */
public abstract class PluginAbstract {

    private JSONObject inputParams = new JSONObject();
    private JSONObject variables = new JSONObject();
    private JSONObject integrations = new JSONObject();
    private JSONObject integrationDetails = new JSONObject();
    public PluginContext pc = null;
    public PluginLogHandler pluginLog = null;
    public Date startdate = null;
    public Date endDate = null;
    private CloudmunchService cloudmunchService = null;

    /**
     * This is an abstract method to be implemented by every plugin.
     */
    public abstract void process();

    /**
     * Initialize the plugin execution by processing the all inputs.
     *
     * @param args
     *            the args
     */
    public void initialize(String[] args) {
	pc = new PluginContext();
	createLogHandler();
	// pluginLog.logHandler(LogModes.INFO.toString(),
	// "App execution started");
	startdate = new Date(System.currentTimeMillis());
	getInputs(args);
    }

    /**
     * 
     * This method read and process the input parameters.
     * 
     * @param args
     *            the args
     */
    private void getInputs(String[] args) {

	ArgumentParser.parseArguments(args, pluginLog);
	inputParams = ArgumentParser.inputParams;
	variables = ArgumentParser.variables;
	integrations = ArgumentParser.integrations;
	pluginLog.logHandler(LogModes.DEBUG.toString(),
		"Parameters are " + inputParams);
	pluginLog.logHandler(LogModes.DEBUG.toString(),
		"Integrations are " + integrations);
	pluginLog.logHandler(LogModes.DEBUG.toString(),
		"Variables are " + variables);

	if (inputParams.length() == 0) {
	    pluginLog.logHandler(LogModes.WARN.toString(),
		    "Input parameters for this execution are empty");
	}
	if (variables.length() == 0) {
	    pluginLog.logHandler(LogModes.WARN.toString(),
		    "Variables for this execution are empty");
	}
	if (integrations.length() == 0) {
	    pluginLog.logHandler(LogModes.WARN.toString(),
		    "Integrations for this execution are empty");
	}

	String masterUrl = "{master_url}";
	String domainVariable = "{domain}";
	String applicationVariable = "{application}";
	String pipelineVariable = "{pipeline}";
	String workspace = "{workspace}";
	String apiKey = "{api_key}";
	String run = "{run}";

	if (variables.has(masterUrl)) {
	    pc.setMasterUrl(variables.getString(masterUrl));
	}
	if (variables.has(domainVariable)) {
	    pc.setDomain(variables.getString(domainVariable));
	}
	if (variables.has(applicationVariable)) {
	    pc.setApplication(variables.getString(applicationVariable));
	}
	if (variables.has(pipelineVariable)) {
	    pc.setPipeline(variables.getString(pipelineVariable));
	}
	if (variables.has(workspace)) {
	    pc.setWorkspace(variables.getString(workspace));
	}
	if (variables.has(run)) {
	    pc.setRunNumber(variables.getString(run));
	}
	if (variables.has(apiKey)) {
	    pc.setApiKey(variables.getString(apiKey));
	}
	String stepDetailsKey = "stepdetails";
	if (variables.has(stepDetailsKey)) {

	    JSONObject stepDetails = new JSONObject();
	    if (variables.get(stepDetailsKey) instanceof JSONObject) {
		stepDetails = variables.getJSONObject(stepDetailsKey);
	    } else if (variables.get(stepDetailsKey) instanceof String) {
		stepDetails = new JSONObject(
			variables.getString(stepDetailsKey));
	    }
	    if (stepDetails.length() > 0) {
		pc.setStepID(stepDetails.getString("id"));
		pc.setStepName(stepDetails.getString("name"));
		pc.setStepReportsLocation(stepDetails
			.getString("reports_location"));
	    }
	}
	createLogHandler();

	integrationDetails = new IntegrationHelper(getLogHandler())
		.getIntegration(this.getCloudmunchService(), getInputParams());

	pluginLog.logHandler(LogModes.DEBUG.toString(),
		"Integration Details are " + integrationDetails);

	// integrationDetails = new IntegrationHelper(getLogHandler())
	// .getIntegration(getInputParams(),
	// integrations);

    }

    private void createLogHandler() {
	pluginLog = new PluginLogHandler(getPluginContext());
    }

    /**
     * Gets the log handler.
     *
     * @return the log handler
     */
    public PluginLogHandler getLogHandler() {
	return pluginLog;
    }

    /**
     * This is a lifecycle method invoked at the completion of the plugin to
     * capture some data.
     */
    public void performAppcompletion() {
	pluginLog.logHandler(LogModes.INFO.toString(),
		"App completed successfully");
	endDate = new Date(System.currentTimeMillis());
	long timedifference = endDate.getTime() - startdate.getTime();
	long millisecondsTohours = TimeUnit.MILLISECONDS
		.toHours(timedifference);
	long millisecondsTominutes = TimeUnit.MILLISECONDS
		.toMinutes(timedifference);
	long hoursToMinutes = TimeUnit.HOURS
		.toMinutes(millisecondsTohours);
	long millisecondsToSeconds = TimeUnit.MILLISECONDS
		.toSeconds(timedifference);
	long hoursToSeconds = TimeUnit.MINUTES
		.toSeconds(millisecondsTominutes);

	String diff = String.format(
		"%d hour(s) %d min(s) %d sec(s)",
		millisecondsTohours,
		millisecondsTominutes
			- hoursToMinutes,
		millisecondsToSeconds - hoursToSeconds);
	pluginLog.logHandler(LogModes.INFO.toString(),
		"Total time taken: " + diff);
    }

    /**
     * @return the inputParams
     */
    public JSONObject getInputParams() {
	return inputParams;
    }

    /**
     * @param inputParams
     *            the inputParams to set
     */
    public void setInputParams(JSONObject inputParams) {
	this.inputParams = inputParams;
    }

    /**
     * @return the variables
     */
    public JSONObject getVariables() {
	return variables;
    }

    /**
     * @param variables
     *            the variables to set
     */
    public void setVariables(JSONObject variables) {
	this.variables = variables;
    }

    /**
     * @return the plugin context
     */
    public PluginContext getPluginContext() {
	return pc;
    }

    /**
     * @param pc
     *            the pc to set
     */
    public void setPc(PluginContext pc) {
	this.pc = pc;
    }

    /**
     * Returns the details of the integration for the provider select for plugin
     * 
     * @return the integrationDetails
     */
    public JSONObject getIntegrationDetails() {
	return integrationDetails;
    }

    /**
     * Outputs the pipeline variables.
     *
     * @param variableName
     *            the variable name
     * @param variableValue
     *            the variable value
     */
    public void outputPipelineVariables(String variableName,
	    String variableValue) {

	File variablesFileLoc = new File(pc.getStepReportsLocation(),
		pc.getStepID() + ".out");
	JSONObject variables = new JSONObject();
	try {
	    if (variablesFileLoc.exists()) {
		String contents;
		contents = FileUtils.readFileToString(variablesFileLoc);
		if (contents.trim().length() > 0) {
		    variables = new JSONObject(contents);
		}
	    }

	    variables.put("{" + variableName + "}", variableValue);
	    FileUtils.writeStringToFile(variablesFileLoc, variables.toString());
	} catch (IOException e) {
	    pluginLog
		    .logHandler("ERROR",
			    "Exception occurred while pipeline variables are being output ");
	    e.printStackTrace();
	}
    }

    /**
     * This method gives reference to ServerHelper,this helper class has all the
     * methods to get/set data on servers registered with cloudmunch.
     * 
     * @return ServerHelper serverhelper
     */
    public ServerHelper getCloudmunchServerHelper() {
	ServerHelper serverhelper = new ServerHelper(pc, pluginLog);
	return serverhelper;
    }

    public CloudmunchService getCloudmunchService() {
	if (null == this.cloudmunchService) {
	    this.cloudmunchService = new CloudmunchService(this.pc,
		    this.pluginLog);
	}

	return this.cloudmunchService;
    }

}
