package camcorders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

@SuppressWarnings("checkstyle:LineLength")
public class HttpCamerasRequest {
    private static int size = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(size);
    private static String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    private static HttpClient httpClient = HttpClient.newBuilder()
            .executor(executor)
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String getResponse(String url) throws ExecutionException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", agent)
                .build();
        CompletableFuture<HttpResponse<String>> result = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.get().body();
    }

    static class Aggregate extends RecursiveTask<Camera[]> {
        private Camera[] arr;
        private int from;
        private int to;

        public Aggregate(Camera[] arr, int from, int to) {
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        public Camera[] aggregate() {
            ObjectMapper mapper = new ObjectMapper();
            for (int i = from; i <= to; i++) {
                try {
                    Camera c = arr[i];
                    JsonNode nodeA = mapper.readTree(getResponse(c.getSourceDataUrl()));
                    c.setUrlType(nodeA.path("urlType").asText());
                    c.setVideoUrl(nodeA.path("videoUrl").asText());
                    JsonNode nodeB = mapper.readTree(getResponse(c.getTokenDataUrl()));
                    c.setValue(nodeB.path("value").asText());
                    c.setTtl(nodeB.path("ttl").asInt());
                } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return arr;
        }

        @Override
        protected Camera[] compute() {
            if (to - from <= 2) {
                return aggregate();
            }
            int mid = (from + to) / 2;
            Aggregate leftAggreg = new Aggregate(arr, from, mid);
            Aggregate rightAggreg = new Aggregate(arr, mid + 1, to);
            leftAggreg.fork();
            rightAggreg.fork();
            Camera[] left = leftAggreg.join();
            Camera[] right = rightAggreg.join();
            return ArrayUtils.addAll(left, right);
        }

        public static String camerasAggregated(String url) {
            ObjectMapper mapper = new ObjectMapper();
            String s = null;
            try {
                Camera[] arr = mapper.readValue(getResponse(url), Camera[].class);
                ForkJoinPool fjp = new ForkJoinPool(size);
                fjp.invoke(new Aggregate(arr, 0, arr.length - 1));
                s = mapper.writeValueAsString(arr);
            } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return s;
        }
    }

    public static void main(String[] args) {
        System.out.println(Aggregate.camerasAggregated("http://www.mocky.io/v2/5c51b9dd3400003252129fb5"));
        }
}

