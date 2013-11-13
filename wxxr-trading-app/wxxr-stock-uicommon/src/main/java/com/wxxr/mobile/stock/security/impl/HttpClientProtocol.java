package com.wxxr.mobile.stock.security.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.IProtocol;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpClientProtocol implements IProtocol {
	Logger log = Logger.getLogger(HttpClientProtocol.class);

	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient customerHttpClient;
	private String url_prefix;
	protected Proxy proxyObj;
	private IConnectorContext context;

	public HttpClientProtocol(IConnectorContext context) {
		this.url_prefix = context.getUrlPrefix();
		this.context = context;
	}

	public class CustomResponseHandler implements ResponseHandler<HttpResponse> {

		@Override
		public HttpResponse handleResponse(org.apache.http.HttpResponse response)
				throws ClientProtocolException, IOException {
			int curr_responseCode = response.getStatusLine().getStatusCode();
			Map<String, List<String>> headers = new HashMap<String, List<String>>();

			HeaderIterator i = response.headerIterator();
			while (i.hasNext()) {
				Header h = i.nextHeader();
				List<String> l = new ArrayList<String>();
				l.add(h.getValue());
				headers.put(h.getName(), l);
			}

			InputStream inputStream = response.getEntity().getContent();
			byte[] output = getbyte(inputStream);
			HttpResponse r = new HttpResponse(curr_responseCode, headers,
					output);
			return r;
		}

	}

	@Override
	public HttpResponse service(HttpRequest request) throws IOException {
		Long start_time = System.currentTimeMillis();
		HttpClient httpclient = HttpClientProtocol.getHttpClient();
		HttpUriRequest uriq = warp(request);
		HttpResponse res = httpclient.execute(uriq,
				new CustomResponseHandler());
		Long end_time = System.currentTimeMillis();
		res.setRequest(request);
		res.setExptime(end_time - start_time);
		log.debug(res.toString());
		return res;
	}

	private HttpUriRequest warp(HttpRequest request) {
		HttpUriRequest r = null;
		if (request != null) {
			// 缺省的头信息
			request.addHttpHeader(ProtocolCommon.DEVICEID,
					context.getDeviceId());
			request.addHttpHeader("deviceType", "2");
			request.addHttpHeader("appName", "finance");
			request.addHttpHeader("appVer", context.getVersionId());

			if (request instanceof HttpGetRequest || ProtocolCommon.GET.equals(request.getHttpMethod())) {
				r = new HttpGet(getUrl(request.getUri()));
			}
			if (request instanceof HttpPostRequest|| ProtocolCommon.POST.equals(request.getHttpMethod())) {
				r = new HttpPost(getUrl(request.getUri()));
				HttpPost post = (HttpPost) r;
				post.setEntity(new ByteArrayEntity(request.getInput()));
			}

			// 设置头信息
			Iterator i = request.getHttpHeaders().keySet().iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				r.setHeader(key, request.getHttpHeaders().get(key));
			}
		}

		return r;
	}

	@Override
	public IProtocol getNext() {
		return null;
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// HttpProtocolParams.setUserAgent(params,
			// "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
			// +
			// "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			ConnManagerParams.setTimeout(params, 10000);
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 4000);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	public static byte[] getbyte(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;

	}

	protected String getUrl(String url) {
		if (url != null) {
			if (url.startsWith("http:")) {
				return url;
			} else {
				return url_prefix + url;
			}
		}
		return null;
	}

	
}
