

package code.traveler.test.space.java.sb.elasticsearch.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class RestClientUtil {
    public static String sendPost(final String url, final String content) {

        return sendPost(url, content, null);
    }

    /**
     * @param url
     * @return java.lang.String
     * @description GET请求-不带header
     * @author panda
     * @time 2020/11/26 16:35
     */
    public static String sendGet(final String url) {

        return send(RequestMethod.GET, url, null, null);
    }

    public static String sendGet(final String url, Map<String, String> header) {

        return send(RequestMethod.GET, url, null, header);
    }

    public static String sendDelete(final String url, final String content, final Map<String, String> header) {
        return send(RequestMethod.DELETE, url, content, header);
    }


    public static String sendPut(final String url, final String content, final Map<String, String> header) {
        return send(RequestMethod.PUT, url, content, header);
    }

    public static String sendPost(final String url, final String content, final Map<String, String> header) {

        final StringBuilder result = new StringBuilder("");
        try {
            final URL connect = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) connect.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            if (Objects.nonNull(header)&&!header.isEmpty()) {
                header.forEach((k, v) -> connection.setRequestProperty(k, v));
            }
            if (!StringUtils.isEmpty(content)) {
                try (final OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                } catch (final Exception e) {
                    connection.disconnect();
                    throw new Exception("写入POST请求体参数失败");
                }
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                connection.disconnect();
                throw new Exception("POST请求失败, 状态码: " + connection.getResponseCode());
            }
            try (final BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((connection.getInputStream())))) {
                String output;
                while ((output = responseBuffer.readLine()) != null) {
                    result.append(output);
                }
            } finally {
                connection.disconnect();
            }
        } catch (final Exception e) {
            log.error("POST请求异常：{}", e);
//            throw new ParameterException("POST请求异常");
        }
        return result.toString();
    }

    /**
     * @param requestMethod
     * @param url
     * @param content
     * @param header
     * @return java.lang.String
     * @description 请求调用
     * @author panda
     * @time 2020/11/26 16:23
     */
    private static String send(final RequestMethod requestMethod, final String url, final String content,
                               final Map<String, String> header) {

        final StringBuilder result = new StringBuilder("");
        try {
            final URL targetUrl = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();//新建连接实例
            connection.setDoOutput(true);//是否打开输出流
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestMethod(requestMethod.name());
            connection.setConnectTimeout(10000);//设置连接超时时间【单位：毫秒】
            connection.setReadTimeout(10000);//设置读取数据超时时间【单位：毫秒】
            if (Objects.nonNull(header)&&!header.isEmpty()) {
                header.forEach((k, v) -> connection.setRequestProperty(k, v));
            }
            if (!StringUtils.isEmpty(content)) {
                try (final OutputStream outputStream = connection.getOutputStream()) {// 输出完成后刷新
                    outputStream.write(content.getBytes());//发送请求参数即数据
                    outputStream.flush();// 输出完成后刷新
                } catch (final Exception e) {
                    log.error("method={},url={},content={},header={}请求体参数写入异常：{}，{}", requestMethod.name(), url,
                            content, header, e.getMessage(), e);
                    connection.disconnect();
                    throw new Exception("写入" + requestMethod.name() + "请求体参数失败");
                }
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {//真正发起调用的是触发了getInputStream()
                // ，getResponseCode()内部有这个方法的调用，所有调用初始化参数需在getInputStream()之前
                try (final BufferedReader responseBuffer = new BufferedReader(
                        new InputStreamReader((connection.getInputStream())))) {//发送报文，得到服务器返回的输入流
                    String output;
                    while ((output = responseBuffer.readLine()) != null) {
                        result.append(output);
                    }
                } finally {
                    connection.disconnect();
                }
            } else {//请求失败
                try (final BufferedReader responseBuffer = new BufferedReader(
                        new InputStreamReader((connection.getErrorStream())))) {
                    String output;
                    while ((output = responseBuffer.readLine()) != null) {
                        result.append(output);
                    }
                } finally {
                    connection.disconnect();
                }

                log.error("{}请求失败, 状态码:{}，请求参数：{}，请求头：{}，返回结果：{}", requestMethod.name(), connection.getResponseCode(),
                        content, JsonUtil.toJson(header), result);
                throw new Exception(requestMethod.name() + "请求失败, 状态码: " + connection.getResponseCode());
            }
        } catch (final Exception e) {
            log.error("method={}，url={}，content={}，header={}调用异常：{}，{}", requestMethod.name(), url, content, header,
                    e.getMessage(), e);
//            throw new Exception("调用外部系统接口异常");
        }
        return result.toString();
    }

    public static String doGet(final String url) {
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(url);
        try {
            //这里可以设置请求参数，token等
            get.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            final HttpResponse response = httpClient.execute(get);//执行获取响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//根据状态码处理
                //返回字符串
                final String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
