package ru.job4j.userstorage;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class UserStorage {

    private final Map<Integer, Integer> users = new HashMap<>();

    public synchronized boolean add(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user.getAmount());
            return true;
        }
        return false;
    }

    public synchronized boolean update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user.getAmount());
            return true;
        }
        return false;
    }

    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user.getAmount());
    }

    public synchronized void transfer(int fromId, int toId, int amount) {
        if (users.containsKey(fromId) && users.containsKey(toId)) {
            if (users.get(fromId) >= amount) {
                users.merge(fromId, amount, (oldVal, newVal) -> oldVal - newVal);
                users.merge(toId, amount, Integer::sum);
            }
        }
    }

    public static void main(String[] args) {
        UserStorage stoge = new UserStorage();

        stoge.add(new User(1, 100));
        stoge.add(new User(2, 200));

        stoge.transfer(1, 2, 50);
        System.out.println(stoge.users.toString());
    }
}
