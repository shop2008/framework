package com.wxxr.mobile.stock.app;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.api.DataEntity;
import com.wxxr.mobile.core.rpc.api.Request;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpMethod;
import com.wxxr.mobile.core.rpc.http.api.HttpParamsBean;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.rpc.http.api.HttpResponse;
import com.wxxr.mobile.core.rpc.http.api.HttpStatus;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

public class SSLHttpRpcServiceTest extends TestCase {
	private KeyStore trustKeyStore;

	protected void setUp() throws Exception {
		try {
			trustKeyStore = KeyStore.getInstance("JKS");
			InputStream in = new FileInputStream(
					"src/test/resources/trust.keystore");
			trustKeyStore.load(in, "111111".toCharArray());
		} catch (Exception e) {
			System.err.println("Failed to load trust key store");
			e.printStackTrace();
		}
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateRequest() throws Throwable {

		MockApplication app = new MockApplication() {

			@Override
			protected void initModules() {
				// TODO Auto-generated method stub

			}

		};
		IKernelContext context = app.getContext();
		AbstractHttpRpcService service = new AbstractHttpRpcService();

		context.registerService(ISiteSecurityService.class,
				new ISiteSecurityService() {
					@Override
					public KeyStore getTrustKeyStore() {
						return trustKeyStore;
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
		service.startup(context);
		HttpRequest req = service
				.createRequest(
						"https://www.xrcj.cn:8443/mobilestock2/secure/user/getUser",
						new HttpParamsBean().setHttpMethod(HttpMethod.GET)
								.toMap());
		HttpFuture<HttpResponse, Request<HttpResponse>> future = new HttpFuture<HttpResponse, Request<HttpResponse>>();
		req.invokeAsync(future);
		HttpResponse resp = future.getResponse(0L, TimeUnit.SECONDS);
		assertTrue(resp != null);
		assertEquals(HttpStatus.SC_UNAUTHORIZED, resp.getStatusCode());
		System.out.println("Headers :[" + resp.getHeaders() + "]");
		DataEntity entity = resp.getResponseEntity();
		assertNotNull(entity);
		System.out.println("ContentType :" + entity.getContentType());
		System.out.println("ContentLength :" + entity.getContentLength());
		System.out.println("Content Encoding :" + entity.getContentEncoding());
		InputStream is = entity.getContent();
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		byte[] bytes = new byte[512];
		int len = 0;
		while ((len = is.read(bytes)) != -1) {
			aos.write(bytes, 0, len);
		}
		is.close();
		aos.close();
		byte[] data = aos.toByteArray();
		System.out.print(new String(data));
	}

	public void testHttpClient() throws Exception {
		/*Protocol.registerProtocol("https", new Protocol("https",
				new MySSLSocketFactory(), 443));
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod("https://www.whatever.com/");
		try {
			httpclient.executeMethod(httpget);
			System.out.println(httpget.getStatusLine());
		} finally {
			httpget.releaseConnection();
		}*/
	}

}
