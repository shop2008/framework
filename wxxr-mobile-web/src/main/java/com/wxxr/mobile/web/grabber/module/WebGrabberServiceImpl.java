/**
 * 
 */
package com.wxxr.mobile.web.grabber.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.api.IGrabbingTaskFactory;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebCrawler;
import com.wxxr.mobile.web.grabber.api.IWebGrabberService;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public abstract class WebGrabberServiceImpl extends AbstractMicroKernel<IGrabberServiceContext, AbstractGrabberModule> implements IWebGrabberService {
	private static final Trace log = Trace.register(WebGrabberServiceImpl.class);
	
	private class CrawlerServiceContext extends AbstractContext implements IGrabberServiceContext{
		
	}
	
	private int maxThreads = 10, minThread = 2, taskQueueSize = 5, maxCrawler = 2;
	private ExecutorService executor;
	
	private CrawlerServiceContext context = new CrawlerServiceContext();
	
	@Override
	protected IGrabberServiceContext getContext() {
		return context;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executor;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel#start()
	 */
	@Override
	public void start() throws Exception {
		super.start();
		this.executor = new ThreadPoolExecutor(this.minThread, this.maxThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(this.taskQueueSize), 
				new ThreadFactory() {
					private AtomicInteger seqNo = new AtomicInteger(1);
					@Override
					public Thread newThread(Runnable task) {
						return new Thread(task, "Html Crawler Thread -- "+seqNo.getAndIncrement());
					}
				},new ThreadPoolExecutor.AbortPolicy());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel#stop()
	 */
	@Override
	public void stop() {
		if(this.executor != null){
			this.executor.shutdown();
			this.executor = null;
		}
		super.stop();
	}
	
	@Override
	public boolean grabWebSite(String htmlUrl, Object customData) {
		final IWebSiteGrabbingTask task = context.getService(IGrabbingTaskFactory.class).createSiteTask();
		task.init(context, htmlUrl,customData);
		return doGrabSite(task, new ArrayList<String>());
	}

	/**
	 * @param task
	 */
	protected boolean doGrabSite(final IWebSiteGrabbingTask task, List<String> grabbedSites) {
		if(grabbedSites.contains(task.getPageUrl().getURL())){
			return true;
		}
		if(log.isDebugEnabled()){
			log.debug("Going to grab site :"+task.getPageUrl());
		}
		boolean done = true;
		if(doGrabWebPage(task)){
			grabbedSites.add(task.getPageUrl().getURL());
			IWebSiteGrabbingTask nextTask = null;
			while((nextTask = task.getNextPageGrabbingTask()) != null){
				if(!doGrabSite(nextTask,grabbedSites)){
					done = false;
//					if(nextTask.getHtmlData().isNewHtml()){
//						task.getHtmlData().setHasNewDownloadedLinks(true);
//					}
				}
			}
		}else{
			done = false;
		}
		if(done){
			try {
//				if(task.getHtmlData().hasNewDownloadedLinks()||task.getHtmlData().isNewHtml()){
					getService(IWebContentStorage.class).makeContentReady(task);
//				}
				WebURL linkUrl = task.getLinkUrl();		// change link in parent page referring to local cached page
				IWebSiteGrabbingTask parent = task.getParentTask();
				if((parent != null)&&(linkUrl.getAnchor() != null)){
					parent.getHtmlData().addDownloadedUrl(linkUrl);
				}
			} catch (IOException e) {
				log.error("Failed to make content ready", e);
				return false;
			}
		}
		return done;
	}

	@Override
	public boolean grabWebPage(String htmlUrl, Object customData) {
		final IWebPageGrabbingTask task = context.getService(IGrabbingTaskFactory.class).createPageTask();
		task.init(context, htmlUrl,customData);
		if(doGrabWebPage(task)){
			try {
				getService(IWebContentStorage.class).makeContentReady(task);
			} catch (IOException e) {
				log.error("Failed to make content ready", e);
				return false;
			}
		}
		return !task.hasErrors();
	}

	/**
	 * @param task
	 */
	protected boolean doGrabWebPage(final IWebPageGrabbingTask task) {
		LinkedList<WebURL> urls = new LinkedList<WebURL>();
		while(true){
			urls.clear();
			WebURL webUrl = null;
			int cnt = 0;
			while((webUrl = task.getNextScheduledURL()) != null){
				urls.add(webUrl);
				cnt++;
				if(cnt >= this.maxCrawler){
					break;
				}
			}
			if(urls.isEmpty()){
				break;
			}
			final CountDownLatch latch = new CountDownLatch(urls.size());
			for (final WebURL url : urls) {
				while(true){
					try {
						getExecutorService().execute(new Runnable() {
							
							@Override
							public void run() {
								try {
									context.getService(IWebCrawler.class).processPage(task, url);
								} catch (Throwable e) {
									task.onProcessingError(url,e);
								}finally {
									latch.countDown();
								}
								
							}
						});
						break;
					}catch(RejectedExecutionException e){
						try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
						}
					}
				}
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
			}
		}
		task.finish(null);
		return !task.hasErrors();
	}

	/**
	 * @return the maxCrawler
	 */
	public int getMaxCrawler() {
		return maxCrawler;
	}

	/**
	 * @param maxCrawler the maxCrawler to set
	 */
	public void setMaxCrawler(int maxCrawler) {
		this.maxCrawler = maxCrawler;
	}

}
