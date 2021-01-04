package ru.job4j.modelcache;

import java.util.concurrent.ConcurrentHashMap;

public class BasesCacheStorage {
    private ConcurrentHashMap<Integer, Base> map = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        if (!map.containsKey(model.getId())) {
            map.put(model.getId(), model);
            return true;
        }
        return false;
    }

    public void update(Base model) {
        map.computeIfPresent(model.getId(), (id, base) -> {
            int version = model.getVersion();
            if (base.getVersion() == version) {
                model.setVersion((++version));
                return model;
            } else {
                throw new OptimisticException("This version already exist!");
            }
        });
    }

    public boolean delete(Base model) {
        return map.remove(model.getId(), model);
    }
}
