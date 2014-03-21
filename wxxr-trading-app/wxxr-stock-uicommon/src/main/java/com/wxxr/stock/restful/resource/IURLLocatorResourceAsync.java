package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;

public interface IURLLocatorResourceAsync {	
	Async<byte[]> getURLSettings(String digest);
}
