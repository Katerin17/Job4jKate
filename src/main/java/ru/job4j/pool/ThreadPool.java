package ru.job4j.pool;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(2);

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() || tasks.getQueueSize() != 0) {
                    try {
                        Thread t = new Thread(tasks.poll());
                        t.start();
                        System.out.println("The work done by " + Thread.currentThread().getName());
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    public int getTasksSize() {
        return tasks.getQueueSize();
    }

     public void start() {
         for (Thread t: threads) {
             t.start();
         }
     }

    public void work(Runnable job) {
        tasks.offer(job);
    }

    public void shutdown() {
        for (Thread t : threads) {
           t.interrupt();
        }
    }
}
