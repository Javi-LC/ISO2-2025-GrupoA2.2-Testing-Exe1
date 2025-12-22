package io;

public class ConsoleOutputWriter implements OutputWriter {
    @Override
    public void print(String s) {
        System.out.print(s);
        System.out.flush();
    }

    @Override
    public void println(String s) {
        System.out.println(s);
    }
}