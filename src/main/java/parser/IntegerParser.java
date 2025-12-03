package parser;

import exception.InputException;

public class IntegerParser implements ValueParser<Integer> {
    @Override
    public Integer parse(String input) throws InputException {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InputException("Invalid integer: " + input, e);
        }
    }
}