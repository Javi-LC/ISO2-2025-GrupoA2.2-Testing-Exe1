package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import exception.InputException;

public class FileInputReader implements InputReader {

    private final BufferedReader reader;

    public FileInputReader(Path file) throws InputException {
        try {
            this.reader = Files.newBufferedReader(file);
        } catch (IOException e) {
            throw new InputException("Cannot open file: " + file, e);
        }
    }

    @Override
    public String readLine(String prompt) throws InputException {
        try {
            String line = reader.readLine();
            if (line == null) throw new InputException("End of file");
            return line;
        } catch (IOException e) {
            throw new InputException("Error reading file", e);
        }
    }

    @Override
    public void close() {
        try { reader.close(); } catch (IOException ignored) {}
    }
}
