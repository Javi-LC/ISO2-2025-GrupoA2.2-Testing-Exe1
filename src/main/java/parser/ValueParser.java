package parser;

import exception.InputException;

public interface ValueParser<T> {
    T parse(String input) throws InputException;
}