package com.telkomdev.hunk;

import io.reactivex.rxjava3.core.Observable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.function.Supplier;

// Author Wuriyanto

/**
 * Important notes:
 * This Class required JDK 11 API or Higher
 */
public final class Hunk {

    private Hunk() {

    }

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
        CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE;

        public static HttpMethod from(String method) {
            if (method.equals("connect") || method.equals("CONNECT"))
                return CONNECT;
            if (method.equals("delete") || method.equals("DELETE"))
                return DELETE;
            if (method.equals("get") || method.equals("GET"))
                return GET;
            if (method.equals("head") || method.equals("HEAD"))
                return HEAD;
            if (method.equals("options") || method.equals("OPTIONS"))
                return OPTIONS;
            if (method.equals("patch") || method.equals("PATCH"))
                return PATCH;
            if (method.equals("post") || method.equals("POST"))
                return POST;
            if (method.equals("put") || method.equals("PUT"))
                return PUT;
            if (method.equals("trace") || method.equals("TRACE"))
                return TRACE;
            return GET;
        }
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
     *                            headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.PUT,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Hunk.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            <p>
     *                            Usage: POST
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.POST,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Hunk.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: DELETE
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.DELETE,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, null, 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: GET
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.GET,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, null, 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     *                            Usage: PATCH
     *                            Map<String, String> headers = new HashMap<>();
     *                            headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                            <p>
     *                            Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");
     *                            <p>
     *                            Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.PATCH,
     *                            "https://api.mysite.com/posts/1",
     *                            headers, Hunk.ofJsonObject(p), 2);
     *                            <p>
     *                            HttpResponse<byte[]> response = future.get();
     */
    public static Future<HttpResponse<byte[]>> doAsync(HttpMethod method,
                                                       String url,
                                                       Map<String, String> headers,
                                                       HttpRequest.BodyPublisher data,
                                                       long connectTimeout) throws URISyntaxException, HunkMethodNotSupportedException {
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
     * @throws HunkMethodNotSupportedException Usage:
     *                                         Usage: GET
     *                                         Map<String, String> headers = new HashMap<>();
     *                                         headers.put(Hunk.CONTENT_TYPE, "application/json");
     *                                         <p>
     *                                         Observable<HttpResponse<byte[]>> resultObserver = Hunk.doAsyncReactive(Hunk.HttpMethod.GET,
     *                                         "https://jsonplaceholder.typicode.com/posts",
     *                                         headers, null, 0);
     *                                         <p>
     *                                         resultObserver.subscribe(response -> {
     *                                         HttpHeaders respHeaders = response.headers();
     *                                         <p>
     *                                         respHeaders.map().forEach((key, values) -> {
     *                                         System.out.println(key);
     *                                         System.out.println(values);
     *                                         });
     *                                         <p>
     *                                         List<Post> posts = Arrays.asList(JsonUtil.jsonToData(Post[].class, response.body()));
     *                                         <p>
     *                                         System.out.println(response.statusCode());
     *                                         for (Post post : posts) {
     *                                         System.out.println(post.getTitle());
     *                                         }
     *                                         }, throwable -> System.out.println(throwable.getMessage()));
     */
    public static Observable<HttpResponse<byte[]>> doAsyncReactive(HttpMethod method,
                                                                   String url,
                                                                   Map<String, String> headers,
                                                                   HttpRequest.BodyPublisher data,
                                                                   long connectTimeout) throws URISyntaxException, HunkMethodNotSupportedException {
        Future<HttpResponse<byte[]>> future = doAsync(method, url, headers, data, connectTimeout);
        return Observable.fromFuture(future);
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
                                              long connectTimeout) throws URISyntaxException, IOException, InterruptedException, HunkMethodNotSupportedException {
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
                                                             HttpRequest.BodyPublisher data) throws URISyntaxException, HunkMethodNotSupportedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        switch (method) {
            case CONNECT:
                throw new HunkMethodNotSupportedException("connect");
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
                requestBuilder.method(HttpMethod.OPTIONS.name(), HttpRequest.BodyPublishers.noBody());
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
                requestBuilder.method(HttpMethod.TRACE.name(), HttpRequest.BodyPublishers.noBody());
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
     * @param inputStream
     * @return Example:
     * Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.POST,
     * "https://jsonplaceholder.typicode.com/posts",
     * headers, Hunk.ofInputStream(() -> new ByteArrayInputStream(JsonUtil.dataToJson(p).getBytes())), 0);
     */
    public static HttpRequest.BodyPublisher ofInputStream(Supplier<InputStream> inputStream) {
        return HttpRequest.BodyPublishers.ofInputStream(inputStream);
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
