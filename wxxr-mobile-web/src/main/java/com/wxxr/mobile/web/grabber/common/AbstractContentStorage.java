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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
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

	private static final Trace log = Trace.register(AbstractContentStorage.class);
	private SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isProcessed(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask, com.wxxr.mobile.web.grabber.model.WebURL)
	 */
	@Override
	public String getContentLastModified(IWebPageGrabbingTask task, WebURL url) {
		try {
			File resFile = getResourceFile(task,url);
			String path = resFile.getCanonicalPath();
			File origFile = new File(path+".orig");
			String date = null;
			if(resFile.exists()){
				Date d = null;
				if(origFile.exists()){
					d = new Date(origFile.lastModified());
				}else{
					d = new Date(resFile.lastModified());
				}
				synchronized(this.httpDateFormat){
					date = this.httpDateFormat.format(d);
				}
			}
			return date;
		}catch (Exception e) {
			return null;
		}
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isProcessed(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask, com.wxxr.mobile.web.grabber.model.WebURL)
	 */
	@Override
	public IWebContent getContent(IWebPageGrabbingTask task, WebURL url) throws IOException {
		File resFile = getResourceFile(task,url);
		String path = resFile.getCanonicalPath();
		File propFile = new File(path+".x");
		File origFile = new File(path+".orig");
		Properties p = new Properties();
		InputStream fis = null;
		if(propFile.exists()){
			fis = new FileInputStream(propFile);
			p.load(fis);
			fis.close();
		}
		if(origFile.exists()){
			fis = new FileInputStream(origFile);
		}else{
			fis = new FileInputStream(resFile);
		}
		String s = p.getProperty("URL");
		Page page = null;
		if(s != null){
			page = new Page(s);
		}else{
			page = new Page(url.getURL());
		}
		s = p.getProperty("Charset");
		if(s != null){
			page.setContentCharset(s);
		}
		s = p.getProperty("ContentType");
		if(s != null){
			page.setContentType(s);
		}
		page.setContentData(fis);
		synchronized(this.httpDateFormat){
			page.setLastModifiedDate(this.httpDateFormat.format(new Date(resFile.lastModified())));
		}
		return page;
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#saveContent(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask, com.wxxr.mobile.web.grabber.model.IWebContent)
	 */
	@Override
	public void saveContent(IWebPageGrabbingTask task, IWebContent content,WebURL url) throws IOException {
		File resFile = getResourceFile(task,url);
		String path = resFile.getCanonicalPath();
		File tmpFile = new File(path+".tmp");
		File propFile = new File(path+".x");
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
			if(Util.isHtmlContent(content.getContentType())){
				Properties p = new Properties();
				p.setProperty("Charset", content.getContentCharset());
				p.setProperty("ContentType", content.getContentType());
				p.setProperty("URL", content.getWebURL());
				fos = new FileOutputStream(propFile);
				p.store(fos, null);
				fos.close();
				fos = null;
			}
			if(resFile.exists()){
				if(!resFile.delete()){
					log.warn("Failed to delete existing content file :"+resFile.getAbsolutePath());
				}
			}
			if(!tmpFile.renameTo(resFile)){
				log.warn("Failed to rename downloaded content file :"+tmpFile.getAbsolutePath()+" to regular file :"+resFile.getAbsolutePath());
			}
			String lastModified = content.getLastModifiedDate();
			Date d = null;
			if(lastModified != null){
				synchronized(this.httpDateFormat){
					try {
						d = httpDateFormat.parse(lastModified);
					}catch(Exception e){}
				}
			}
			if(d != null){
				resFile.setLastModified(d.getTime());
			}
		}finally{
			if(fos != null){
				try {
					fos.close();
				}catch(Exception e){}
				fos = null;
			}
		}
		
	}	
	
	protected abstract File getResourceFile(IWebPageGrabbingTask task,WebURL url);

	protected abstract File getContentRoot(IWebPageGrabbingTask task);
	
	protected abstract String getRelativePath(IWebPageGrabbingTask task,WebURL url);


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#makeContentReady(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask)
	 */
	@Override
	public void makeContentReady(IWebPageGrabbingTask task) throws IOException {
		HtmlProcessingData data = task.getHtmlData();
		if(data == null){		// duplicated grabbing task
			return;
		}
		List<WebURL> outLinks = data.getOutgoingUrls();
		List<WebURL> downloadedLinks = data.getDownloadedUrls();
		boolean domUpdated = false;
		if(outLinks != null){
			for (WebURL webURL : outLinks) {
				if(log.isDebugEnabled()){
					log.debug("Going to update link :"+webURL);
				}
				ExtractedUrlAnchorPair anchor = webURL.getAnchor();
				if(anchor != null){
					String url = webURL.getURL();
					if((downloadedLinks != null)&&downloadedLinks.contains(webURL)){
						url = getRelativePath(task, webURL);
					}
					IWebLinkExtractor[] extractors = task.getContext().getService(IWebLinkExractorRegistry.class).getLinkExtractors(anchor.getElement().tagName().toLowerCase());
					if(extractors != null){
						for (IWebLinkExtractor extractor : extractors) {
							if(extractor.updateLink(task.getContext(), anchor, url)){
								domUpdated = true;
								if(log.isDebugEnabled()){
									log.debug("Link :"+webURL+" was updated !");
								}
							}
						}
					}
				}
			}
		}
		if(domUpdated){
			WebURL webUrl = new WebURL();
			webUrl.setURL(data.getCurrentUrl());
			File resFile = getResourceFile(task,webUrl);
			String path = resFile.getCanonicalPath();
			File origFile = new File(path+".orig");
			if(!origFile.exists()) {
				resFile.renameTo(origFile);
			}
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
	 * @see com.wxxr.mobile.web.grabber.api.IWebContentStorage#isContentReady(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask)
	 */
	@Override
	public boolean isContentReady(IWebPageGrabbingTask task) {
		File readyFile = new File(getContentRoot(task),".ready");
		return readyFile.exists();
	}
	
}
