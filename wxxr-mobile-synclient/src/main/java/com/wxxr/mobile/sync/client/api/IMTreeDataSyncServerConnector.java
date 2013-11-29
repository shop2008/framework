/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

import java.io.IOException;

/**
 * @author neillin
 *
 */
public interface IMTreeDataSyncServerConnector {
	String KEY_WIFI_NETWORK_CHECK_INTERVAL = "WIFI_NETWORK_CHECK_INTERVAL";
	String KEY_NON_WIFI_NETWORK_CHECK_INTERVAL = "NON_WIFI_NETWORK_CHECK_INTERVAL";
	UNodeDescriptor getNodeDescriptor(String key,String nodePath) throws IOException;
	
	byte[] getNodeData(String key,String nodePath) throws IOException;
	
	/**
	 * 检查指定节点的hash是否改变，如果改变返回该节点的UNodeDescriptor（该值应该与getNodeDescriptor()的返回值一致），否则返回NULL
	 * @param key
	 * @param nodePath
	 * @param digest
	 * @return
	 * @throws IOException
	 */
	UNodeDescriptor isDataChanged(String key,String nodePath, byte[] digest) throws IOException;
	
	byte[] getDataDigest(String key,String nodePath) throws IOException;
	
}
