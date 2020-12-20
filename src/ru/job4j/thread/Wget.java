package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream buff = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fos = new FileOutputStream("test_temp.txt")) {
            byte[] arr = new byte[1024];
            int byteReads;
            while (true) {
                int start = LocalTime.now().get(ChronoField.MILLI_OF_DAY);
                byteReads = buff.read(arr, 0, 1024);
                if (byteReads != -1)
                    fos.write(arr, 0, byteReads);
                else break;
                int finish = LocalTime.now().get(ChronoField.MILLI_OF_DAY);
                Thread.sleep(speed - (finish - start));
                System.out.println("1");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
