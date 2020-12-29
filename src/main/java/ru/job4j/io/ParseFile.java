package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private File file;

    public synchronized void setFile(File f) {
        file = f;
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized String getContent(Predicate<Integer> cond) throws IOException {
        try (FileReader fr = new FileReader(file)) {
            StringBuilder output = new StringBuilder();
            int data;
            while (fr.ready()) {
                data = fr.read();
                if (cond.test(data)) {
                    output.append((char) data);
                }
            }
            return output.toString();
        }
    }

    public synchronized void saveContent(String content) {
        try (FileWriter o = new FileWriter(file)) {
            o.write(content);
            o.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ParseFile pf = new ParseFile();
        pf.setFile(new File("/Users/user/Desktop/test.txt"));
        try {
            System.out.println(pf.getContent(i -> i < 0x80));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
