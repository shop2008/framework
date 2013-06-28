package com.wxxr.mobile.core.tools;
public class ParsedTypeNameParam {

	private final String wildcard;
	private final String type;

	public ParsedTypeNameParam(String wildcard, String type) {
		this.wildcard = wildcard;
		this.type = type;
	}

	public String getWildcard() {
		return wildcard;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ParsedTypeNameParam [wildcard=" + wildcard + ", type=" + type
				+ "]";
	}

}