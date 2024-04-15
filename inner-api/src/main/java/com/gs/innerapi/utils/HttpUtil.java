package com.gs.innerapi.utils;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
	public static final CloseableHttpClient httpClient;
	
	static {
		/** HTTP连接池配置 **/	
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//		cm.setValidateAfterInactivity(10000);
		cm.setMaxTotal(200);// max connection
		cm.setDefaultMaxPerRoute(200); //
		/** 超时时间配置 **/
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间
				.setConnectionRequestTimeout(5000) // 获取连接超时时间
				.setSocketTimeout(5000)//// 设置请求超时时间
				// .setProxy(new HttpHost("183.196.97.125", 46006, "HTTP"))
				.build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm).build();
	}
	
	
	public static String postJSON(String url, String jsonParam, String charset) throws Exception {
		String result = null;
		CloseableHttpResponse response = null;
		try {
			 HttpPost httppost = new HttpPost(url);
			 StringEntity entity = new StringEntity(jsonParam,"utf-8");//解决中文乱码问题
			 httppost.setHeader("Content-Type", "application/json; charset=utf-8");
			 httppost.setEntity(entity);
			 response = httpClient.execute(httppost);
			 if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				 result = EntityUtils.toString(response.getEntity(), "UTF-8");	 
			 }
		} catch(Exception e) {
			throw e;
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	
	public static String post(String url, Map<String, String> params) {
		String result = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			 List<NameValuePair> list = new ArrayList<NameValuePair>(); 
			 for(Map.Entry<String, String> entry : params.entrySet()) {
				  list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  //请求参数
			 }
			 UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8"); 
			 httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				 result = EntityUtils.toString(response.getEntity(), "UTF-8");	 
			 }
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
