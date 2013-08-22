package com.wxxr.mobile.web.grabber.test;

import org.apache.log4j.Level;

import com.wxxr.mobile.core.log.spi.ILoggerFactory;
import com.wxxr.mobile.core.log.spi.Log4jLoggerFactory;
import com.wxxr.mobile.core.util.BasicLog4jConfigurator;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.model.URLCanonicalizer;
import com.wxxr.mobile.web.grabber.module.HtmlParserModule;
import com.wxxr.mobile.web.grabber.module.WebCrawlerModule;
import com.wxxr.mobile.web.grabber.module.WebGrabberServiceImpl;
import com.wxxr.mobile.web.grabber.module.WebLinkExtractorRegistryModule;
import com.wxxr.mobile.web.grabber.module.WebPageFetcherModule;
import com.wxxr.mobile.web.link.extractor.ActionLinkExtractor;
import com.wxxr.mobile.web.link.extractor.BackgroundLinkExtractor;
import com.wxxr.mobile.web.link.extractor.CiteLinkExtractor;
import com.wxxr.mobile.web.link.extractor.CodebaseLinkExtractor;
import com.wxxr.mobile.web.link.extractor.DataLinkExtractor;
import com.wxxr.mobile.web.link.extractor.DataUrlLinkExtractor;
import com.wxxr.mobile.web.link.extractor.FormActionLinkExtractor;
import com.wxxr.mobile.web.link.extractor.HrefLinkExtractor;
import com.wxxr.mobile.web.link.extractor.IconLinkExtractor;
import com.wxxr.mobile.web.link.extractor.LongDescLinkExtractor;
import com.wxxr.mobile.web.link.extractor.MetaLinkExtractor;
import com.wxxr.mobile.web.link.extractor.SrcLinkExtractor;
import com.wxxr.mobile.web.link.extractor.UseMapLinkExtractor;

public class WebGrabberServiceImplTest {

	private WebGrabberServiceImpl grabber = new WebGrabberServiceImpl() {
		
		@Override
		protected void initModules() {
			registerKernelModule(new TestContentStorageService());
			registerKernelModule(new TestHttpService());
			registerKernelModule(new TestGrabbingTaskFactory());
			registerKernelModule(new TestSiteSecurityService());
			registerKernelModule(new WebCrawlerModule());
			registerKernelModule(new HtmlParserModule());
			registerKernelModule(new WebPageFetcherModule());
			registerKernelModule(createWebLinkExtratorRegistryModule());
		}
	};
	
//	
//	<a href=url>
//	<applet codebase=url>
//	<area href=url>
//	<base href=url>
//	<blockquote cite=url>
//	<body background=url>
//	<del cite=url>
//	<form action=url>
//	<frame longdesc=url> and <frame src=url>
//	<head profile=url>
//	<iframe longdesc=url> and <iframe src=url>
//	<img longdesc=url> and <img src=url> and <img usemap=url>
//	<input src=url> and <input usemap=url>
//	<ins cite=url>
//	<link href=url>
//	<object classid=url> and <object codebase=url> and <object data=url> and <object usemap=url>
//	<q cite=url>
//	<script src=url>
//	HTML 5 adds a few (and HTML5 seems to not use some of the ones above as well):
//
//	<audio src=url>
//	<button formaction=url>
//	<command icon=url>
//	<embed src=url>
//	<html manifest=url>
//	<input formaction=url>
//	<source src=url>
//	<video poster=url> and <video src=url>
//	These aren't necessarily simple URLs:
//
//	<object archive=url> or <object archive="url1 url2 url3">
//	<applet archive=url> or <applet archive=url1,url2,url3>
//	<meta http-equiv="refresh" content="seconds; url">
	
	
	protected void setUp() throws Exception {
		grabber.start();
	}

	protected void tearDown() throws Exception {
		grabber.stop();
	}

	public void testDoCrawl() {
		grabber.doCrawl("http://public.cmhelper.7500.com.cn/magnoliaPublic/txzs/webs.html", 100);
	}
	
	/**
	 * 
	 */
	protected AbstractGrabberModule createWebLinkExtratorRegistryModule() {
		WebLinkExtractorRegistryModule m = new WebLinkExtractorRegistryModule();
		IWebLinkExtractor extractor = new HrefLinkExtractor();
		m.registerExtractor("a", extractor);
		m.registerExtractor("area", extractor);
		m.registerExtractor("link", extractor);
		extractor = new SrcLinkExtractor();
		m.registerExtractor("frame", extractor);
		m.registerExtractor("iframe", extractor);
		m.registerExtractor("img", extractor);
		m.registerExtractor("input", extractor);
		m.registerExtractor("script", extractor);
		m.registerExtractor("audio", extractor);
		m.registerExtractor("embed", extractor);
		m.registerExtractor("source", extractor);
		m.registerExtractor("video", extractor);
		m.registerExtractor("meta", new MetaLinkExtractor());
		m.registerExtractor("li", new DataUrlLinkExtractor());
		extractor = new DataLinkExtractor();
		m.registerExtractor("object", extractor);
		extractor = new ActionLinkExtractor();
		m.registerExtractor("form", extractor);
		extractor = new FormActionLinkExtractor();
		m.registerExtractor("button", extractor);
		m.registerExtractor("input", extractor);
		extractor = new UseMapLinkExtractor();
		m.registerExtractor("img", extractor);
		m.registerExtractor("input", extractor);
		m.registerExtractor("object", extractor);
		extractor = new BackgroundLinkExtractor();
		m.registerExtractor("body", extractor);
		extractor = new CodebaseLinkExtractor();
		m.registerExtractor("applet", extractor);
		m.registerExtractor("object", extractor);
		extractor = new CiteLinkExtractor();
		m.registerExtractor("del", extractor);
		m.registerExtractor("blockquote", extractor);
		m.registerExtractor("ins", extractor);
		m.registerExtractor("q", extractor);
		extractor = new LongDescLinkExtractor();
		m.registerExtractor("frame", extractor);
		m.registerExtractor("iframe", extractor);
		m.registerExtractor("img", extractor);
		extractor = new IconLinkExtractor();
		m.registerExtractor("command", extractor);
		return m;
	}

	public static void main(String[] args) throws Exception {
		ILoggerFactory.DefaultFactory.setLoggerFactory(new Log4jLoggerFactory());
		BasicLog4jConfigurator logConfig = new BasicLog4jConfigurator();
		logConfig.configure();
		logConfig.configureConsoleAppender("com.wxxr.mobile",Level.DEBUG);
		WebGrabberServiceImplTest tester = new WebGrabberServiceImplTest();
		tester.setUp();
		tester.testDoCrawl();
		tester.tearDown();
	}

}
