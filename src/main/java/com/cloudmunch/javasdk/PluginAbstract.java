package com.cloudmunch.javasdk;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.cloudmunch.argumentparser.ArgumentParser;
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
    public PluginContext pc = null;
    public Date startdate = null;
    public Date endDate = null;

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
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
		"App execution started");
	startdate = new Date(System.currentTimeMillis());
	processInputs(args);
    }

    /**
     * 
     * This method read and process the input parameters.
     * 
     * @param args
     *            the args
     */
    private void processInputs(String[] args) {

	ArgumentParser.parseArguments(args);
	inputParams = ArgumentParser.inputParams;
	variables = ArgumentParser.variables;
	integrations = ArgumentParser.integrations;
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Parameters are " + inputParams);
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Integrations are " + integrations);
	PluginLogHandler.logHandler(LogModes.DEBUG.toString(),
		"Variables are " + variables);

	if (inputParams.length() == 0) {
	    PluginLogHandler.logHandler(LogModes.WARN.toString(),
		    "Input parameters for this execution are empty");
	}
	if (variables.length() == 0) {
	    PluginLogHandler.logHandler(LogModes.WARN.toString(),
		    "Variables for this execution are empty");
	}
	if (integrations.length() == 0) {
	    PluginLogHandler.logHandler(LogModes.WARN.toString(),
		    "Integrations for this execution are empty");
	}

	pc = new PluginContext();
	String masterUrl = "{master_url}";
	String domainVariable = "{domain}";
	String applicationVariable = "{application}";
	String pipelineVariable = "{pipeline}";

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
    }

    /**
     * This is a lifecycle method invoked at the completion of the plugin to
     * capture some data.
     */
    public void performAppcompletion() {
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
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
	PluginLogHandler.logHandler(LogModes.INFO.toString(),
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
     * @return the pc
     */
    public PluginContext getPc() {
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
     * @return the integrations
     */
    public JSONObject getIntegrations() {
	return integrations;
    }

    /**
     * @param integrations
     *            the integrations to set
     */
    public void setIntegrations(JSONObject integrations) {
	this.integrations = integrations;
    }
}
