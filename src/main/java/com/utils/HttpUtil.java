package com.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public abstract class HttpUtil {
    private final static String DEFAFULT_CHARSET = "utf-8";
    private final static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * post请求(默认utf-8)
     *
     * @param url
     * @param json
     */
    public static boolean doPost(String url, String json) {
        return doPost(url, json, DEFAFULT_CHARSET);
    }

    /**
     * post请求(自定义编码格式)
     *
     * @param url
     * @param json
     * @param charset
     * @return
     */
    public static boolean doPost(String url, String json, String charset) {
        boolean flag = false;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            int stateCode=200;
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(json, charset);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("charset", charset);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            stateCode = response.getStatusLine().getStatusCode();
            log.info("response:" + stateCode);
            if (stateCode == HttpStatus.SC_OK) {
                flag = true;
                log.info(EntityUtils.toString(httpEntity));
            }else{
                log.info(EntityUtils.toString(httpEntity));
            }
            EntityUtils.consume(httpEntity);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return flag;
    }

    public static Optional<String> doGet(String url, Map<String, Object> params) {
        return doGet(url, DEFAFULT_CHARSET, params);
    }

    /**
     * 执行http get请求
     *
     * @param url     请求地址
     * @param charset 编码格式
     * @param params  请求参数
     * @return
     */
    public static Optional<String> doGet(String url, String charset, Map<String, Object> params) {
        String content = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            int stateCode=200;
            String parserUrl = url; //+ buildParams(params);
            HttpGet httpGet = new HttpGet(parserUrl);
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.setHeader("charset", charset);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            stateCode = response.getStatusLine().getStatusCode();
            if (stateCode == HttpStatus.SC_OK) {
                log.info(EntityUtils.toString(httpEntity));
                content = EntityUtils.toString(httpEntity);
            }else{
                log.error(EntityUtils.toString(httpEntity));
            }
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return Optional.ofNullable(content);
    }

    public static Optional<String> doPost(String url, Map<String, Object> params) {
        return doPost(url, DEFAFULT_CHARSET, params);
    }

    /**
     * 执行 http post请求
     * @param url 请求地址
     * @param charset 编码格式
     * @param params 请求参数
     * @return 返回请求结果
     */
    public static Optional<String> doPost(String url, String charset, Map<String, Object> params) {
        String content = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            int stateCode=200;
            String parserUrl = url;// + buildParams(params);
            HttpPost httpPost = new HttpPost(parserUrl);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("charset", charset);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            stateCode = response.getStatusLine().getStatusCode();
            if (stateCode == HttpStatus.SC_OK) {
                log.info(EntityUtils.toString(httpEntity));
                content = EntityUtils.toString(httpEntity);
            }else{
                log.error(EntityUtils.toString(httpEntity));
            }
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return Optional.ofNullable(content);
    }

    private static String buildParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((s, o) -> sb.append(s).append("=").append(o).append("&"));
        sb.insert(0, "?");
        sb.delete(sb.lastIndexOf("&") - 1, sb.length());
        return sb.toString();
    }
}
