package org.lucius.components.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 连接超时，单位：毫秒
     */
    private static final int CONNECT_TIME_OUT = 3000;

    /**
     * 读超时，单位：毫秒
     */
    private static final int SOCKET_TIME_OUT = 5000;

    /**
     * 重试次数
     */
    private static final int RETRY_TIMES = 3;

    /**
     * 连接池大小
     */
    private static final int MAX_TOTAL = 500;

    /**
     * 每个路由最大连接数
     */
    private static final int MAX_PER_ROUTE = 100;

    /**
     * 可用空闲连接过期时间，重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     */
    private static final int VALIDATE_AFTER_INACTIVITY = 1000;

    private static final String EMPTY_STRING = "";
    private static final String SYMBOL_MARK = "?";
    private static final String SYMBOL_CONNECTOR = "&";
    private static final String SYMBOL_EQUAL = "=";

    private static Executor executor;

    private static RequestConfig requestConfig;

    private static PoolingHttpClientConnectionManager cm;
    private static SSLConnectionSocketFactory socketFactory;
    private static CloseableHttpClient httpClient;
    private static Map<String, String> defaultHeaders;
    /**
     * 初始化HttpClient和connectionManager配置
     */
    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(CONNECT_TIME_OUT)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setRedirectsEnabled(false).setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(
                        Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        if (cm == null) {
            ignoreVerifySSL();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
            cm.setMaxTotal(MAX_TOTAL);
            cm.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
        }
        reSetHeader();
        httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm)
                .setRetryHandler(
                        new DefaultHttpRequestRetryHandler(RETRY_TIMES, true))
                .build();
        executor = Executor.newInstance(httpClient);
    }

    public static void reSetHeader() {
        defaultHeaders = new HashMap<>();
        defaultHeaders.put("Accept",
                "text/html,application/xhtml+xml,application/xml;application/json;q=0.9,image/webp,*/*;q=0.8");
        defaultHeaders.put("Accept-Encoding", "gzip, deflate, sdch, br");
        defaultHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
        defaultHeaders.put("Upgrade-Insecure-Requests", "1");
        defaultHeaders.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    }

    /**
     * 忽略证书
     */
    private static void ignoreVerifySSL() {
        try {
            TrustManager manager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] x509Certificates, String s)
                        throws CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] x509Certificates, String s)
                        throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext context = SSLContext.getInstance("TLS");// SSLv3
            context.init(null, new TrustManager[] { manager }, null);
            socketFactory = new SSLConnectionSocketFactory(context,
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static String post(String url, Map<String, String> header,
            Map<String, String> params) {
        List<Header> headers = new ArrayList<>();
        if (Objects.nonNull(header) && header.size() > 0) {
            header.entrySet().forEach(entry -> headers
                    .add(new BasicHeader(entry.getKey(), entry.getValue())));
        }
        return execute(
                Request.Post(url).bodyForm(map2List(params), Consts.UTF_8)
                        .setHeaders(headers.toArray(new Header[] {})));
    }

    public static String post(String url, Map<String, String> params) {
        return execute(
                Request.Post(url).bodyForm(map2List(params), Consts.UTF_8));
    }

    public static String post(String url) {
        return execute(Request.Post(url));
    }

    public static String get(String url, Map<String, String> header,
            Map<String, String> params) {
        List<Header> headers = new ArrayList<>();
        if (Objects.nonNull(header) && header.size() > 0) {
            header.entrySet().forEach(entry -> headers
                    .add(new BasicHeader(entry.getKey(), entry.getValue())));
        }
        return execute(Request.Get(parse(url, params))
                .setHeaders(headers.toArray(new Header[] {})));
    }

    public static String get(String url, Map<String, String> params) {
        return execute(Request.Get(parse(url, params)));
    }

    public static String get(String url) {
        return execute(Request.Get(url));
    }

    public static String postJson(String url, String json) {
        StringEntity se = new StringEntity(json,Consts.UTF_8);
        se.setContentEncoding("UTF-8");  
        se.setContentType("application/json");  
        return execute(Request.Post(url).addHeader(HTTP.CONTENT_TYPE,"application/json").body(se));
    }
    
    public static String postMultiple(String url, byte[] fb,Map<String, String> params) throws Exception {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if(params != null && params.size() > 0){
            params.entrySet().forEach(entry -> builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA)));
        }
        builder.addPart("file", new ByteArrayBody(fb, "fileName"));
        HttpEntity entity = builder.build();
        return execute(Request.Post(url).body(entity));
    }
    
    /**
     * Url追加参数
     */
    private static String parse(String url, Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url);
        builder.append(SYMBOL_MARK);
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            try {
                builder.append(key).append(SYMBOL_EQUAL)
                        .append(URLEncoder.encode(params.get(key) == null
                                ? EMPTY_STRING : params.get(key),
                                Consts.UTF_8.name()))
                        .append(SYMBOL_CONNECTOR);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Parse Url error", e);
                throw new RuntimeException(e);
            }
        }
        String parse = builder.toString();
        return parse.substring(0, parse.length() - 1);
    }

    /**
     * 参数转换
     */
    private static List<NameValuePair> map2List(Map<String, String> params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String name = it.next();
                list.add(new BasicNameValuePair(name, params.get(name) == null
                        ? EMPTY_STRING : params.get(name)));
            }
        }
        return list;
    }

    /**
     * 执行request
     *
     * @param request
     *            Request
     * @return String
     */
    public static String execute(Request request) {
        try {
            return executor.execute(request).returnContent()
                    .asString(Consts.UTF_8);
        } catch (Exception e) {
            LOGGER.error("Execute request error", e);
        }
        return null;
    }

}
