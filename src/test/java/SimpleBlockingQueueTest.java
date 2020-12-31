import org.junit.Test;
import ru.job4j.SimpleBlockingQueue;

import static org.junit.Assert.assertEquals;

public class SimpleBlockingQueueTest {
    @Test
    public void queueShouldBeEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> sbq = new SimpleBlockingQueue<>(2);
        Thread first = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sbq.offer(i);
            }
        });
        Thread second = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sbq.poll();
            }
        });
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(sbq.getQueue().size(), 0);

    }
}
