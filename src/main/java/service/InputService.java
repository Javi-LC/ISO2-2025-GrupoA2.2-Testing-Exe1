package service;

import exception.InputException;
import io.ConsoleInputReader;
import io.ConsoleOutputWriter;
import io.InputReader;
import io.OutputWriter;
import parser.ValueParser;

public class InputService {

    private final InputReader reader;
    private final OutputWriter writer;

    public InputService() {
        this.reader = new ConsoleInputReader();
        this.writer = new ConsoleOutputWriter();
    }

    public String readString(String prompt) throws InputException {
        return reader.readLine(prompt);
    }

    public <T> T readWithParser(
            String prompt,
            ValueParser<T> parser,
            int maxRetries
    ) throws InputException {

        int attempts = 0;

        while (true) {
            attempts++;
            String line = reader.readLine(prompt);

            try {
                return parser.parse(line);
            } catch (InputException e) {
                writer.println("Invalid input: " + e.getMessage());
                if (maxRetries > 0 && attempts >= maxRetries) {
                    throw new InputException("Maximum attempts reached for: " + prompt);
                }
            }
        }
    }

    public void close() {
        reader.close();
    }
}