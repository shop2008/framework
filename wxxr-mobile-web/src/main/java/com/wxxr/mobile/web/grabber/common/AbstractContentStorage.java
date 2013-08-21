/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;

import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebGrabbingTask;
import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;
import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.IWebContent;
import com.wxxr.mobile.web.grabber.model.Page;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public abstract class AbstractContentStorage implements IWebContentStorage {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isProcessed(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask, com.wxxr.mobile.web.grabber.model.WebURL)
	 */
	@Override
	public boolean isDownloaded(IWebGrabbingTask task, WebURL url) {
		File resFile = getResourceFile(task,url);
		return resFile.exists();
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isProcessed(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask, com.wxxr.mobile.web.grabber.model.WebURL)
	 */
	@Override
	public IWebContent getContent(IWebGrabbingTask task, WebURL url) throws IOException {
		File resFile = getResourceFile(task,url);
		String path = resFile.getCanonicalPath();
		File propFile = new File(path+".properties");
		InputStream fis = new FileInputStream(propFile);
		Properties p = new Properties();
		p.load(fis);
		fis.close();
		fis = new FileInputStream(resFile);
		Page page = new Page(p.getProperty("URL"));
		page.setContentCharset(p.getProperty("Charset"));
		page.setContentEncoding(p.getProperty("Encoding"));
		page.setContentType(p.getProperty("ContentType"));
		page.setContentData(fis);
		return page;
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#saveContent(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask, com.wxxr.mobile.web.grabber.model.IWebContent)
	 */
	@Override
	public void saveContent(IWebGrabbingTask task, IWebContent content,WebURL url) throws IOException {
		File resFile = getResourceFile(task,url);
		String path = resFile.getCanonicalPath();
		File tmpFile = new File(path+".tmp");
		File propFile = new File(path+".properties");
		if(!tmpFile.getParentFile().exists()){
			tmpFile.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tmpFile);
			InputStream is = content.getContentData();
            byte[] tmp = new byte[4096];
            int l;
            while((l = is.read(tmp)) != -1) {
                fos.write(tmp, 0, l);
            }
			fos.close();
			fos = null;
			Properties p = new Properties();
			p.setProperty("Charset", content.getContentCharset());
			p.setProperty("Encoding", content.getContentEncoding());
			p.setProperty("ContentType", content.getContentType());
			p.setProperty("URL", content.getWebURL());
			fos = new FileOutputStream(propFile);
			p.store(fos, null);
			fos.close();
			fos = null;
			tmpFile.renameTo(resFile);
		}finally{
			if(fos != null){
				try {
					fos.close();
				}catch(Exception e){}
				fos = null;
			}
		}
		
	}	
	
	protected abstract File getResourceFile(IWebGrabbingTask task,WebURL url);

	protected abstract File getContentRoot(IWebGrabbingTask task);
	
	protected abstract String getRelativePath(IWebGrabbingTask task,WebURL url);


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#makeContentReady(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask)
	 */
	@Override
	public void makeContentReady(IWebGrabbingTask task) throws IOException {
		HtmlProcessingData data = task.getHtmlData();
		List<WebURL> outLinks = data.getDownloadedUrls();
		List<WebURL> downloadedLinks = data.getDownloadedUrls();
		boolean domUpdated = false;
		if(outLinks != null){
			for (WebURL webURL : outLinks) {
				ExtractedUrlAnchorPair anchor = webURL.getAnchor();
				if(anchor != null){
					String url = webURL.getURL();
					if((downloadedLinks != null)&&downloadedLinks.contains(webURL)){
						url = getRelativePath(task, webURL);
					}
					anchor.getElement().attr(anchor.getAttrName(), url);
					domUpdated = true;
				}
			}
		}
		if(domUpdated){
			WebURL webUrl = new WebURL();
			webUrl.setURL(data.getCurrentUrl());
			File resFile = getResourceFile(task,webUrl);
			String path = resFile.getCanonicalPath();
			File origFile = new File(path+".orig");
			resFile.renameTo(origFile);
			FileOutputStream fos = null;
			OutputStreamWriter sw = null;
			try {
				fos = new FileOutputStream(resFile);
				sw = new OutputStreamWriter(fos,data.getCharset());
				sw.write(data.getDocument().outerHtml());
			}finally{
				if(sw != null){
					try {
						sw.close();
					}catch(Throwable t){}
					sw = null;
				}
				if(fos != null){
					try {
						fos.close();
					}catch(Throwable t){}
					fos.close();
				}
			}
		}
		File readyFile = new File(getContentRoot(task),".ready");
		if(!readyFile.exists()){
			readyFile.createNewFile();
		}
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isContentReady(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask)
	 */
	@Override
	public boolean isContentReady(IWebGrabbingTask task) {
		File readyFile = new File(getContentRoot(task),".ready");
		return readyFile.exists();
	}
	
}
