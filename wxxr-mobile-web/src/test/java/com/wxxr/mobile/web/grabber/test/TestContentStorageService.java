/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebGrabbingTask;
import com.wxxr.mobile.web.grabber.common.AbstractContentStorage;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public class TestContentStorageService extends AbstractGrabberModule {

	private AbstractContentStorage storage = new AbstractContentStorage() {
		
		@Override
		protected File getResourceFile(IWebGrabbingTask task, WebURL url) {
			return new File(getContentRoot(task),getRelativePath(task, url));
		}
		
		@Override
		protected String getRelativePath(IWebGrabbingTask task, WebURL url) {
			if(url.getDepth() == 0){
				return getFileName(url);
			}else{
				return "resources/"+getFileName(url);
			}
		}
		
		@Override
		protected File getContentRoot(IWebGrabbingTask task) {
			Integer val = ((TestGrabbingTask)task).getSeqNo();
			return new File(contentRoot,val.toString());
		}
	};
	
	private File contentRoot;
	
	
	protected String getFileName(WebURL webUrl){
		String path = webUrl.getPath();
		int idx = path.lastIndexOf('/');
		if(idx > 0){
			path = path.substring(idx+1);
		}
		idx = path.lastIndexOf('.');
		if((webUrl.getDepth()==0)&&(idx < 0)){
			return path+".html";
		}
		try {
			return URLDecoder.decode(path,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("System shoud support UTF-8 charset!!!");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		String dir = System.getProperty("user.home");
		File file = new File(dir,"webCache");
		if(!file.exists()){
			file.mkdirs();
		}
		this.contentRoot = file;
		context.registerService(IWebContentStorage.class, this.storage);

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IWebContentStorage.class, this.storage);
	}

}
