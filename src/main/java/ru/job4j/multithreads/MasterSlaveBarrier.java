package ru.job4j.multithreads;

public class MasterSlaveBarrier {
    private boolean available = true;

    public synchronized void tryMaster() {
        if (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void trySlave() {
        if (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void doneMaster() {
        available = false;
        notifyAll();
        System.out.println(Thread.currentThread().getName() + " is in runnable state.");
    }

    public synchronized void doneSlave() {
        available = true;
        notifyAll();
        System.out.println(Thread.currentThread().getName() + " is in runnable state.");
    }
}
