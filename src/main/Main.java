package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        config conf = new config(); 
        Scanner scanner = new Scanner(System.in); 

        Reports report = new Reports(conf, scanner); 
        Students students = new Students(conf, scanner);
        Books books = new Books();

        int choice;
        do {
            System.out.println("   MAIN MENU ");
            System.out.println("1. Student Management");
            System.out.println("2. Book Management");
            System.out.println("3. View Student Report");
            System.out.println("4. View Book Report");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            choice = getValidChoice(scanner); 

            switch (choice) {
                case 1:
                    students.studentInfo(); 
                    break;
                case 2:
                    books.BookInfo(); 
                    break;
                case 3:
                    report.viewStudentReport();
                    break;
                case 4:
                    report.viewBookReport(); 
                    break;
                case 5:
                    System.out.println("Exiting the system. Thank you!"); 
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 5); 

        scanner.close(); 
    }

    private static int getValidChoice(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim(); 
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 5) {
                    return choice; 
                } else {
                    System.out.print("Please enter a valid number between 1 and 5: "); 
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
