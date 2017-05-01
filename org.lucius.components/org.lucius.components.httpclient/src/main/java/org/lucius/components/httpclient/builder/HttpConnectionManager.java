/**
 * @(#)HttpConnectionManager.java 1.0 2017年4月27日
 * @Copyright:  Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        2017年4月27日
 * Author:      lucius.lv
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package org.lucius.components.httpclient.builder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.util.EntityUtils;

public class HttpConnectionManager {
    
    private static PoolingHttpClientConnectionManager cm = null;
    
    private static final SimpleSSLVerifier verifier = new SimpleSSLVerifier();
    
    private static SSLSocketFactory sslFactory ;
    private static SSLConnectionSocketFactory sslConnFactory ;
    private static SSLIOSessionStrategy sslIOSessionStrategy ;
    

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { verifier }, new java.security.SecureRandom());
        sslConnFactory = new SSLConnectionSocketFactory(sc, verifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("https", sslConnFactory)
                .register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
    }

    public static CloseableHttpClient getPooledHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm).build();
        return httpClient;
    }
    
    public static CloseableHttpClient getHttpClient() {

        CloseableHttpClient hc = null;
        try {
            
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { verifier }, null);
            sslFactory = sc.getSocketFactory();
            
            hc = HttpClients.createDefault();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hc;
    
    }
    
    
    // 重写X509TrustManager类的三个方法,信任服务器证书
    // 不验证证书，不验证主机名
    private static class SimpleSSLVerifier implements  X509TrustManager, HostnameVerifier{
        
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
        
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                String authType) throws java.security.cert.CertificateException {
        }
        
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public boolean verify(String paramString, SSLSession paramSSLSession) {
            return true;
        }
    };
    
    public static void main(String[] args) throws InterruptedException {
        
        long start = System.currentTimeMillis();
        
        int times = 1000;
        ExecutorService executors = Executors.newFixedThreadPool(20);
        CountDownLatch cdl = new CountDownLatch(times);
        for(int i = 0 ; i < times ; i ++){
            CloseableHttpClient hc = getPooledHttpClient();
            executors.execute(new DoGetRunnable(hc, cdl));
        }
        cdl.await();
        long end = System.currentTimeMillis();
        System.out.println("执行时间 ： " + (end - start) + "毫秒");
        executors.shutdown();
        
    }
    
}
class DoGetRunnable implements Runnable {
    
    CloseableHttpClient hc;
    CountDownLatch cdl;
    
    public DoGetRunnable(CloseableHttpClient hc,CountDownLatch cdl) {
        super();
        this.hc = hc;
        this.cdl = cdl;
    }



    @Override
    public void run() {
        HttpGet hg = new HttpGet("http://www.baidu.com/");
        CloseableHttpResponse hr = null;
        try {
            hr = hc.execute(hg);
            HttpEntity he = hr.getEntity();
            String x = EntityUtils.toString(he, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            cdl.countDown();
        }
    }
    
}