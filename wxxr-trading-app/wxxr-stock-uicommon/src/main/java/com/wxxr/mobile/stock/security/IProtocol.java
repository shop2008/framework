package com.wxxr.mobile.stock.security;

import java.io.IOException;

import com.wxxr.mobile.stock.security.impl.HttpRequest;
import com.wxxr.mobile.stock.security.impl.HttpResponse;


public interface IProtocol {
   public HttpResponse service(HttpRequest request) throws IOException ;
   public IProtocol getNext();
}
