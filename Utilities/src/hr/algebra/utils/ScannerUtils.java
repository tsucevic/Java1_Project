package hr.algebra.utils;

import java.util.Arrays;
import java.util.Scanner;

public class ScannerUtils {

    public static int readInt(String message, Scanner scanner) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print(message);
            scanner.nextLine();
        }
        int number = scanner.nextInt();
        scanner.nextLine();

        return number;
    }

    public static double readDouble(String message, Scanner scanner) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.print(message);
            scanner.nextLine();
        }
        double number = scanner.nextDouble();
        scanner.nextLine();

        return number;
    }

    public static String readString(String message, Scanner scanner) {
        String line;

        do {
            System.out.print(message);
            line = scanner.nextLine().trim();
        } while (line.isEmpty());

        return line;
    }

    public static String readString(String message, Scanner scanner, String... values) {
        String line;

        do {
            System.out.print(message);
            line = scanner.nextLine().trim();
        } while (line.isEmpty() || !Arrays.asList(values).contains(line));

        return line;
    }

    public static char readChar(String message, Scanner scanner) {
        return readString(message, scanner).charAt(0);
    }

}
