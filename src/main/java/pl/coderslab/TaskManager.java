package pl.coderslab;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.List;
import java.util.Scanner;


public class TaskManager {
    private static final String FILENAME = "tasks.csv";

    public static void main(String[] args) {
        programStart();
    }

    public static void programStart() {
        Scanner toDo = new Scanner(System.in);
        System.out.println(ConsoleColors.GREEN_BOLD + "Welcome!");
        System.out.println(ConsoleColors.BLUE + "What would you like to do today?");
        String action = "";
        while (!action.equals("exit")) {
            displayOptions();
            action = toDo.next();
            managerFunctionChoice(action);
        }

    }

    private static void managerFunctionChoice(String action) {
        switch (action) {
            case "list":
                displayList();
                break;
            case "add":
                add();
                break;
            case "edit":
                edit();
                break;
            case "remove":
                remove();
                break;
            case "clear":
                clear();
                break;
            case "exit":
                System.out.println("\n" + ConsoleColors.PURPLE_BOLD + "Thank you, see you later alligator h3h3");
                break;
            default:
                System.out.println(ConsoleColors.RED_BOLD + "Please enter a correct option.");
        }
    }

    private static void edit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the number of the task, that you wish to edit: ");
        try {
            CSVReader reader2 = new CSVReader(new FileReader(FILENAME));
            List<String[]> allElements = reader2.readAll();
            String rowEditIndex = scanner.nextLine();
            while (!NumberUtils.isParsable(rowEditIndex) || !(Integer.parseInt(rowEditIndex) >= 0
                    && Integer.parseInt(rowEditIndex) <= allElements.size())) {
                System.out.println("Select a number from 1 to " + allElements.size() + "\nor '0' to cancel");
                rowEditIndex = scanner.nextLine();
            }
            if (Integer.parseInt(rowEditIndex) == 0) {
                return;
            }
            System.out.println(ConsoleColors.BLUE + "What do you want to edit?");
            System.out.println(ConsoleColors.RESET + "Task description - enter '1'");
            System.out.println("Due date - enter '2'\nImportance(true/false) - enter '3'\nor '0' to cancel:");
            String elementToChange = scanner.nextLine();
            while (!(elementToChange.equals("0") ||elementToChange.equals("1")
                    || elementToChange.equals("2") || elementToChange.equals("3"))) {
                System.out.println("Please enter a value between 1 - 3");
                System.out.println("or '0' to cancel: ");
                elementToChange = scanner.nextLine();
            }
            if (elementToChange.equals("0")) {
                return;
            }
            String[] whatToEdit = {"task description", "due date", "importance(true/false)"};
            System.out.printf("Enter an updated version of %s: ", whatToEdit[Integer.parseInt(elementToChange) - 1]);
            String update = " " + scanner.nextLine();
            allElements.get(Integer.parseInt(rowEditIndex) - 1)[Integer.parseInt(elementToChange) - 1] = update;
            FileWriter sw = new FileWriter(FILENAME);
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(allElements, false);
            writer.close();
        } catch (IOException | CsvException exception) {
            exception.printStackTrace();
        }

    }

    private static void clear() {
        try {
            PrintWriter pw = new PrintWriter(FILENAME);
            pw.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static void displayOptions() {
        System.out.println("\n" + ConsoleColors.BLUE + "Please select one of the following options:");
        System.out.println(ConsoleColors.RESET + "\"list\": show current tasks");
        System.out.println("\"add\": create a new task");
        System.out.println("\"edit\": edit a certain task");
        System.out.println("\"remove\": delete a certain task");
        System.out.println("\"clear\": delete all tasks");
        System.out.println("\"exit\": exit the program");
    }

    public static void displayList() {
        File file = new File(FILENAME);
        try {
            Scanner scan = new Scanner(file);
            int i = 1;
            System.out.println();
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (temp.contains("true")) {
                    System.out.printf("%s%s : %s%n", ConsoleColors.RED_BOLD, i, temp);
                } else {
                    System.out.printf("%s%s : %s%n", ConsoleColors.RESET, i, temp);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void add() {
        Scanner input = new Scanner(System.in);
        try (FileWriter fileWriter = new FileWriter(FILENAME, true)) {
            System.out.print("\nTask description: ");
            fileWriter.append(input.nextLine()).append(", ");
            System.out.print("Due Date: ");
            fileWriter.append(input.nextLine()).append(", ");
            System.out.print("Is it important(true/false): ");
            fileWriter.append(input.nextLine()).append("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void remove() {
        Scanner scanner = new Scanner(System.in);
        try {
            CSVReader reader2 = new CSVReader(new FileReader(FILENAME));
            List<String[]> allElements = reader2.readAll();
            if (allElements.size() == 0) {
                System.out.println("\nThe tasks list is empty.");
                return;
            }
            System.out.print("\nWhich task do you want to remove?\n(select corresponding number)\nor '0' to cancel: ");
            String rowRemoveIndex = scanner.next();
            while (!NumberUtils.isParsable(rowRemoveIndex) || !(Integer.parseInt(rowRemoveIndex) >= 0
                    && Integer.parseInt(rowRemoveIndex) <= allElements.size())) {
                System.out.println("Select a number from 1 to " + allElements.size() + "\nor '0' to cancel");
                rowRemoveIndex = scanner.next();
            }
            if (Integer.parseInt(rowRemoveIndex) == 0) {
                return;
            }
            allElements.remove(Integer.parseInt(rowRemoveIndex) - 1);
            FileWriter sw = new FileWriter(FILENAME);
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(allElements, false);
            writer.close();

        } catch (CsvException | IOException e) {
            e.printStackTrace();
        }

    }

}
