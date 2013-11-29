/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

/**
 * @author neillin
 *
 */
public interface IDataConsumer {
	String[] getAllReceivedDataKeys();
	Object getGroupId(byte[] leafPayload);
	void dataReceived(Object grpId, byte[] data);
	void dateReceiving(Object grpId);
	void dataReceivingFailed(Object grpId);
	void allDataReceived();
	byte[] removeReceivedData(Object grpId);
	byte[] getReceivedData(Object grpId);
}
