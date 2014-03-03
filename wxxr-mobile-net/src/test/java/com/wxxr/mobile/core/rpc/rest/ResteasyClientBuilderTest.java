package com.wxxr.mobile.core.rpc.rest;

import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.impl.MockApplication;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

public class ResteasyClientBuilderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetRestServiceClassOfTString() throws Exception {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		MockApplication app = new MockApplication(){

			@Override
			protected void initModules() {
				// TODO Auto-generated method stub
				
			}

		};
		IKernelContext context = app.getContext();
		service.startup(context);
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
			@Override
			public KeyStore getTrustKeyStore() {
				// TODO Auto-generated method stub
				return null;
			}
						
			@Override
			public KeyStore getSiteKeyStore() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HostnameVerifier getHostnameVerifier() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
//		ResteasyRestClientService builder = new ResteasyRestClientService();
//		builder.getProviderFactory().register(JacksonJsonProvider.class);
//		builder.startup(context);
//		IAsyncArticleResource res = builder.getRestService(IAsyncArticleResource.class,IArticleResource.class, "http://192.168.123.44:8480/mobilestock2");
//		StockArticleQuery q = new StockArticleQuery();
//		q.setMarketCode("SH");
//		q.setStockId("601088");
//		q.setLimit(10);
//		q.setStart(0);
//		
//		Async<ArticleVOs> result = res.getNewArticle("16", 0, 3);
//		AsyncFuture<ArticleVOs> future = new AsyncFuture<ArticleVOs>(result);
//		ArticleVOs vos = future.get(30, TimeUnit.SECONDS);
//		System.out.print("Result size :"+vos.getArticles().size());
	}

}
