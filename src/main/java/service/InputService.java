package service;

import exception.InputException;
import io.ConsoleInputReader;
import io.InputReader;
import io.OutputWriter;
import io.SilentOutputWriter;
import parser.ValueParser;

public class InputService {

    private final InputReader reader;
    private final OutputWriter writer;

    public InputService() {
        this.reader = new ConsoleInputReader();
        this.writer = new SilentOutputWriter();
    }

    public String readString(String prompt) throws InputException {
        return reader.readLine(prompt);
    }

    public <T> T readWithParser(String prompt, ValueParser<T> parser) throws InputException {
        String line = reader.readLine(prompt);
        return parser.parse(line); 
    }

    public void close() {
        reader.close();
    }
}