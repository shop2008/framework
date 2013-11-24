package com.thoughtworks.xstream;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="message")
public class RendezvousMessage {

   	public RendezvousMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	@XStreamOmitField
	private int messageType;

	@XStreamImplicit(itemFieldName="part")
	private List<String> content;

	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"yes", "no"})
	private boolean important;

	@XStreamConverter(SingleValueCalendarConverter.class)
	private Calendar created = new GregorianCalendar();

	public RendezvousMessage(int messageType, boolean important, String... content) {
		this.messageType = messageType;
		this.important = important;
		this.content = Arrays.asList(content);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RendezvousMessage [messageType=" + messageType + ", content="
				+ content + ", important=" + important + ", created=" + created
				+ "]";
	}
}