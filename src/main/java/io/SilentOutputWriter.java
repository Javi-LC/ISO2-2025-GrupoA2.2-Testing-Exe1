package io;

public class SilentOutputWriter implements OutputWriter {
    @Override public void print(String s) {}
    @Override public void println(String s) {}
}