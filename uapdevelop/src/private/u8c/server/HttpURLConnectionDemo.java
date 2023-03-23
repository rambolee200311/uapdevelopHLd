package u8c.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.nio.charset.Charset;
import nc.bs.logging.Logger;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.alibaba.fastjson.JSON;

public class HttpURLConnectionDemo {
	public static String httpPostWithJson(String url,String postData){
		String result="";
		HttpPost post = null;
		Logger.init("hanglianAPI");
		Logger.debug(postData);
	    try {
	    	// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
	        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	            
	        post = new HttpPost(url);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");	         
	                    
	        // 构建消息实体
	        StringEntity entity = new StringEntity(postData, Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);	            
	     // 响应模型
	        CloseableHttpResponse response = null;
	        response = httpClient.execute(post);
	            
	        // 返回数据
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
	        	Logger.debug("请求出错: "+statusCode);	            
	        }else{
	        	HttpEntity responseEntity = response.getEntity();
	            result=EntityUtils.toString(responseEntity);  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Logger.error(e.getMessage());
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	                Logger.error(e.getMessage());
	            }
	        }
	    }
		return result;
	}
	public static String operator(String url, Map<String, Object> headparams,String json) {
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		Logger.init("hanglianAPI");
		Logger.debug(json);
		httpPost.setRequestHeader("content-type",
				"application/json;charset=utf-8");
		for (Entry<String, Object> entry : headparams.entrySet()) {
			httpPost.setRequestHeader(entry.getKey(), entry.getValue()
					.toString());
		}
		try {
			RequestEntity entity = new StringRequestEntity(json,
					"application/json", "UTF-8");
			httpPost.setRequestEntity(entity);
			httpClient.executeMethod(httpPost);
			json=httpPost.getResponseBodyAsString();
			Logger.debug(json);
			return json;

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
