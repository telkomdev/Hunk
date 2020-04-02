# Hunk

Java 11 Http client with simple taste

[![Maintainability](https://api.codeclimate.com/v1/badges/5c760b0d5da52b887a73/maintainability)](https://codeclimate.com/github/telkomdev/Hunk/maintainability)

### Usage

POST
```java
public class App {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {

        Map<String, String> headers = new HashMap<>();
        headers.put(Request.CONTENT_TYPE, "application/json");

        Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");

        Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.POST,
                "https://jsonplaceholder.typicode.com/posts",
                headers, Request.ofJsonObject(p), 2);

        HttpResponse<byte[]> response = future.get();
        HttpHeaders respHeaders = response.headers();

        respHeaders.map().forEach((key, values) -> {
            System.out.println(key);
            System.out.println(values);
        });

        Post post = JsonUtil.jsonToData(Post.class, response.body());

        System.out.println(post);


    }
}
```

GET
```java
public class App {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {

        Map<String, String> headers = new HashMap<>();
        headers.put(Request.CONTENT_TYPE, "application/json");
        headers.put(Request.ACCEPT, "application/json");

        Future<HttpResponse<byte[]>> future = Request.doAsync(Request.HttpMethod.GET,
                "https://jsonplaceholder.typicode.com/posts/1",
                headers, null, 2);

        HttpResponse<byte[]> response = future.get();
        HttpHeaders respHeaders = response.headers();

        respHeaders.map().forEach((key, values) -> {
            System.out.println(key);
            System.out.println(values);
        });

        Post post = JsonUtil.jsonToData(Post.class, response.body());

        System.out.println(post);


    }
}
```