/**
 * 
 */
package org.apache.log4j;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author neillin
 *
 */
public interface IDateFormat {
	void setTimeZone(TimeZone timezone);
	String format(Date date);
}
