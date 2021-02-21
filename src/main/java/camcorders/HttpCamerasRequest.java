package camcorders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

@SuppressWarnings("checkstyle:LineLength")
public class HttpCamerasRequest {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executorCam = Executors.newFixedThreadPool(size);
    private final ExecutorService executor = Executors.newFixedThreadPool(size);
    private HttpClient httpClient = HttpClient.newBuilder()
            .executor(executor)
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String getResponse(String url) throws ExecutionException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", agent)
                .build();
        CompletableFuture<HttpResponse<String>> result = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.get().body();
    }

    public String camerasAggregated(String url) {
        String s = null;
        try {
            String jsonCam = getResponse(url);
            Camera[] arr = mapper.readValue(jsonCam, Camera[].class);
            for (Camera camera : arr) {
                FutureTask<Camera> cam = new FutureTask<>(new Aggregate(camera));
                executorCam.execute(cam);
                cam.get();
            }
            s = mapper.writeValueAsString(arr);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return s;
    }

     class Aggregate implements Callable<Camera> {
        private Camera camera;
        private ObjectMapper mapper = new ObjectMapper();

        public Aggregate(Camera camera) {
            this.camera = camera;
        }

        @Override
        public Camera call() {
            try {
                JsonNode nodeA = mapper.readTree(getResponse(camera.getSourceDataUrl()));
                camera.setUrlType(nodeA.path("urlType").asText());
                camera.setVideoUrl(nodeA.path("videoUrl").asText());
                JsonNode nodeB = mapper.readTree(getResponse(camera.getTokenDataUrl()));
                camera.setValue(nodeB.path("value").asText());
                camera.setTtl(nodeB.path("ttl").asInt());
            } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return camera;
        }
    }

    public static void main(String[] args) {
        HttpCamerasRequest httpCamerasRequest = new HttpCamerasRequest();
        System.out.println(httpCamerasRequest.camerasAggregated("http://www.mocky.io/v2/5c51b9dd3400003252129fb5"));
    }
}

