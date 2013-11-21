package com.wxxr.mobile.core.rpc.rest;

import java.security.KeyStore;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.impl.ArticleVO;
import com.wxxr.mobile.core.rpc.impl.IArticleResource;
import com.wxxr.mobile.core.rpc.impl.MockApplication;
import com.wxxr.mobile.core.rpc.impl.StockArticleQuery;
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
			ExecutorService executor = Executors.newFixedThreadPool(3);

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.rpc.impl.MockApplication#getExecutor()
			 */
			@Override
			public ExecutorService getExecutorService() {
				return executor;
			}

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
//		builder.startup(context);
//		IArticleResource res = builder.getRestService(IArticleResource.class, "http://192.168.123.44:8480/mobilestock");
//		StockArticleQuery q = new StockArticleQuery();
//		q.setMarketCode("SH");
//		q.setStockId("601088");
//		q.setLimit(10);
//		q.setStart(0);
//		List<ArticleVO> result = res.getArticleByStock(q);
//		assertNotNull(result);
	}

}
