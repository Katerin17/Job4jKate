package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress.interrupt();
    }

    @Override
    public void run() {
        String[] process = {"_", "_\\", "_\\|", "_\\|/", "_\\|/_", ""};
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
                System.out.print("\r load: " + process[i++]);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("\nLoading is interrupted!");
            }
            if (i == 6) {
                i = 0;
            }
        }
    }
}
