/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class TagPropertiesParser {
	private static final Pattern KEY_PATTERN = Pattern.compile("([a-zA-Z][a-zA-Z0-9_]{0,20})[ ]*=[ ]*");
	private static class Result {
		String val;
		int nextIdx;
	}
	public static Properties parse(String tag) {
		Matcher m = KEY_PATTERN.matcher(tag);
		Properties p = null;
		int start = 0;
		while((start >= 0)&&m.find(start)){
			String key = m.group(1);
			int valStart = m.end();
			Result res = getValue(tag, valStart);
			String val = res.val;
			start = res.nextIdx;
			if(p == null){
				p = new Properties();;
			}
			p.setProperty(key, val);
		}
		return p;
	}
	
	private static Result getValue(String tag, int start){
		Result res = new Result();
		int closeIdx = -1;
		boolean curly = false;
		if(tag.charAt(start) == '['){
			curly = true;
			start+=1;
			closeIdx = findCloseSquare(tag,start);
		}else{
			closeIdx = findNextComma(tag, start);
		}
		if(closeIdx < 0){				
			res.val = StringUtils.trimToEmpty(tag.substring(start));
			res.nextIdx = -1;
		}else {
			int end = curly ? closeIdx-1 : closeIdx;
			res.val = StringUtils.trimToEmpty(tag.substring(start,end));
			res.nextIdx = closeIdx+1;
		}
		return res;
	}
	
	private static int findCloseSquare(String tag, int start){
		int count = 1;
		for(; (count > 0)&&(start < tag.length()) ; start++){
			char ch = tag.charAt(start);
			if(ch == '['){
				count++;
			}else if(ch == ']'){
				count--;
			}
		}
		return count > 0 ? -1 : start;
	}
	
	private static int findNextComma(String tag, int start){
		for(int i= start; i < tag.length() ; i++){
			char ch = tag.charAt(i);
			if(ch == ','){
				return i;
			}
		}
		return -1;
	}

}
