/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package adnsim;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class RestHttpClient {
	static void configureSSL(DefaultHttpClient httpclient) {
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLSocketFactory socketFactory;
		try {
			socketFactory = new SSLSocketFactory(acceptingTrustStrategy,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			socketFactory
					.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", 8443, socketFactory);
			httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (KeyManagementException | UnrecoverableKeyException
				| NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public static HttpResponse get(String originator, String token, String uri) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		configureSSL(httpclient);
		System.out.println("HTTP GET " + uri);
		HttpGet httpGet = new HttpGet(uri);

		httpGet.addHeader("X-M2M-Origin", originator);
		httpGet.addHeader("X-M2M-Key", token);
		httpGet.addHeader("Accept", "application/json");

		HttpResponse httpResponse = new HttpResponse();
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			try {
				closeableHttpResponse = httpclient.execute(httpGet);
				httpResponse.setStatusCode(closeableHttpResponse
						.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse
						.getEntity()));
			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HTTP Response " + httpResponse.getStatusCode()
				+ "\n" + httpResponse.getBody());
		return httpResponse;
	}

	public static HttpResponse put(String originator, String token, String uri,
			String body) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		configureSSL(httpclient);

		System.out.println("HTTP PUT " + uri + "\n" + body);

		HttpPut httpPut = new HttpPut(uri);

		httpPut.addHeader("X-M2M-Origin", originator);
		httpPut.addHeader("X-M2M-Key", token);
		httpPut.addHeader("Content-Type", "application/json");
		httpPut.addHeader("Accept", "application/json");

		HttpResponse httpResponse = new HttpResponse();

		CloseableHttpResponse closeableHttpResponse = null;

		try {
			httpPut.setEntity(new StringEntity(body));
			try {
				closeableHttpResponse = httpclient.execute(httpPut);
				httpResponse.setStatusCode(closeableHttpResponse
						.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse
						.getEntity()));

			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HTTP Response " + httpResponse.getStatusCode()
				+ "\n" + httpResponse.getBody());

		return httpResponse;
	}

	public static HttpResponse post(String originator, String token,
			String uri, String body, int ty) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		configureSSL(httpclient);

		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLSocketFactory socketFactory;
		try {
			socketFactory = new SSLSocketFactory(acceptingTrustStrategy,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			socketFactory
					.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", 8443, socketFactory);
			httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (KeyManagementException | UnrecoverableKeyException
				| NoSuchAlgorithmException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("HTTP POST " + uri + "\n" + body);
		HttpPost httpPost = new HttpPost(uri);

		httpPost.addHeader("X-M2M-Origin", originator);
		httpPost.addHeader("X-M2M-Key", token);
		httpPost.addHeader("Accept", "application/json");

		httpPost.addHeader("Content-Type", "application/json;ty=" + ty);

		HttpResponse httpResponse = new HttpResponse();
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			try {
				httpPost.setEntity(new StringEntity(body));

				closeableHttpResponse = httpclient.execute(httpPost);
				httpResponse.setStatusCode(closeableHttpResponse
						.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse
						.getEntity()));

			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HTTP Response " + httpResponse.getStatusCode()
				+ "\n" + httpResponse.getBody());
		return httpResponse;
	}

	public static HttpResponse delete(String originator, String token,
			String uri) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		configureSSL(httpclient);

		System.out.println("HTTP DELETE " + uri);

		HttpDelete httpDelete = new HttpDelete(uri);
		httpDelete.addHeader("X-M2M-Key", token);
		httpDelete.addHeader("X-M2M-Origin", originator);
		httpDelete.addHeader("Accept", "application/json");

		HttpResponse httpResponse = new HttpResponse();
		try {
			CloseableHttpResponse closeableHttpResponse = httpclient
					.execute(httpDelete);
			try {
				httpResponse.setStatusCode(closeableHttpResponse
						.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse
						.getEntity()));
			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();

		}
		System.out.println("HTTP Response " + httpResponse.getStatusCode()
				+ "\n" + httpResponse.getBody());
		return httpResponse;
	}
}