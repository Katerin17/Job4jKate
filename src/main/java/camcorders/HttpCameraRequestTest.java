package camcorders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("checkstyle:LineLength")
public class HttpCameraRequestTest {

    @Test
    public void dataShouldBeMatch() {
        Camera expected = new Camera(1, "LIVE", "rtsp://127.0.0.1/1", "fa4b588e-249b-11e9-ab14-d663bd873d93", 120);
        expected.setTokenDataUrl("http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s");
        expected.setSourceDataUrl("http://www.mocky.io/v2/5c51b230340000094f129f5d");
        HttpCamerasRequest httpCamerasRequest = new HttpCamerasRequest();
        HttpCamerasRequest.Aggregate aggregate = httpCamerasRequest.new Aggregate(expected);
        Camera result = aggregate.call();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void fieldsShouldNotBeNull() {
        Camera cam = new Camera();
        cam.setId(20);
        cam.setTokenDataUrl("http://www.mocky.io/v2/5c51b5ed340000554e129f7e");
        cam.setSourceDataUrl("http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms");
        HttpCamerasRequest httpCamerasRequest = new HttpCamerasRequest();
        HttpCamerasRequest.Aggregate aggregate = httpCamerasRequest.new Aggregate(cam);
        Camera result = aggregate.call();
        Assert.assertNotNull(result.getUrlType());
        Assert.assertNotNull(result.getValue());
    }

    @Test
    public void arrayLengthShouldBeFour() throws ExecutionException, InterruptedException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HttpCamerasRequest httpCamerasRequest = new HttpCamerasRequest();
        Camera[] arr = mapper.readValue(httpCamerasRequest.getResponse("http://www.mocky.io/v2/5c51b9dd3400003252129fb5"), Camera[].class);
        Assert.assertEquals(arr.length, 4);
    }
}
