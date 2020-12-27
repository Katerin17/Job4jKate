package ru.job4j.synch;

import java.util.*;

public class SimpleArray<T> implements Iterable<T> {
    private transient Object[] container = new Object[10];
    private int size;
    private int modCount;
    private int point = 0;

    public int getSize() {
        return size;
    }

    public void grow(Object[] array) {
        Arrays.copyOf(array, (int) ((array.length * 1.5) + 1));
    }

    public T get(int index) {
        Objects.checkIndex(index, size);
        return (T) container[index];
    }

    public void add(T model) {
        if (size == container.length) {
            grow(container);
        }
        container[size++] = model;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() {
        int expectedModCount = modCount;

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                return point < container.length && point != size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) container[point++];
            }
        };
    }
}
