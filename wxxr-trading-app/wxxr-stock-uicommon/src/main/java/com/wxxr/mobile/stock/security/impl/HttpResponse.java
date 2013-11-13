package com.wxxr.mobile.stock.security.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import android.util.Log;

import com.wxxr.mobile.stock.security.IHttpResponse;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpResponse implements IHttpResponse {
	private HttpRequest request;
	

	private int status;
	private Map<String, List<String>> headers;
	private byte[] output;
	private byte[] error_output;
	
	private String body;
	
	private Long exptime;
	private boolean isoutputChange=false;
	
	public HttpResponse(int status, Map<String, List<String>> headers,
			byte[] output) {
		this.status = status;
		this.headers = headers;
		this.output = output;
	}

	public HttpResponse(HttpRequest request ,int status, Map<String, List<String>> headers,
			byte[] output,Long exptime) {
		this.request= request;
		this.status = status;
		this.headers = headers;
		this.output = output;
		this.exptime=exptime;
	}
	public Long getExptime() {
		return exptime;
	}
	public void setExptime(Long exptime ) {
		this.exptime=exptime;
	}
	public byte[] getError_output() {
		return error_output;
	}

	public void setError_output(byte[] error_output) {
		this.error_output = error_output;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public byte[] getOutput() {
		return output;
	}
	public int getBodySize(){
		if (output!=null){
			return output.length;
		}else{
			return -1;
		}
	}
	public HttpRequest getRequest() {
		return request;
	}
	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	public String getBody() {
		if (body!=null && isoutputChange==false){
			return body;
		}
		String tmp = null;
		try {
			String content_encoding = getHeader(ProtocolCommon.CONTENT_ENCODING);
			if (ProtocolCommon.GZIP.equals(content_encoding) && output != null) {
				byte[] unzip_tmp = unGZip(output);
				tmp = unzip_tmp == null ? null : new String(unzip_tmp, "utf-8");
			} else {
				tmp = output == null ? null : new String(output, "utf-8");
			}
			body=tmp;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return tmp;
	}

	/***
	 * ��ѹZip
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	// public static byte[] unGZip(byte[] data) throws IOException {
	// ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	// ByteArrayInputStream bis = new ByteArrayInputStream(data);
	// GZIPInputStream in = new GZIPInputStream(bis);
	// byte[] buf = new byte[100];
	// int size = 0;
	// while ((size = in.read(buf)) != -1){
	// bytestream.write(buf, 0, size);
	// }
	//
	// byte imgdata[] = bytestream.toByteArray();
	// bytestream.close();
	// return imgdata;
	// }

	public static byte[] unGZip(byte[] data) throws IOException {
		byte[] b = null;

		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		GZIPInputStream gzip = new GZIPInputStream(bis);
		byte[] buf = new byte[1024];
		int num = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((num = gzip.read(buf, 0, buf.length)) != -1) {
			baos.write(buf, 0, num);
		}
		b = baos.toByteArray();
		baos.flush();
		baos.close();
		gzip.close();
		bis.close();
		return b;
	}

	public void setOutput(byte[] output) {
		this.output = output;
		isoutputChange=true;
	}

	public String getHeader(String key) {
		if (headers != null) {
			return getMapKeyValue(headers, key);
		}
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("==============Response====begin====================\n");
		sb.append("request uri =" + String.valueOf(request.getUri()) + "\n");
		sb.append("request data =" + String.valueOf(request.getData()) + "\n");
		sb.append("size =" + String.valueOf(this.getBodySize()) + "\n");
		sb.append("exptime =" + String.valueOf(this.getExptime()) + "\n");
		sb.append("status =" + String.valueOf(status) + "\n");
		sb.append("headers =" + String.valueOf(headers) + "\n");
		String tmp = null;

		sb.append("output =" + String.valueOf(this.getBody()) + "\n");
		try {
			tmp = error_output == null ? null : new String(error_output,
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			tmp = "UnsupportedEncodingException";
		}
		sb.append("error_output =" + String.valueOf(tmp) + "\n");

		sb.append("==============Response====end======================\n");
		return sb.toString();
	}

	private String getMapKeyValue(Map<String, List<String>> map,
			String targetkey) {
		String result;
		String key = getMapKey(map, targetkey);
		if (key != null) {
			List<String> a = map.get(key);
			if (a != null && !a.isEmpty())
				return result = a.get(0);
		}
		return null;
	}

	private String getMapKey(Map<String, List<String>> map, String targetkey) {
		if (map != null) {
			Set keys = map.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key != null
						&& targetkey.toLowerCase().equals(key.toLowerCase())) {
					return key;
				}
			}
		}
		return null;
	}

	public String getJSESSIONID() {
		String curr_JSESSIONID = null;
		String set_cookie_key = getMapKey(headers, "set-cookie"); // connection.getHeaderField("Set-Cookie");
		if (set_cookie_key != null) {
			List<String> set_cookie = headers.get(set_cookie_key);
			String tmp = set_cookie.get(0);
			curr_JSESSIONID = tmp.substring(0, tmp.indexOf(";"));
		}
		return curr_JSESSIONID;
	}

	@Override
	public boolean isSuccess() {
		Log.i("HttpResponse","getStatus----------------"+getStatus());
		if ((getStatus() == 200 && getHeader("BizException") == null)
				|| (getStatus() == 204 && getHeader("BizException") == null)) {
			return true;
		}
		return false;
	}
}
