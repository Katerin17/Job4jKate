import org.junit.Test;
import ru.job4j.CASCount;

import static org.junit.Assert.assertEquals;

public class CASCountTest {
    @Test
    public void countShouldBe200() throws InterruptedException {
        CASCount<Integer> cas = new CASCount<>();
        Thread first = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cas.increment();
            }
        });
        Thread second = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cas.increment();
            }
        });
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(200, cas.get());
    }
}
