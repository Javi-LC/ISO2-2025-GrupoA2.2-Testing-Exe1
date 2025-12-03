package io;

import exception.InputException;
import java.io.*;

public class ConsoleInputReader implements InputReader {

    private final BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine(String prompt) throws InputException {
        try {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
                System.out.flush();
            }
            String line = in.readLine();
            if (line == null) throw new InputException("End of input");
            return line;
        } catch (IOException e) {
            throw new InputException("I/O error", e);
        }
    }

    @Override
    public void close() {
        try { in.close(); } catch (IOException ignored) {}
    }
}