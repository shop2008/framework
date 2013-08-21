package com.wxxr.mobile.web.grabber.api;

import com.wxxr.mobile.web.grabber.model.WebContentFetchResult;
import com.wxxr.mobile.web.grabber.model.WebURL;


public interface IWebContentFetcher {

	WebContentFetchResult fetchHeader(IWebGrabbingTask task,WebURL webUrl);

}