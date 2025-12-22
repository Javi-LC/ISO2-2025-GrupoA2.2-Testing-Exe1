package io;

import exception.InputException;

public interface InputReader {
    String readLine(String prompt) throws InputException;
    void close();
}
