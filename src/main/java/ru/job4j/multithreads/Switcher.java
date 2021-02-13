package ru.job4j.multithreads;

public class Switcher {
    public static void main(String[] args) throws InterruptedException {
        MasterSlaveBarrier msb = new MasterSlaveBarrier();
        Thread first = new Thread(
                () -> {
                    while (true) {
                        msb.tryMaster();
                        System.out.println("Thread A");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        msb.doneMaster();
                    }
                }
        );
        Thread second = new Thread(
                () -> {
                    while (true) {
                        msb.trySlave();
                        System.out.println("Thread B");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        msb.doneSlave();
                    }
                }
        );
        first.start();
        second.start();
        first.join();
        second.join();
    }
}
