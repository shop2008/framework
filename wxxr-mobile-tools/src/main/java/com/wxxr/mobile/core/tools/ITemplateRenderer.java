package com.wxxr.mobile.core.tools;

import java.util.Map;

public interface ITemplateRenderer {

	String renderMacro(String macro, Map<String, Object> attributes,
			String[] params) throws Exception;

	String render(String template, Map<String, Object> attributes)
			throws Exception;

	String renderFromFile(String templateFilename,
			Map<String, Object> attributes) throws Exception;

}