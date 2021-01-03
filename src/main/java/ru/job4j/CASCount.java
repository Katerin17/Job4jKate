package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount<T> {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        Integer old;
        do {
            old = count.get();
        }
        while (!count.compareAndSet(old, old + 1));
    }

    public int get() {
        return count.get();
    }
}
