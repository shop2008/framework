/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.common.AbstractContentStorage;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public class TestContentStorageService extends AbstractGrabberModule {
	private static final Trace log = Trace.register(TestContentStorageService.class);
	private AbstractContentStorage storage = new AbstractContentStorage() {
		
		@Override
		protected File getResourceFile(IWebPageGrabbingTask task, WebURL url) {
			return new File(getContentRoot(task),getFileName(task, url));
		}
		
		@Override
		protected String getRelativePath(IWebPageGrabbingTask task, WebURL url) {
			if(task instanceof TestGrabbingTask){
				return getPageFileName(url);
			} else if(task instanceof TestSiteGrabbingTask){
				String target = getSiteFileName((TestSiteGrabbingTask)task, url);
				String page = getSiteFileName((TestSiteGrabbingTask)task, task.getPageUrl());
				return createRalativePath(page, target);
			}else{
				throw new IllegalArgumentException("Unknown task :"+task);
			}
		}
		
		@Override
		protected File getContentRoot(IWebPageGrabbingTask task) {
			if(task instanceof TestGrabbingTask){
				return getPageContentRoot((TestGrabbingTask)task);
			}else if(task instanceof TestSiteGrabbingTask){
				return getSiteContentRoot((TestSiteGrabbingTask)task);
			}
			throw new IllegalArgumentException("Unknown task :"+task);
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.web.grabber.common.AbstractContentStorage#makeContentReady(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask)
		 */
		@Override
		public void makeContentReady(IWebPageGrabbingTask task)
				throws IOException {
			super.makeContentReady(task);
			if(task instanceof TestSiteGrabbingTask){
				TestSiteGrabbingTask siteTask = (TestSiteGrabbingTask)task;
				if(siteTask.getParentTask() == null){
					siteMap.setProperty(siteTask.getDomain(), String.valueOf(siteTask.getSiteId()));
					saveSiteMap();
				}
			}
		}
	};
	
	private File contentRoot;
	private Properties siteMap;
	
	protected String getFileName(IWebPageGrabbingTask task, WebURL url) {
		if(task instanceof TestGrabbingTask){
			return getPageFileName(url);
		} else if(task instanceof TestSiteGrabbingTask){
			return getSiteFileName((TestSiteGrabbingTask)task, url);
		}else{
			throw new IllegalArgumentException("Unknown task :"+task);
		}
	}

	
	
	protected String createRalativePath(String srcPath, String targetPath) {
		String[] srcPaths = StringUtils.split(srcPath, '/');
		String[] tgtPaths = StringUtils.split(targetPath, '/');
		int idx = 0;
		for (; idx < tgtPaths.length;idx++) {
			String p = tgtPaths[idx];
			if(!p.equals(srcPaths[idx])){
				break;
			}
		}
		int upCnt = srcPaths.length-idx-1;
		StringBuffer buf = new StringBuffer();
		if(upCnt > 0){
			for(int i=0 ; i < upCnt; i++){
				buf.append("../");
			}
		}
		int cnt = 0;
		for (; idx < tgtPaths.length;idx++) {
			String p = tgtPaths[idx];
			if(cnt > 0){
				buf.append('/');
			}
			buf.append(p);
			cnt++;
		}
		return buf.toString();
	}
	
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
			throw new RuntimeException("System should support UTF-8 charset!!!");
		}
	}
	
	protected String getSiteFileName(TestSiteGrabbingTask task,WebURL webUrl){
		String domain = task.getDomain();
		String path = webUrl.getDomain()+webUrl.getPath();
		if(log.isDebugEnabled()){
			log.debug("domain :"+domain+", path :"+path);
		}
		path = path.substring(domain.length()+1);
		try {
			return URLDecoder.decode(path,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("System should support UTF-8 charset!!!");
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
		this.siteMap = new Properties();
		file = new File(this.contentRoot,"sitemap.properties");
		if(file.exists()){
			try {
				FileInputStream fis = new FileInputStream(file);
				this.siteMap.load(fis);
				fis.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to load in sitemap.properties");
			}
		}
		context.registerService(IWebContentStorage.class, this.storage);

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IWebContentStorage.class, this.storage);
	}

	/**
	 * @param task
	 * @return
	 */
	protected File getPageContentRoot(TestGrabbingTask task) {
		Integer val = task.getSeqNo();
		return new File(contentRoot,"pages/"+val.toString());
	}
	
	/**
	 * @param task
	 * @return
	 */
	protected File getSiteContentRoot(TestSiteGrabbingTask task) {
		if(task.getSiteId() == null){
			String domain = task.getDomain();
			String s = this.siteMap.getProperty(domain);
			if(s != null){
				int idx = Integer.parseInt(s);
				task.setSiteId(idx+1);
			}else{
				task.setSiteId(1000);
			}
		}
		return new File(contentRoot,"sites/"+task.getSiteId());

	}

	/**
	 * @param s
	 * @return
	 */
	protected void saveSiteMap() {
		File file = new File(this.contentRoot,"sitemap.properties");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			this.siteMap.store(fos, "");
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to store sitemap to sitemap.properties");
		}
	}

	/**
	 * @param url
	 * @return
	 */
	protected String getPageFileName(WebURL url) {
		if(url.getDepth() == 0){
			return getFileName(url);
		}else{
			return "resources/"+getFileName(url);
		}
	}


}
