package pl.coderslab;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.List;
import java.util.Scanner;


public class TaskManager {

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
            switch (action) {
                case "list":
                    displayList();
                    break;
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "exit":
                    System.out.println("\n" + ConsoleColors.PURPLE_BOLD + "Thank you, see you later alligator h3h3");
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD + "Please enter a correct option.");
            }
        }

    }

    public static void displayOptions() {
        System.out.println("\n" + ConsoleColors.BLUE + "Please select one of the following options:");
        System.out.println(ConsoleColors.RESET + "\"list\": show current tasks");
        System.out.println("\"add\": create a new task");
        System.out.println("\"remove\": delete a certain task");
        System.out.println("\"exit\": exit the program");
    }

    public static void displayList() {
        File file = new File("tasks.csv");
        try {
            Scanner scan = new Scanner(file);
            int i = 1;
            System.out.println();
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                if (temp.contains("true")) {
                    System.out.println(ConsoleColors.RED_BOLD + i + " : " + temp);
                } else {
                    System.out.println(ConsoleColors.RESET + i + " : " + temp);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void add() {
        Scanner input = new Scanner(System.in);

        try (FileWriter fileWriter = new FileWriter("tasks.csv", true)) {
            RandomAccessFile f = new RandomAccessFile("tasks.csv", "rw");
            String str = "";
            if (f.length() == 0) {
                System.out.print("\nTask description: ");
                str = input.nextLine();
                fileWriter.append(str + ", ");
            } else {
                System.out.print("\nTask description: ");
                str = input.nextLine();
                fileWriter.append("\n" + str + ", ");
            }
            f.close();
            System.out.print("Due Date: ");
            str = input.nextLine();
            fileWriter.append(str + ", ");
            System.out.print("Is it important(true/false): ");
            str = input.nextLine();
            fileWriter.append(str);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void remove() {
        Scanner scanner = new Scanner(System.in);
        try {
            CSVReader reader2 = new CSVReader(new FileReader("tasks.csv"));
            List<String[]> allElements = reader2.readAll();
            if (allElements.size() == 0) {
                System.out.println("\nThe tasks list is empty.");
                return;
            }
            System.out.print("\nWhich task do you want to remove?\n(select corresponding number): ");
            String rowRemoveIndex = scanner.next();
            while (!NumberUtils.isParsable(rowRemoveIndex) || !(Integer.parseInt(rowRemoveIndex) >= 0
                    && Integer.parseInt(rowRemoveIndex) <= allElements.size())) {
                System.out.println("Select a number from 1 to " + allElements.size() + "\n(0 to cancel)");
                rowRemoveIndex = scanner.next();
            }
            if (Integer.parseInt(rowRemoveIndex) == 0) {
                return;
            }
            allElements.remove(Integer.parseInt(rowRemoveIndex) - 1);
            FileWriter sw = new FileWriter("tasks.csv");
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(allElements, false);
            writer.close();

            RandomAccessFile f = new RandomAccessFile("tasks.csv", "rw");
            byte b = 1;
            if (f.length() > 1) f.setLength(f.length() - b);
            f.close();

        } catch (CsvException | IOException e) {
            e.printStackTrace();
        }

    }

}
