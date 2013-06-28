package com.wxxr.mobile.core.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;

import com.wxxr.mobile.core.util.StringUtils;

public class TemplateHelper {

	public static final String PREFIX = ">>>>>>>";
	public static final String SUFFIX = "<<<<<<<";

	private Logger logger = Logger.getLogger("TEMPLATE");

	private final VelocityContext context;

	public TemplateHelper(VelocityContext context) {
		this.context = context;
	}

	public Map<String, Object> getExtra() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("r", "\r");
		map.put("n", "\n");
		map.put("t", "\t");

		return map;
	}

	public String join(String separator, List<String> parts) {
		StringBuilder sb = new StringBuilder();
		boolean isEmpty = true;

		for (String part : parts) {
			if (!StringUtils.isEmpty(part)) {
				if (!isEmpty) {
					sb.append(separator);
				}
				sb.append(part);
				isEmpty = false;
			}
		}

		return sb.toString();
	}

	public String insight() {
		String keys = Arrays.toString(context.getKeys());
		int depth = context.getCurrentMacroCallDepth();
		String macro = context.getCurrentMacroName();
		List<?> macros = context.getMacroLibraries();
		String macroStack = Arrays.toString(context.getMacroNameStack());
		String info = "INTROSPECTION {\n  keys:%s\n  depth:%s\n  macro:%s\n  macros:%s\n  macroStack:%s\n}";
		return String.format(info, keys, depth, macro, macros, macroStack);
	}

	public String locate(String templateName) {
		return templateName + ".vm";
	}

	public String fileMark(String filename) {
		return String.format("%s %s %s", PREFIX, filename,
				SUFFIX);
	}

	public boolean isInstanceOf(Object value, String className)
			throws ClassNotFoundException {
		return Class.forName(className).isInstance(value);
	}

	public String indent(String code, String prefix) {

		StringBuilder sb = new StringBuilder();

		String[] lines = StringUtils.splitPreserveAllTokens(code, "\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			if (!StringUtils.isWhitespace(line)) {
				line = prefix + line;
			}
			sb.append(line);

			if (i < lines.length - 1) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String capitalize(String content){
		if((content == null)||(content.length() == 0)){
			return content;
		}
		StringBuffer buf = new StringBuffer(content);
		buf.setCharAt(0,Character.toUpperCase(content.charAt(0)));
		return buf.toString();	
	}
	
	
	public void setAttributes(Map<String, ? extends Object> attributes) {
		for (Entry<String, ? extends Object> entry : attributes.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
	}

	public void removeAttributes(Map<String, ? extends Object> attributes) {
		for (String key : attributes.keySet()) {
			context.remove(key);
		}
	}

	public String info(String msg) {
		logger.info(msg);
		return "";
	}

	public String debug(String msg) {
		logger.debug(msg);
		return "";
	}

}