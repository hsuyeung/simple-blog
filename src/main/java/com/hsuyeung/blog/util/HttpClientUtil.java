package com.hsuyeung.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.model.vo.httpclient.HttpClientResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;


/**
 * httpClient 工具类
 *
 * @author hsuyeung
 * @date 2021/10/24
 */
@Component
public class HttpClientUtil {
    /**
     * 编码格式。发送编码格式统一用 UTF-8
     */
    private static final String ENCODING = "UTF-8";

    @Resource(name = "customRequestConfig")
    private RequestConfig requestConfig;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 发送 get 请求。不带请求头和请求参数
     *
     * @param url 请求地址
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doGet(String url) throws URISyntaxException, IOException {
        return doGet(url, null, null);
    }

    /**
     * 发送 get 请求。带请求参数
     *
     * @param url    请求地址
     * @param params 请求参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doGet(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return doGet(url, null, params);
    }

    /**
     * 发送 get 请求。带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params)
            throws URISyntaxException, IOException {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(requestConfig);
        // 设置请求头
        this.packageHeader(headers, httpGet);

        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行请求并获得响应结果
        return this.getHttpClientResult(httpClient, httpGet);
    }

    /**
     * 发送表单参数类型的 post 请求。不带请求头和请求参数
     *
     * @param url 请求地址
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByForm(String url) throws IOException {
        return doPostByForm(url, null, null);
    }

    /**
     * 发送表单参数类型的 post 请求。带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByForm(String url, Map<String, String> params) throws IOException {
        return doPostByForm(url, null, params);
    }

    /**
     * 发送表单参数类型的 post 请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        // 设置请求头
        this.packageHeader(headers, httpPost);
        // 封装请求参数
        this.packageParam(params, httpPost);
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行请求并获得响应结果
        return this.getHttpClientResult(httpClient, httpPost);
    }

    /**
     * 发送 json 参数类型的 post 请求。不带请求头和请求参数
     *
     * @param url 请求地址
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByJson(String url) throws IOException {
        return doPostByJson(url, null, null);
    }

    /**
     * 发送表单类型的 post 请求。带请求参数
     *
     * @param url   请求地址
     * @param param 参数，将会被序列化为 json 字符串
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByJson(String url, Object param) throws IOException {
        return doPostByJson(url, null, param);
    }

    /**
     * 发送表单类型的 post 请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param param   参数，将会被序列化为 json 字符串
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPostByJson(String url, Map<String, String> headers, Object param) throws IOException {
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        // 请求参数
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(param), ContentType.APPLICATION_JSON));
        // 设置请求头
        this.packageHeader(headers, httpPost);
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行请求并获得响应结果
        return this.getHttpClientResult(httpClient, httpPost);
    }

    /**
     * 发送 put 请求。不带请求参数
     *
     * @param url 请求地址
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPut(String url) throws IOException {
        return doPut(url, null);
    }

    /**
     * 发送 put 请求。带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doPut(String url, Map<String, String> params) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);
        this.packageParam(params, httpPut);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return this.getHttpClientResult(httpClient, httpPut);
    }

    /**
     * 发送 delete 请求.不带请求参数
     *
     * @param url 请求地址
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doDelete(String url) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(requestConfig);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return this.getHttpClientResult(httpClient, httpDelete);
    }

    /**
     * 发送 delete 请求。带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return {@link HttpClientResult}
     */
    public HttpClientResult doDelete(String url, Map<String, String> params) throws IOException {
        if (params == null) {
            params = new HashMap<>(1);
        }
        params.put("_method", "delete");
        return doPostByForm(url, params);
    }

    // --------------------------------------------- PRIVATE METHOD ---------------------------------------------

    /**
     * 封装表单格式的请求参数
     *
     * @param params     请求参数
     * @param httpMethod 请求方法
     */
    private void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            // 设置到请求的 http 对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * 封装请求头
     *
     * @param params     请求头参数
     * @param httpMethod 请求方法
     */
    private void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                // 设置到请求头到 HttpRequestBase 对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获得响应结果
     *
     * @param httpClient {@link CloseableHttpClient}
     * @param httpMethod 请求方法
     * @return {@link HttpClientResult}
     */
    private HttpClientResult getHttpClientResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws IOException {
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpMethod);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                String content = "";
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
                return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
            }
            return new HttpClientResult(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            this.release(httpResponse, httpClient);
        }
    }


    /**
     * 释放资源
     *
     * @param httpResponse {@link CloseableHttpResponse}
     * @param httpClient   {@link CloseableHttpClient}
     */
    private void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }
}
