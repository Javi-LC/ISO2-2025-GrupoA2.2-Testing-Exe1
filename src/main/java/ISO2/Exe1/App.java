package ISO2.Exe1;

import ISO2.Exe1.Domain.CustomDate;
import service.InputService;
import parser.IntegerParser;
import exception.InputException;

public class App {
    public static void main(String[] args) {
        InputService input = new InputService();
        System.out.println("Welcome to Leap Year Checker");

        boolean running = true;
        while (running) {
            try {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Check a Date");
                System.out.println("2. Exit");
                
                int choice = input.readWithParser("Select an option: ", new IntegerParser(), 3);

                switch (choice) {
                    case 1:
                        checkDate(input);
                        break;
                    case 2:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
        input.close();
    }

    private static void checkDate(InputService input) throws InputException {
        System.out.println("Reading Date...");
        int day = input.readWithParser("Enter day: ", new IntegerParser(), 3);
        int month = input.readWithParser("Enter month: ", new IntegerParser(), 3);
        int year = input.readWithParser("Enter year: ", new IntegerParser(), 3);

        try {
            CustomDate date = new CustomDate(day, month, year);
            boolean isLeap = date.isLeapYear();
            if (isLeap) {
                System.out.println("The date " + date + " is in a LEAP year.");
            } else {
                System.out.println("The date " + date + " is NOT in a leap year.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Date Error: " + e.getMessage());
        }
    }
}
