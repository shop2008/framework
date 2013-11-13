package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wxxr.mobile.stock.client.util.RSACoder;
import com.wxxr.mobile.stock.client.util.StringUtils;
import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class LoginProtocol extends AbstractProtocol
{
	Logger log = Logger.getLogger(LoginProtocol.class);
	private MessageDigest md5Helper;
	private Auth auth = null;
	private String loginUserNameType;
	private IConnectorContext context;

	public LoginProtocol(IConnectorContext context)
	{
		this.context = context;
		this.loginUserNameType = this.context.getLoginUserNameType();
	}

	private String getPasswd()
	{
		return this.context.get(ProtocolCommon.PASSWD_KEY);
		// return "1234567";
	}

	protected String getUserLoginName()
	{
		return this.context.get(ProtocolCommon.USERNAME_KEY);
		// return "zhengjc";
	}

	@Override
	public HttpResponse service(HttpRequest request) throws IOException
	{
		
		// 待定
		// if ("/rest/client/getClientInfo".equals(request.getUri())){
		// request.addHttpHeader(ProtocolCommon.SPECIALEDITION,"gx" );
		// }
		HttpRequest re_bak = request.clone();
		HttpResponse res = getNext().service(request);
		if (res.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED)
		{
			auth = parseWwwauthorization(res.getHeader("www-authenticate"));
			// �ڴ˴�������Ž����������²��EnDeCodeProtocol����
			pwd2EnDeCode(re_bak);
			try
			{
				re_bak.addHttpHeader(ProtocolCommon.AUTHORIZATION,
						getAuthorizationHeader(request.getUri(), request.getHttpMethod(), auth));
			}
			catch (Exception e)
			{
				log.error("getAuthorizationHeader error!");
			}
			res = getNext().service(re_bak);

		}
		return res;
	}

	public String getAuthorizationHeader(String url, String method, Auth auth) throws Exception
	{
		if (auth != null)
		{
			String uri = ProtocolCommon.CONTEX_PATH + url;
			String nc = getNc();
			String cnonce = generateCnonce(this.getPasswd());
			String authorization = null;
			if (loginUserNameType.equals(ProtocolCommon.USER_NAME_TYPE_DEVICEID))
			{
				authorization = generateResponse(uri, method, auth.getRealm(), auth.getNonce(), nc, cnonce,
						auth.getQop(), auth.getOpaque(), getUserLoginName(), this.getPasswd());
			}
			else if (loginUserNameType.equals(ProtocolCommon.USER_NAME_TYPE_MSISDN))
			{
				authorization = generateResponse(uri, method, auth.getRealm(), this.getUserLoginName(), nc, cnonce,
						auth.getQop(), auth.getOpaque(), getUserLoginName(), this.getPasswd());
			}
			return authorization;
		}
		return null;
	}

	protected String getNc()
	{
		return context.getDeviceId();
	}

	protected static Auth parseWwwauthorization(String authorization)
	{
		if (authorization == null)
			return null;
		if (!authorization.startsWith("Digest "))
			return null;
		authorization = authorization.substring(7).trim();
		String[] tokens = authorization.split(",(?=(?:[^\"]*\"[^\"]*\")+$)");
		// String userName = null;
		String realmName = null;
		String nOnce = null;
		// String nc = null;
		// String cnonce = null;
		String qop = null;
		// String uri = null;
		// String response = null;
		// String method = request.getMethod();
		String opaque = null;
		for (int i = 0; i < tokens.length; i++)
		{
			String currentToken = tokens[i];
			if (currentToken.length() == 0)
				continue;

			int equalSign = currentToken.indexOf('=');
			if (equalSign < 0)
				return null;
			String currentTokenName = currentToken.substring(0, equalSign).trim();
			String currentTokenValue = currentToken.substring(equalSign + 1).trim();
			// if ("username".equals(currentTokenName))
			// userName = removeQuotes(currentTokenValue);
			if ("realm".equals(currentTokenName))
				realmName = removeQuotes(currentTokenValue, true);
			if ("nonce".equals(currentTokenName))
				nOnce = removeQuotes(currentTokenValue);
			// if ("nc".equals(currentTokenName))
			// nc = removeQuotes(currentTokenValue);
			// if ("cnonce".equals(currentTokenName))
			// cnonce = removeQuotes(currentTokenValue);
			if ("qop".equals(currentTokenName))
				qop = removeQuotes(currentTokenValue);
			// if ("uri".equals(currentTokenName))
			// uri = removeQuotes(currentTokenValue);
			// if ("response".equals(currentTokenName))
			// response = removeQuotes(currentTokenValue);
			if ("opaque".equals(currentTokenName))
				opaque = removeQuotes(currentTokenValue);
		}

		// String[] result = new String[]{realmName, nOnce, qop, opaque};
		Auth auth = new Auth();
		auth.setRealm(realmName);
		auth.setNonce(nOnce);
		auth.setQop(qop);
		auth.setOpaque(opaque);
		return auth;
	}

	protected static String removeQuotes(String quotedString, boolean quotesRequired)
	{
		// support both quoted and non-quoted
		if (quotedString.length() > 0 && quotedString.charAt(0) != '"' && !quotesRequired)
		{
			return quotedString;
		}
		else if (quotedString.length() > 2)
		{
			return quotedString.substring(1, quotedString.length() - 1);
		}
		else
		{
			return new String();
		}
	}

	/**
	 * Removes the quotes on a string.
	 */
	protected static String removeQuotes(String quotedString)
	{
		return removeQuotes(quotedString, false);
	}

	protected String generateCnonce(String password)
	{
		if (md5Helper == null)
			try
			{
				md5Helper = MessageDigest.getInstance("MD5");
			}
			catch (NoSuchAlgorithmException e)
			{
				log.error("md5Helper init error!" + e.getMessage());
			}
		long currentTime = System.currentTimeMillis();
		String nOnceValue = currentTime + ":" + password;
		byte[] buffer = null;
		buffer = md5Helper.digest(nOnceValue.getBytes());
		nOnceValue = RSACoder.bytesToHexStr(buffer);
		return nOnceValue;
	}

	protected String generateResponse(String uri, String method, String realm, String nonce, String nc, String cnonce,
			String qop, String opaque, String username, String password)
	{
		String md5a1 = getDigest(username, realm, password);
		String md5a2 = getmd2(uri, method);
		String serverDigestValue = md5a1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + md5a2;// md5a2
		byte[] valueBytes = null;
		valueBytes = serverDigestValue.getBytes();
		String serverDigest = null;
		serverDigest = RSACoder.bytesToHexStr(getMd5Helper().digest(valueBytes));
		StringBuffer sb = new StringBuffer();
		sb.append("Digest ");
		sb.append("username=\"" + username + "\",");
		sb.append("realm=\"" + realm + "\",");
		sb.append("nonce=\"" + nonce + "\",");
		sb.append("uri=\"" + uri + "\",");
		sb.append("qop=\"" + qop + "\",");
		sb.append("opaque=\"" + opaque + "\",");
		sb.append("nc=\"" + nc + "\",");
		sb.append("cnonce=\"" + cnonce + "\",");
		sb.append("response=\"" + serverDigest + "\"");
		// System.out.print("serverDigestValue :" + serverDigestValue);
		return sb.toString();
	}

	protected MessageDigest getMd5Helper()
	{
		if (md5Helper == null)
		{
			try
			{
				md5Helper = MessageDigest.getInstance("MD5");
			}
			catch (NoSuchAlgorithmException e)
			{
				throw new IllegalStateException(e.getMessage());
			}
		}
		return md5Helper;
	}

	protected String getDigest(String username, String realmName, String password)
	{

		String digestValue = username + ":" + realmName + ":" + password;
		byte[] valueBytes = null;
		byte[] digest = null;
		// Bugzilla 32137
		valueBytes = digestValue.getBytes();
		digest = getMd5Helper().digest(valueBytes);

		return RSACoder.bytesToHexStr(digest);
	}

	protected String getmd2(String uri, String method)
	{
		String digestValue = method + ":" + uri;
		byte[] valueBytes = null;
		byte[] digest = null;
		// Bugzilla 32137
		valueBytes = digestValue.getBytes();
		digest = getMd5Helper().digest(valueBytes);

		return RSACoder.bytesToHexStr(digest);
	}

	protected void pwd2EnDeCode(HttpRequest request)
	{

		String reg = "^[0-9]+$";
		Pattern pattern = Pattern.compile(reg);
		if (StringUtils.isBlank(getUserLoginName()))
			return;

		Matcher matcher = pattern.matcher(getUserLoginName());
		if (matcher.matches())
		{
			// �ֻ�����û�
			request.addHttpHeader(ProtocolCommon.SYNC_PASSWD, getPasswd());
		}

	}
}
