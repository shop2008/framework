/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;

/**
 * @author neillin
 *
 */
public class WebLinkExtractorRegistry implements IWebLinkExractorRegistry {

	private HashMap<String, List<IWebLinkExtractor>> registry = new HashMap<String, List<IWebLinkExtractor>>();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry#getLinkExtractor(java.lang.String)
	 */
	@Override
	public synchronized IWebLinkExtractor[] getLinkExtractors(String elementName) {
		List<IWebLinkExtractor> list =  registry.get(elementName.toLowerCase());
		return list != null ? list.toArray(new IWebLinkExtractor[list.size()]) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry#registerExtractor(java.lang.String, com.wxxr.mobile.web.grabber.api.IWebLinkExtractor)
	 */
	@Override
	public synchronized void registerExtractor(String elemName, IWebLinkExtractor extrator) {
		elemName = elemName.toLowerCase();
		List<IWebLinkExtractor> list = this.registry.get(elemName);
		if(list == null){
			list = new ArrayList<IWebLinkExtractor>();
			this.registry.put(elemName, list);
		}
		if(!list.contains(extrator)){
			list.add(extrator);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry#unregisterExtractor(java.lang.String, com.wxxr.mobile.web.grabber.api.IWebLinkExtractor)
	 */
	@Override
	public synchronized boolean unregisterExtractor(String elemName,
			IWebLinkExtractor extrator) {
		elemName = elemName.toLowerCase();
		List<IWebLinkExtractor> list = this.registry.get(elemName);
		if(list != null){
			return list.remove(extrator);
		}
		return false;
	}

}
