package ru.job4j.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService pool;

    public EmailNotification() {
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void addTask(Runnable runnable) {
        pool.submit(runnable);
    }

    public void emailTo(User user) {
        String subject = "Notification " + user.getUserName() + " to email " + user.getEmail();
        String body = "Add a new event to " + user.getUserName();
        send(subject, body, user.getEmail());
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String subject, String body, String email) {
        System.out.println("Message sent");
    }
}
