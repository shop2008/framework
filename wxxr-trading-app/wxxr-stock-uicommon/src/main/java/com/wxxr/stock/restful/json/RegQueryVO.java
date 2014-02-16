package com.wxxr.stock.restful.json;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "RegQueryVO")
public class RegQueryVO {
	String username;
	String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegQueryVO [username=").append(username)
				.append(", password=").append(password).append("]");
		return builder.toString();
	}
}
