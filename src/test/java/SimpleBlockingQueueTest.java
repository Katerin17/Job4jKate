import org.junit.Test;
import ru.job4j.SimpleBlockingQueue;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {
    @Test
    public void queueShouldBeEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> sbq = new SimpleBlockingQueue<>(5);
        Thread first = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sbq.offer(i);
            }
        });
        Thread second = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    sbq.poll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(sbq.getQueueSize(), 0);
    }

    @Test
    public void allDataShouldBeFetch() throws InterruptedException {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        SimpleBlockingQueue<Integer> sbq = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                sbq.offer(i);
            }
        });
        Thread consumer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() || sbq.getQueueSize() != 0) {
                try {
                    list.add(sbq.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(list, is(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
