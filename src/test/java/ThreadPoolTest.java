import org.junit.Test;
import ru.job4j.pool.ThreadPool;

import static org.junit.Assert.*;

public class ThreadPoolTest {
    @Test
    public void tasksShouldBeEmpty() {
        ThreadPool pool = new ThreadPool();
        pool.start();
        for (int i = 0; i < 20; i++) {
            pool.work(
                    () -> System.out.println("End of thread " + Thread.currentThread().getName()));
        }
        pool.shutdown();
        assertEquals(0, pool.getTasksSize());
    }
}
