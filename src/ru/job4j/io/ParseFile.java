package ru.job4j.io;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class ParseFile {
    private File file;

    public synchronized void setFile(File f) {
        file = f;
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized String getContent() throws IOException {
        List<String> list = Files.readAllLines(file.toPath());
        StringBuilder output = new StringBuilder();
        for (String s : list) {
            output.append(s);
        }
        return output.toString();
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        InputStream i = new FileInputStream(file);
        StringBuilder output = new StringBuilder();
        int data;
        while ((data = i.read()) != -1) {
            if (data < 0x80) {
                output.append(data);
            }
        }
        return output.toString();
    }

    public synchronized void saveContent(String content) {
        try (FileWriter o = new FileWriter(file)) {
            o.write(content);
            o.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
