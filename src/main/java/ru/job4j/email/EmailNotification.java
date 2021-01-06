package ru.job4j.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final ExecutorService pool = Executors.newFixedThreadPool(size);

    public void emailTo(User user) {
        String body = "Add a new event to " + user.getUserName();
        String subject = "Notification " + user.getUserName() + "to email " + user.getEmail();
        send(subject, body, user.getEmail());
    }

    public void close() {
        pool.shutdown();
    }

    public void send(String subject, String body, String email) {
        System.out.println("Message sent");
    }

    public static void main(String[] args) {
        EmailNotification en = new EmailNotification();
        en.pool.submit(() -> en.emailTo(new User("Kate", "mail.ru")));
        en.close();
    }
}
