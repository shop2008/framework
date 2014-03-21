/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.io.IOException;

/**
 * 
 * @author wangyan
 *
 */
public interface IContentManager {
	
	void saveContent(String type, String id, byte[] content) throws IOException;
	byte[] getContent(String type, String id) throws IOException;
	void delete(String type,String id)throws IOException;
	
	void updateStatus(String type, String id, String statusName, String status) throws IOException;
	String getStatus(String type, String id, String statusName) throws IOException;
	void deleteStatus(String type, String id,String statusName)throws IOException;
	String[] queryContentIds(String type, String statusName, String statusValue) throws IOException;
	boolean isExistContent(String type,String id) ;
	public Long getStatusLastModified(String type,String id,String statusName);
	public Long getContentLastModified(String type,String id);
}
