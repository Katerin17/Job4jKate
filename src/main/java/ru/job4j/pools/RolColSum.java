package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private int index;
        private int rowSum;
        private int colSum;

        public Sums(int index) {
            this.index = index;
        }

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] totalRowCol = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int sumRow = 0;
            int sumCol = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                try {
                    sumRow += matrix[i][j];
                    sumCol += matrix[j][i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
            Sums s = new Sums(i);
            s.setRowSum(sumRow);
            s.setColSum(sumCol);
            totalRowCol[i] = s;
        }
        return totalRowCol;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] totalRowCol = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> map = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            map.put(i, getSum(matrix, i));
        }
        for (Integer key : map.keySet()) {
            totalRowCol[key] = map.get(key).get();
        }
        return totalRowCol;
    }

    public static CompletableFuture<Sums> getSum(int[][] data, int rowCol) {
        return CompletableFuture.supplyAsync(() -> {
            int sumRow = 0;
            int sumCol = 0;
            for (int i = 0; i < data[0].length; i++) {
                try {
                    sumRow += data[rowCol][i];
                    sumCol += data[i][rowCol];
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
            Sums s = new Sums(rowCol);
            s.setRowSum(sumRow);
            s.setColSum(sumCol);
            return s;
        });
    }
}
