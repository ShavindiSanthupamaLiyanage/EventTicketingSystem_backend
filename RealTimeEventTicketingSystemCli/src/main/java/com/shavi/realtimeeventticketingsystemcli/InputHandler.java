//package com.shavi.realtimeeventticketingsystemcli;
//
//import java.util.Scanner;
//
//public class InputHandler {
//
//    private final Scanner scanner;
//
//    public InputHandler() {
//        this.scanner = new Scanner(System.in);
//    }
//
//    public int getValidInput(String prompt, int min, int max) {
//        int input;
//        while (true) {
//            System.out.print(prompt);
//            try {
//                input = Integer.parseInt(scanner.nextLine().trim());
//                if (input >= min && input <= max) {
//                    break;
//                } else {
//                    System.out.printf("Invalid input! Please enter a number between %d and %d.%n", min, max);
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input! Please enter a valid integer.");
//            }
//        }
//        return input;
//    }
//
//    public String getCommand() {
//        return scanner.nextLine().trim().toLowerCase();
//    }
//}
