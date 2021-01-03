package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount<T> {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        int old;
        do {
            old = count.get();
        }
        while (!count.compareAndSet(old, old + 1));
    }

    public int get() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        CASCount<Integer> cas = new CASCount<>();
        Thread first = new Thread(() -> {
            cas.increment();
            cas.increment();
        });
        Thread second = new Thread(() -> {
            cas.increment();
            cas.increment();
        });
        first.start();
        second.start();
        first.join();
        second.join();
        cas.increment();
        cas.increment();

        System.out.println(cas.get());
    }
}
