package com.telkomdev.hunk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

// Author Wuriyanto

/**
 * Important notes:
 * This Class required JDK 11 API or Higher
 */
public class Request {

    /**
     * If you need to add more http headers
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers
     */
    public static final String ACCEPT = "Accept";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String USER_AGENT = "User-Agent";

    /**
     * http method
     * https://developer.mozilla.org/id/docs/Web/HTTP/Methods
     */
    public enum HttpMethod {
        CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE
    }

    /**
     * @param method
     * @param url
     * @param headers
     * @param data
     * @param connectTimeout
     * @return
     * @throws URISyntaxException Usage: PUT
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Request.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.PUT,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Request.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            <p>
     *                            Usage: POST
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Request.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.POST,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Request.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: DELETE
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Request.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.DELETE,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, null, 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: GET
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Request.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.GET,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, null, 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: PATCH
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Request.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.PATCH,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Request.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     */
    public static Future<HttpResponse<byte[]>> doAsync(HttpMethod method,
                                                       String url,
                                                       Map<String, String> headers,
                                                       HttpRequest.BodyPublisher data,
                                                       long connectTimeout) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = getHttpRequestBuilder(method, url, headers, data);

        HttpRequest request = requestBuilder.build();

        CompletableFuture<HttpResponse<byte[]>> future = sendAsync(request, connectTimeout);
        return future;
    }

    /**
     * @param method
     * @param url
     * @param headers
     * @param data
     * @param connectTimeout
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static HttpResponse<byte[]> doSync(HttpMethod method,
                                              String url,
                                              Map<String, String> headers,
                                              HttpRequest.BodyPublisher data,
                                              long connectTimeout) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = getHttpRequestBuilder(method, url, headers, data);

        HttpRequest request = requestBuilder.build();

        return sendSync(request, connectTimeout);
    }

    /**
     * @param url
     * @param headers
     * @return
     * @throws URISyntaxException
     */
    private static HttpRequest.Builder getHttpRequestBuilder(HttpMethod method,
                                                             String url,
                                                             Map<String, String> headers,
                                                             HttpRequest.BodyPublisher data) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        switch (method) {
            case CONNECT:
                requestBuilder.method(HttpMethod.CONNECT.name(), data);
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
            case GET:
                requestBuilder.GET();
                break;
            case HEAD:
                requestBuilder.method(HttpMethod.HEAD.name(), HttpRequest.BodyPublishers.noBody());
                break;
            case OPTIONS:
                requestBuilder.method(HttpMethod.OPTIONS.name(), data);
                break;
            case PATCH:
                requestBuilder.method(HttpMethod.PATCH.name(), data);
                break;
            case POST:
                requestBuilder.POST(data);
                break;
            case PUT:
                requestBuilder.PUT(data);
                break;
            case TRACE:
                requestBuilder.method(HttpMethod.TRACE.name(), data);
                break;
        }

        if (headers != null) {
            headers.forEach((k, v) -> {
                requestBuilder.header(k, v);
            });
        }

        requestBuilder.uri(new URI(url));
        // do we need timeout for waiting the response?
        //requestBuilder.timeout(Duration.of(timeout, ChronoUnit.SECONDS));
        return requestBuilder;
    }

    /**
     * @param request
     * @return
     */
    private static CompletableFuture<HttpResponse<byte[]>> sendAsync(HttpRequest request, Long connectTimeout) {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder();

        if (connectTimeout != 0) {
            clientBuilder.connectTimeout(Duration.of(connectTimeout, ChronoUnit.SECONDS));
        }

        clientBuilder.followRedirects(HttpClient.Redirect.ALWAYS);
        clientBuilder.proxy(ProxySelector.getDefault());
        HttpClient client = clientBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static HttpResponse<byte[]> sendSync(HttpRequest request, Long connectTimeout) throws IOException, InterruptedException {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder();

        if (connectTimeout != 0) {
            clientBuilder.connectTimeout(Duration.of(connectTimeout, ChronoUnit.SECONDS));
        }

        clientBuilder.followRedirects(HttpClient.Redirect.ALWAYS);
        clientBuilder.proxy(ProxySelector.getDefault());
        HttpClient client = clientBuilder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * @param data
     * @return
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    public static HttpRequest.BodyPublisher ofByteArray(byte[] data) throws URISyntaxException, FileNotFoundException {
        return HttpRequest.BodyPublishers.ofByteArray(data);
    }

    /**
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    /**
     * @param path
     * @return
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    public static HttpRequest.BodyPublisher ofFile(String path) throws URISyntaxException, FileNotFoundException {
        return HttpRequest.BodyPublishers.ofFile(Paths.get(new URI(path)));
    }

    /**
     * @param data
     * @param <I>
     * @return
     */
    public static <I> HttpRequest.BodyPublisher ofJsonObject(I data) {
        String body = JsonUtil.dataToJson(data);
        return HttpRequest.BodyPublishers.ofString(body);
    }
}
