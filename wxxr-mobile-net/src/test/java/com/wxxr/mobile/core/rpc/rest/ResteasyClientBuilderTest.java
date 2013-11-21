package com.wxxr.mobile.core.rpc.rest;

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
import com.wxxr.mobile.core.rpc.impl.ArticleVO;
import com.wxxr.mobile.core.rpc.impl.IArticleResource;
import com.wxxr.mobile.core.rpc.impl.MockApplication;
import com.wxxr.mobile.core.rpc.impl.NewsQueryBO;
import com.wxxr.mobile.core.rpc.impl.StockUserResource;
import com.wxxr.mobile.core.rpc.impl.UserVO;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

public class ResteasyClientBuilderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	private IUserAuthCredential auth =  new IUserAuthCredential() {
		
		@Override
		public String getUserName() {
			return "13500001009";
		}
		
		@Override
		public String getAuthPassword() {
			return "404662";
		}

	};
	
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
				
			}

		};
		IKernelContext context = app.getContext();
		
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				System.out.println(host+"-"+realm);
				return auth;
			}
		});
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
		
		ResteasyRestClientService builder = new ResteasyRestClientService();
		builder.startup(context);
		/*try {
			UserVO vo = builder.getRestService(StockUserResource.class,"http://192.168.123.44:8480/mobilestock2").getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		IArticleResource res = builder.getRestService(IArticleResource.class, "http://192.168.123.44:8480/mobilestock2");
		NewsQueryBO query = new NewsQueryBO();
		query.setLimit(4);
		query.setStart(0);
		query.setType(String.valueOf(15));
		List<ArticleVO> result = res.getNewArticle(query);
		assertNotNull(result);
	}

}
