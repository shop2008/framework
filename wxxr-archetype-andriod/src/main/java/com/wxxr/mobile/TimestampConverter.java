/**
 * 
 */
package com.wxxr.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;

/**
 * @author neillin
 *
 */
public class TimestampConverter implements IValueConvertor<Long, String> {

	private String format = "yyyy/MM/dd HHmmss";
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#destroy()
	 */
	@Override
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#getSourceType()
	 */
	@Override
	public Class<Long> getSourceType() {
		return Long.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#getTargetType()
	 */
	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext, java.util.Map)
	 */
	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> map) {
		if (map.containsKey("format")) {
			this.format = (String) map.get("format");
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#toSourceTypeValue(java.lang.Object)
	 */
	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		try {
			return new SimpleDateFormat(this.format).parse(s).getTime();
		} catch (ParseException e) {
			throw new ValidationException("Invalid timestamp format :"+s);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#toTargetTypeValue(java.lang.Object)
	 */
	@Override
	public String toTargetTypeValue(Long time) {
		return new SimpleDateFormat(this.format).format(new Date(time));
	}

}
