package ru.job4j.modelcache;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.atomic.AtomicReference;

public class BasesCacheStorageTest {
    @Test
    public void updateModelTwoThreadsSameTimeShouldThrowException() throws InterruptedException {
        AtomicReference<Exception> ex = new AtomicReference<>();
        BasesCacheStorage bcs = new BasesCacheStorage();
        Base base = new Base(1);
        bcs.add(base);
        Thread thread1 = new Thread(
                () -> {
                    try {
                        bcs.update(new Base(1));
                    } catch (Exception e) {
                        ex.set(e);
                    }
                }
        );
        Thread thread2 = new Thread(
                () -> {
                    try {
                        bcs.update(new Base(1));
                    } catch (Exception e) {
                        ex.set(e);
                    }
                }
        );
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Assert.assertThat(ex.get().getMessage(), is("This version already exist!"));
    }
}
