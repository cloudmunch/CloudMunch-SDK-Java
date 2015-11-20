package com.cloudmunch.argumentparser;

public class ParameterHandler {

	private String parameter = "";
	private boolean required = false;

	public String getParameter() {
		return parameter;
	}

	public ParameterHandler setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public boolean isRequired() {
		return required;
	}

	public ParameterHandler setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
