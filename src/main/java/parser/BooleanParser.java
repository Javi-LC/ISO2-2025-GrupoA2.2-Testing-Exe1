package parser;

import exception.InputException;

public class BooleanParser implements ValueParser<Boolean> {
    @Override
    public Boolean parse(String input) throws InputException {
        try {
            return Boolean.parseBoolean(input.trim());
        } catch (NumberFormatException e) {
            throw new InputException("Invalid double: " + input, e);
        }
    }
}