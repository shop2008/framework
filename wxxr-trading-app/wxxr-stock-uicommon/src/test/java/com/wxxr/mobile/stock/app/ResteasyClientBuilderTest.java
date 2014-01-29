package com.wxxr.mobile.stock.app;

import java.security.KeyStore;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.resource.ArticleResource;

public class ResteasyClientBuilderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testGetRestServiceClassOfTString() throws Exception {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
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
				
			}

		};
		IKernelContext context = app.getContext();
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				return new IUserAuthCredential() {
					
					@Override
					public String getUserName() {
						return "13500001009";
					}
					
					@Override
					public String getAuthPassword() {
						return "404662";
					}

				};
			}
		});
		service.startup(context);
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
			@Override
			public KeyStore getTrustKeyStore() {
				
				return null;
			}
						
			@Override
			public KeyStore getSiteKeyStore() {
				
				return null;
			}
			
			@Override
			public HostnameVerifier getHostnameVerifier() {
				
				return null;
			}
		});
		
		MockRestClient builder = new MockRestClient();
		builder.init(context);
		ArticleResource res = builder.getRestService(ArticleResource.class, "http://192.168.123.44:8480/mobilestock2");
		NewsQueryBO query = new NewsQueryBO();
		query.setStart(0);
		query.setLimit(4);
		query.setType(String.valueOf(15));
	/*	List<ArticleVO> result = res.getNewArticle(query);
		System.out.println(result);
		assertNotNull(result);*/
	}

}
