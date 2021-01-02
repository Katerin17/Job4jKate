package ru.job4j.userstorage;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    public synchronized boolean add(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public synchronized boolean update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    public synchronized void transfer(int fromId, int toId, int amount) {
            User from = users.get(fromId);
            User to = users.get(toId);
            if (from != null && to != null) {
                if (from.getAmount() >= amount) {
                from.setAmount(from.getAmount() - amount);
                to.setAmount(to.getAmount() + amount);
            }
        }
    }

    public static void main(String[] args) {
        UserStorage stoge = new UserStorage();

        stoge.add(new User(1, 100));
        stoge.add(new User(2, 200));
        for (Map.Entry<Integer, User> e: stoge.users.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue() + " " + e.getValue().getAmount());
        }
        stoge.transfer(1, 2, 50);
        for (Map.Entry<Integer, User> e: stoge.users.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue() + " " + e.getValue().getAmount());
        }

    }
}
