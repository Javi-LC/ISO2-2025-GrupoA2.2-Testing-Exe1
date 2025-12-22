package ISO2.Exe1;

import ISO2.Exe1.Domain.CustomDate;
import service.InputService;
import parser.IntegerParser;
import exception.InputException;

import java.io.PrintStream;

public class App {
    
    private final InputService input;
    private final PrintStream output;
    private boolean running;

    // Constructor para producción
    public App() {
        this(new InputService(), System.out);
    }

    // Constructor para testing con inyección de dependencias
    public App(InputService input, PrintStream output) {
        this.input = input;
        this.output = output;
        this.running = true;
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        output.println("Welcome to Leap Year Checker");

        while (running) {
            try {
                output.println("\n--- Menu ---");
                output.println("1. Check a Date");
                output.println("2. Exit");
                
                int choice = input.readWithParser("Select an option: ", new IntegerParser());

                processChoice(choice);
            } catch (InputException e) {
                output.println("Error: " + e.getMessage());
            } catch (Exception e) {
                output.println("Unexpected Error: " + e.getMessage());
            }
        }
        input.close();
    }

    // Método expuesto para testing
    public void processChoice(int choice) throws InputException {
        switch (choice) {
            case 1:
                checkDate();
                break;
            case 2:
                running = false;
                output.println("Goodbye!");
                break;
            default:
                output.println("Invalid option. Please try again.");
        }
    }

    public void checkDate() throws InputException {
        output.println("Reading Date...");
        int day = input.readWithParser("Enter day: ", new IntegerParser());
        int month = input.readWithParser("Enter month: ", new IntegerParser());
        int year = input.readWithParser("Enter year: ", new IntegerParser());

        checkDateWithValues(day, month, year);
    }

    // Método para testing directo sin I/O
    public String checkDateWithValues(int day, int month, int year) {
        try {
            CustomDate date = new CustomDate(day, month, year);
            boolean isLeap = date.isLeapYear();
            String result;
            if (isLeap) {
                result = "The date " + date + " is in a LEAP year.";
            } else {
                result = "The date " + date + " is NOT in a leap year.";
            }
            output.println(result);
            return result;
        } catch (IllegalArgumentException e) {
            String error = "Date Error: " + e.getMessage();
            output.println(error);
            return error;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }
}
