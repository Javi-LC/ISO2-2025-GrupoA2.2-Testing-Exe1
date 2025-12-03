package parser;

import exception.InputException;
import java.time.LocalDate;
import java.time.format.*;

public class DateParser implements ValueParser<LocalDate> {

    private final DateTimeFormatter formatter;

    public DateParser(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate parse(String input) throws InputException {
        try {
            return LocalDate.parse(input.trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new InputException("Invalid date: " + input, e);
        }
    }
}