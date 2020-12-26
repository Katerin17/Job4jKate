package ru.job4j.userstorage;

import net.jcip.annotations.GuardedBy;

public class User {
    private final int id;
    @GuardedBy("this")
    private int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public synchronized int getAmount() {
        return amount;
    }
}
