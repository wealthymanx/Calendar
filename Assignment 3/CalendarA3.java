// Programmer: Marcel Rodriguez
// Date: 6/11/2023
// Class: CS &141
// Assignment: Assignment 3 
// Purpose: Creates a calendar that allows users to view and navigate through different months, plan 
// events, and save the calendar with events to a file.

import java.util.Scanner;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CalendarA3 {
    private static final String[] MONTH_NAMES = {
        // Array containing month names
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private static String[][] eventArray = new String[12][31];

    public static void main(String[] args) {
        // String variable ASCII cat's head
        String catHead = "       /\\__/\\ \n" +
                         "      { o  o }  \n" +
                         "      { ``*``} \n" +
                         "       (_(_)_\n" +
                         "    Cat Calendar";

        Scanner scanner = new Scanner(System.in);
        int inputMonth = LocalDate.now().getMonthValue(); // gets current month
        int inputDay = LocalDate.now().getDayOfMonth(); // gets current day

        loadEventsFromFile(); // load events from file into eventArray

        System.out.println("What date would you like to look at? (mm/dd)");
        String inputDate = scanner.nextLine();
        String[] dateParts = inputDate.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);

        System.out.println(catHead);
        drawMonth(month, day); // Display calendar for specified month/day

        System.out.println("\nCurrent Date:\n");
        drawMonth(inputMonth, inputDay); // display calendar current month/day

        String command = "";
        while (!command.equals("q")) {
            // User menu
            System.out.println("\nUser Menu:");
            // User menu options
            System.out.println("Enter 'n' to display the next month");
            System.out.println("Enter 'p' to display the previous month");
            System.out.println("Enter 'e' to enter a date and display the corresponding calendar");
            System.out.println("Enter 't' to get today's date");
            System.out.println("Enter 'ev' to plan an event");
            System.out.println("Enter 'fp' to print the calendar to a file");
            System.out.println("Enter 'q' to quit the program");

            System.out.print("Enter your command: ");
            command = scanner.nextLine();

            if (command.equals("n")) {
                // Display the next month
                month++;
                if (month > 12)
                    month = 1;
                drawMonth(month, day);
            } else if (command.equals("p")) {
                // Display the previous month
                month--;
                if (month < 1)
                    month = 12;
                drawMonth(month, day);
            } else if (command.equals("e")) {
                // Enter a specific date and display the calendar
                System.out.print("Enter a date (mm/dd): ");
                inputDate = scanner.nextLine();
                dateParts = inputDate.split("/");
                month = Integer.parseInt(dateParts[0]);
                day = Integer.parseInt(dateParts[1]);
                drawMonth(month, day);
            } else if (command.equals("t")) {
                month = LocalDate.now().getMonthValue();
                day = LocalDate.now().getDayOfMonth();
                drawMonth(month, day);
                System.out.println("\nCurrent Date:\n");
                drawMonth(inputMonth, inputDay);
            } else if (command.equals("ev")) {
                System.out.print("Enter the event (MM/DD event_title): ");
                String event = scanner.nextLine();
                String[] eventParts = event.split(" ");
                String eventDate = eventParts[0];
                String title = eventParts[1];

                dateParts = eventDate.split("/");
                int eventMonth = Integer.parseInt(dateParts[0]);
                int eventDay = Integer.parseInt(dateParts[1]);
                eventArray[eventMonth - 1][eventDay - 1] = title;

                month = eventMonth;
                day = eventDay;

                drawMonth(month, day);
                System.out.println("\nCurrent Date:\n");
                drawMonth(inputMonth, inputDay);
            } else if (command.equals("fp")) {
                System.out.print("Enter the month to print (1-12): ");
                int printMonth = scanner.nextInt();
                scanner.nextLine(); 

                System.out.print("Enter the name of the file to print to: ");
                String fileName = scanner.nextLine();

                printMonthToFile(printMonth, fileName);
            } else if (command.equals("q")) {
                System.out.println("Exiting the program.");
            } else {
                System.out.println("Invalid command. Please enter a valid command.");
            }
        }
    }
    // draws the calendar for a given month and highlights the current day and events
    public static void drawMonth(int month, int day) {
        System.out.println();
        System.out.printf("%28s\n", MONTH_NAMES[month - 1]);

        YearMonth yearMonth = YearMonth.of(2023, month);
        DayOfWeek startingDay = yearMonth.atDay(1).getDayOfWeek();
        int emptyCells = startingDay.getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        printEqualSigns();

        for (int i = 0; i < emptyCells; i++) {
            System.out.print("|       ");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            if (emptyCells % 7 == 0) {
                System.out.println("|");
                printEqualSigns();
            }

            String event = eventArray[month - 1][i - 1];

            if (i == day) {
                System.out.printf("|%6d ", i);
            } else if (event != null) {
                System.out.printf("|%6s ", event);
            } else {
                System.out.printf("|%6d ", i);
            }

            emptyCells++;
        }

        while (emptyCells % 7 != 0) {
            System.out.print("|       ");
            emptyCells++;
        }

        System.out.println("|");
        printEqualSigns();
        displayDate(month, day);
    }
    // formats and displays the current date "MM/DD"
    public static void displayDate(int month, int day) {
        System.out.printf("%02d/%02d\n", month, day);
    }
    // reads event data from the "calendarEvents.txt" file
    public static void loadEventsFromFile() {
        File file = new File("calendarEvents.txt");
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] eventParts = line.split(" ");
                    String date = eventParts[0];
                    String title = eventParts[1];
                    String[] dateParts = date.split("/");
                    int eventMonth = Integer.parseInt(dateParts[0]);
                    int eventDay = Integer.parseInt(dateParts[1]);
                    eventArray[eventMonth - 1][eventDay - 1] = title;
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Failed to read events from file.");
            }
        }
    }
    // prints a line of equal signs to separate the calendar grid
    public static void printEqualSigns() {
        String s = "========";
        String line = "";
        for (int i = 1; i <= 7; i++) {
            line = line + s;
        }
        System.out.println(line);
    }
    // writes the calendar for a specific month to a user specified file.
    public static void printMonthToFile(int month, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName);
            drawMonthToFile(month, writer); // write the calendar content to file
            writer.close();
            System.out.println("Calendar printed to file: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to print calendar to file.");
        }
    }
    // writes the calendar output to a PrintWriter object
    public static void drawMonthToFile(int month, PrintWriter writer) {
        writer.println();
        writer.printf("%28s\n", MONTH_NAMES[month - 1]);

        YearMonth yearMonth = YearMonth.of(2023, month);
        DayOfWeek startingDay = yearMonth.atDay(1).getDayOfWeek();
        int emptyCells = startingDay.getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        printEqualSignsToFile(writer);

        for (int i = 0; i < emptyCells; i++) {
            writer.print("|       ");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            if (emptyCells % 7 == 0) {
                writer.println("|");
                printEqualSignsToFile(writer);
            }

            String event = eventArray[month - 1][i - 1];

            if (event != null) {
                writer.printf("|%6s ", event);
            } else {
                writer.printf("|%6d ", i);
            }

            emptyCells++;
        }

        while (emptyCells % 7 != 0) {
            writer.print("|       ");
            emptyCells++;
        }

        writer.println("|");
        printEqualSignsToFile(writer);
    }

    public static void printEqualSignsToFile(PrintWriter writer) {
        String s = "========";
        String line = "";
        for (int i = 1; i <= 7; i++) {
            line = line + s;
        }
        writer.println(line);
    }
}







