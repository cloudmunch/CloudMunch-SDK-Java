package com.cloudmunch.javasdk.arguments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentParser {

	public static JSONObject inputParams = new JSONObject();
	public static String strInputParams = "";
	public static StringBuffer message = new StringBuffer(50);

	public final static List<ParameterHandler> requiredParams = new ArrayList<ParameterHandler>();

	public static void parseArguments(String[] args) {
		System.out.println("Inside parese arguments");
		Options options = new Options();

		Option jsonInputOption = new Option("i", "jsoninput", true, "List of User arguments in the form of JSON");
		jsonInputOption.setRequired(true);

		options.addOption(jsonInputOption);

		// HelpFormatter formatter = new HelpFormatter();
		// formatter.printHelp("JavaInstrument", options);

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(jsonInputOption.getLongOpt())) {
				strInputParams = cmd.getOptionValue(jsonInputOption.getLongOpt());
				inputParams = new JSONObject(cmd.getOptionValue(jsonInputOption.getLongOpt()));
				String strinput=inputParams.toString();
				System.out.println("input parameters are:"+strinput);
			}

			
		} catch (MissingOptionException e) {
			@SuppressWarnings("unchecked")
			List<String> missedOptions = e.getMissingOptions();
			for (int i = 0; i < missedOptions.size(); i++) {
				System.out.println(options.getOption(missedOptions.get(i)));
				message.append("Missing Option : " + options.getOption(missedOptions.get(i))).append("\r\n");
			}
			writeReport();
			// System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			message.append(e.getMessage()).append("\r\n");
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			message.append(e.getMessage()).append("\r\n");
		}
		
	}

	public static String getValue(String key) throws JSONException {
		return inputParams.getString(key);
	}

	public static boolean hasKey(String key) {
		return inputParams.has(key);
	}

	public static void writeReport() {
		System.out.println(message.toString());
	}

	public static void printDebugMessage(String message) throws JSONException {
		if (hasKey("DEBUG") && getValue("DEBUG").equalsIgnoreCase("TRUE")) {
			System.out.println(message);
		}
	}

	public static String getArgumentParameter(boolean includeOptional) throws JSONException {
		JSONObject param = new JSONObject();
		for (int i = 0; i < requiredParams.size(); i++) {
			ParameterHandler ph = requiredParams.get(i);
			if (ph.isRequired()) {
				String argument = ph.getParameter();
				param.put(argument, " ");
			} else {
				if (includeOptional) {
					String argument = ph.getParameter();
					param.put(argument, " ");
				}
			}
		}

		return StringUtils.replace(param.toString(), "\"", "\\\"");
	}
}
