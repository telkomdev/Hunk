# Hunk

Java 11 Http client with simple taste

[![Maintainability](https://api.codeclimate.com/v1/badges/5c760b0d5da52b887a73/maintainability)](https://codeclimate.com/github/telkomdev/Hunk/maintainability)

### Usage

The Post `class`
```java
public class Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String body;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
```

POST
```java
public class App {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, URISyntaxException {

        Map<String, String> headers = new HashMap<>();
        headers.put(Hunk.CONTENT_TYPE, "application/json");

        Post p = new Post(1, 1, "Java 11", "The awesome of Java 11");

        Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.POST,
                "https://jsonplaceholder.typicode.com/posts",
                headers, Hunk.ofJsonObject(p), 2);

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
        headers.put(Hunk.CONTENT_TYPE, "application/json");
        headers.put(Hunk.ACCEPT, "application/json");

        Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.GET,
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

GET from string

```java
public class App {

    public static void main(String[] args) throws HunkMethodNotSupportedException,
            URISyntaxException, IOException, ExecutionException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put(Hunk.CONTENT_TYPE, "application/json");
        headers.put(Hunk.ACCEPT, "application/json");

        Future<HttpResponse<byte[]>> future = Hunk.doAsync(Hunk.HttpMethod.from("get"),
                "https://jsonplaceholder.typicode.com/posts/1",
                headers, null, 2);

        HttpResponse<byte[]> response = future.get();
        HttpHeaders respHeaders = response.headers();

        respHeaders.map().forEach((key, values) -> {
            System.out.println(key);
            System.out.println(values);
        });

        Post post = JsonUtil.jsonToData(Post.class, response.body());

        System.out.println(post.getTitle());
        System.out.println(post.getBody());


    }
}
```

#### Reactive feature with RxJava

```java
public class Main {

    public static void main(String[] args) throws HunkMethodNotSupportedException,
            URISyntaxException, IOException, ExecutionException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put(Hunk.CONTENT_TYPE, "application/json");

        Observable<HttpResponse<byte[]>> resultObserver = Hunk.doAsyncReactive(Hunk.HttpMethod.from("get"),
                "https://jsonplaceholder.typicode.com/posts",
                headers, null, 0);

        resultObserver.subscribe(response -> {
            HttpHeaders respHeaders = response.headers();

            respHeaders.map().forEach((key, values) -> {
                System.out.println(key);
                System.out.println(values);
            });

            List<Post> posts = Arrays.asList(JsonUtil.jsonToData(Post[].class, response.body()));

            System.out.println(response.statusCode());
            for (Post post : posts) {
                System.out.println(post.getTitle());
            }
        }, throwable -> System.out.println(throwable.getMessage()));


    }
}
```