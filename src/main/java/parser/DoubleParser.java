package parser;

import exception.InputException;

public class DoubleParser implements ValueParser<Double> {
    @Override
    public Double parse(String input) throws InputException {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            throw new InputException("Invalid double: " + input, e);
        }
    }
}