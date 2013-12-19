package com.wxxr.stock.restful.resource;

import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.article.ejb.api.ArticleVOs;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.json.PullMessageVOs;

public class ArticleResourceTest extends TestCase{

	private ArticleResource articleResource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		articleResource=null;
	}
	protected void init() {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
		MockApplication app = new MockApplication(){
			ExecutorService executor = Executors.newFixedThreadPool(3);

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
		articleResource=builder.getRestService(ArticleResource.class,"http://192.168.123.44:8480/mobilestock2");
	}
	public void testGetNewArticle()throws Exception{
		NewsQueryBO query = new NewsQueryBO();
		query.setStart(0);
		query.setLimit(3);
		query.setType("18");
		query.setUid(0);
		ArticleVOs a = articleResource.getNewArticle("16", 0, 3);
		assertNotNull(a);
		System.out.println(a);
	}
	public void testGetPullMessage()throws Exception{
		PullMessageVOs a = articleResource.getPullMessage(0,4);
		System.out.println(a);
	}
}
