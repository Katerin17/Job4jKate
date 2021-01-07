package ru.job4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinIndexFound extends RecursiveTask<Integer> {
    private int[] arr;
    private int value;
    private int from;
    private int to;

    public ForkJoinIndexFound(int[] arr, int value, int from, int to) {
        this.arr = arr;
        this.value = value;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return searchIndex();
        }
        int mid = (from + to) / 2;
        ForkJoinIndexFound leftSearch = new ForkJoinIndexFound(arr, value, from, mid);
        ForkJoinIndexFound rightSearch = new ForkJoinIndexFound(arr, value, mid + 1, to);
        leftSearch.fork();
        rightSearch.fork();
        int left = leftSearch.join();
        int right = rightSearch.join();
        return Math.max(left, right);
    }

    public static int search(int[] arr, int value) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ForkJoinIndexFound(arr, value, 0, arr.length - 1));
    }

    private int searchIndex() {
        for (int i = from; i < to; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
