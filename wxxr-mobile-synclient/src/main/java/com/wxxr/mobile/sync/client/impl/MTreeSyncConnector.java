/**
 * 
 */
package com.wxxr.mobile.sync.client.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Base64;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.sync.client.api.IMTreeDataSyncServerConnector;
import com.wxxr.mobile.sync.client.api.ISyncResource;
import com.wxxr.mobile.sync.client.api.MNodeDescriptor;
import com.wxxr.mobile.sync.client.api.UNodeDescriptor;
import com.wxxr.mobile.sync.client.dto.MNodeDescriptorVO;
import com.wxxr.mobile.sync.client.dto.UNodeDescriptorVO;

/**
 * @author wangxuyang
 * 
 */
public abstract class MTreeSyncConnector<T extends IKernelContext> extends AbstractModule<T> implements IMTreeDataSyncServerConnector {
	private static final Trace log = Trace.register(MTreeSyncConnector.class);

	private boolean isWifiConnected(){
		return context.getService(IDataExchangeCoordinator.class).checkAvailableNetwork()>0;
	}	
	private UNodeDescriptor fromVO(UNodeDescriptorVO vo) {
		if (vo == null) {
			return null;
		}
		UNodeDescriptor node = new UNodeDescriptor();
		node.setNodeId(vo.getNodeId());
		node.setChildren(fromVOS(vo.getChildren()));
		node.setLeaf(vo.isLeaf());
		if (vo.isLeaf()) {
			String leafPayload = vo.getLeafPayload();
			if (leafPayload!=null) {
				byte[] data = Base64.decode(leafPayload,Base64.NO_WRAP);
				node.setLeafPayload(data);
			}
		}		
		node.setDigest(Base64.decode(vo.getDigest(),Base64.NO_WRAP));
		return node;
	}

	private MNodeDescriptor fromVO(MNodeDescriptorVO vo) {
		if (vo == null) {
			return null;
		}
		MNodeDescriptor node = new MNodeDescriptor();
		node.setNodeId(vo.getNodeId());
		node.setLeaf(vo.isLeaf());
		if (vo.isLeaf()) {
			String leafPayload = vo.getLeafPayload();
			if (leafPayload!=null) {
				byte[] data = Base64.decode(leafPayload,Base64.NO_WRAP);
				node.setLeafPayload(data);
			}
		}	
		node.setDigest(Base64.decode(vo.getDigest(),Base64.NO_WRAP));
		return node;
	}

	private MNodeDescriptor[] fromVOS(MNodeDescriptorVO[] vos) {
		if (vos == null) {
			return null;
		}
		List<MNodeDescriptor> nodeList = new ArrayList<MNodeDescriptor>();
		for (MNodeDescriptorVO vo : vos) {
			if (vo instanceof UNodeDescriptorVO) {
				nodeList.add(fromVO((UNodeDescriptorVO) vo));
			} else {
				nodeList.add(fromVO(vo));
			}
		}
		return nodeList.size() > 0 ? nodeList.toArray(new MNodeDescriptor[nodeList.size()]) : null;
	}
	public byte[] getNodeData(String key, String nodePath) throws IOException {
		if (!isWifiConnected()) {// 检测网络环境
			throw new IOException("Network is not connected.");
		}
		ISyncResource resource = getRestService(ISyncResource.class);
		try {
			return resource.getNodeData(key,nodePath);			
		} catch (Exception e) {
			log.warn(String.format("Error when get data for key[%s],nodePath[%s]", key, nodePath), e);
			throw new RuntimeException(String.format("Check data change error,key[%s],nodepath[%s]", key, nodePath), e);
		}
	}

	

	public UNodeDescriptor isDataChanged(String key, String nodePath, byte[] digest) throws IOException {
		if (!isWifiConnected()) {// 检测网络环境
			throw new IOException("Network is not connected.");
		}
		if (digest==null) {
			digest = new byte[0];
		}
		String digestStr = Base64.encodeToString(digest,Base64.NO_WRAP);
		
		ISyncResource resource = getRestService(ISyncResource.class);
		try {
			if (log.isDebugEnabled()) {
				log.debug(String.format("isDataChanged:[key:%s,nodePath:%s,digest:%s]", key, nodePath, digestStr ));
			}
			UNodeDescriptorVO vo = resource.isDataChanged(key,nodePath, digestStr);
			return fromVO(vo);
		} catch (Exception e) {
			log.warn(String.format("Check data change error,key[%s],nodepath[%s]", key, nodePath), e);
			throw new RuntimeException(String.format("Check data change error,key[%s],nodepath[%s]", key, nodePath), e);
		}

	}

	
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IDataExchangeCoordinator.class);
	}

	@Override
	protected void startService() {
		if (log.isDebugEnabled()) {
			log.debug("serverUrl:" + getServerUrl());
		}
		if (getServerUrl() == null) {
			throw new IllegalStateException("There is not sshx sync server url setup, you should specified server target url[SSHX_SYNC_SERVER_URI] !!!");
		}
		context.registerService(IMTreeDataSyncServerConnector.class, this);

	}

	@Override
	protected void stopService() {
		context.unregisterService(IMTreeDataSyncServerConnector.class, this);
	}
	@Override
	public String getModuleName() {
		return this.getClass().getSimpleName();
	}
	protected abstract String getServerUrl();

	public <S> S getRestService(Class<S> clazz) {
		String url = getServerUrl();
		if (url == null) {
			throw new IllegalArgumentException("There is not  sync server url setup, you should specified server target url[SSHX_SYNC_SERVER_URI] !!!");
		}
		return context.getService(IRestProxyService.class).getRestService(clazz, url);
	}
	
}
