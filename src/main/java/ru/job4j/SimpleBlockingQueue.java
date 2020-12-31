package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final int size;

    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized Queue<T> getQueue() {
        return queue;
    }

    public void offer(T value) {
        synchronized (this) {
            while (queue.size() == size) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.offer(value);
            this.notify();
        }
    }

    public T poll() {
        T element;
        synchronized (this) {
            while (queue.size() == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            element = queue.poll();
            this.notify();
        }
        return element;
    }
}
