package ru.job4j.modelcache;

public class Base {
    private int id;
    private int version;

    public Base(int id) {
        this.id = id;
        this.version = 1;
    }

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
