package com.wxxr.mobile.stock.security.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.IProtocol;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpProtocol implements IProtocol {
	Logger log = Logger.getLogger(HttpProtocol.class);

	private String url_prefix;
	protected Proxy proxyObj;
	private IConnectorContext context;

	public HttpProtocol() {
	}

	public HttpProtocol(IConnectorContext context) {
		this.url_prefix = context.getUrlPrefix();
		this.context=context;
	}

	@Override
	public  HttpResponse service(HttpRequest request)
			throws IOException {
		HttpURLConnection connection = null;

		try {
			request.addHttpHeader(ProtocolCommon.DEVICEID, context.getDeviceId());
			request.addHttpHeader("deviceType", "2");
			request.addHttpHeader("appName", "finance");
			request.addHttpHeader("appVer", context.getVersionId());
			Long start_time = System.currentTimeMillis();
			log.debug("url_prefix" +url_prefix);
			log.debug(request.toString());
			URL u = new URL(getUrl(request.getUri()));
			if (proxyObj != null) {
				connection = (HttpURLConnection) u.openConnection(proxyObj);
			} else {
				connection = (HttpURLConnection) u.openConnection();
			}
			Map<String, String> requestPropertyMap = request.getHttpHeaders();
			if (requestPropertyMap != null) {
				Set set = requestPropertyMap.keySet();
				Iterator it = set.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					connection.setRequestProperty(key,
							requestPropertyMap.get(key));
				}
			}
			connection.setRequestMethod(request.getHttpMethod());
			connection.setConnectTimeout(request.getConnectTimeout());
			connection.setReadTimeout(request.getReadTimeout());
			if (!request.getHttpMethod().equals(ProtocolCommon.GET)) {
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				OutputStream os = connection.getOutputStream();
				os.write(request.getInput());
				os.flush();
			}
			int curr_responseCode = connection.getResponseCode();
			byte[] output = null;
			byte[] error_output = null;
			if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
				output = getbyte(connection.getInputStream());
			} else {
				error_output = getbyte(connection.getErrorStream());

			}
			Map<String, List<String>> header = connection.getHeaderFields();
			Long end_time = System.currentTimeMillis();
			HttpResponse r = new HttpResponse(request,curr_responseCode, header, output,end_time - start_time);
			r.setError_output(error_output);
			log.debug(r.toString());
			return r;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	@Override
	public IProtocol getNext() {
		return null;
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
}
