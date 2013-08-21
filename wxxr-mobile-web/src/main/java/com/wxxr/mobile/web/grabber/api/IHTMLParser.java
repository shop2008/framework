package com.wxxr.mobile.web.grabber.api;

import java.io.IOException;

import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.IWebContent;


public interface IHTMLParser {

	HtmlProcessingData parse(IWebGrabbingTask task,IWebContent page, String contextURL) throws IOException ;

}