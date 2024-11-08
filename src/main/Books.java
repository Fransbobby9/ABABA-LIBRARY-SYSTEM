package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Books {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
 
    public void BookInfo() {
        String response;
        do {
            System.out.println("LIBRARY SYSTEM MANAGEMENT");
            System.out.println("1. ADD BOOK RECORD");
            System.out.println("2. VIEW ALL BOOK RECORDS");
            System.out.println("3. UPDATE BOOK RECORD");
            System.out.println("4. DELETE BOOK RECORD");
            System.out.println("5. EXIT");

            System.out.print("ENTER (1-5):");
            int action = getValidAction(1, 5);

            switch (action) {
                case 1:
                    addBookRecord();  
                    break;
                case 2:
                    viewBookRecords(); 
                    break;
                case 3:
                    viewBookRecords();
                    updateBookRecord(); 
                    viewBookRecords();
                    break;
                case 4:
                    viewBookRecords();
                    deleteBookRecord();
                    viewBookRecords();
                    break;
                case 5:
                    return; 
                default:
                    System.out.println("Invalid action. Please select a valid option.");
                    break;
            }

            while (true) {
                System.out.print("Do you want to perform another action? (yes/no): ");
                response = sc.nextLine().toLowerCase();
                if (response.equals("yes") || response.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no' only.");
                }
            }

        } while (response.equals("yes"));

        System.out.println("Thank you for using the Library Management System!");
    }

    private int getValidAction(int min, int max) {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                int action = Integer.parseInt(input);
                if (action >= min && action <= max) {
                    return action;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    private void addBookRecord() {
        String title = getValidStringInput("Enter Book Title: ");
        String language = getValidStringInput("Enter Language: ");
        String genre = getValidStringInput("Enter Genre: ");

        int copiesAvail = getValidPositiveIntInput("Enter Copies Available: ");

        String borrowedDate = getValidDateInput("Enter Borrowed Date (YYYY-MM-DD): ");
        String returnDate = getValidDateInput("Enter Return Date (YYYY-MM-DD): ");
        
        String sql = "INSERT INTO tbl_books (b_title, b_copies_available, b_language, b_gen, b_borrowed_date, b_return_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        
            stmt.setString(1, title);
            stmt.setInt(2, copiesAvail);
            stmt.setString(3, language);
            stmt.setString(4, genre);
            stmt.setString(5, borrowedDate);
            stmt.setString(6, returnDate);
            
            stmt.executeUpdate();
            System.out.println("Book added successfully.");
            
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    private int getValidPositiveIntInput(String prompt) {
        while (true) {
            String input = getValidStringInput(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Value must be a positive integer. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String getValidDateInput(String prompt) {
        while (true) {
            String date = getValidStringInput(prompt);
            if (isValidDate(date)) {
                return date;
            } else {
                System.out.println("Invalid date format. Please use 'YYYY-MM-DD'.");
            }
        }
    }

    private boolean isValidDate(String date) {
        String regex = "^\\d{4}-\\d{2}-\\d{2}$"; 
        if (!date.matches(regex)) {
            return false;
        }
        try {
            java.sql.Date.valueOf(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void viewBookRecords() {
        String sqlQuery = "SELECT * FROM tbl_books";
        String[] columnHeaders = {"Book ID", "Title", "Copies Available", "Language", "Genre", "Borrowed Date", "Return Date"};
        String[] columnNames = {"b_id", "b_title", "b_copies_available", "b_language", "b_gen", "b_borrowed_date", "b_return_date"};
        conf.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateBookRecord() {
        int bookId = getValidIntInput("Enter Book ID to update: ");
        String newTitle = getValidStringInput("Enter new title: ");
        int newCopiesAvail = getValidPositiveIntInput("Enter new Copies Available: ");
        String newLanguage = getValidStringInput("Enter new language: ");
        String newGenre = getValidStringInput("Enter new genre: ");
        String newBorrowedDate = getValidDateInput("Enter new borrowed date: ");
        String newReturnDate = getValidDateInput("Enter new Return date: ");

        String sql = "UPDATE tbl_books SET b_title = ?, b_copies_available = ?, b_language = ?, b_gen = ?, b_borrowed_date = ?, b_return_date = ? WHERE b_id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setInt(2, newCopiesAvail);
            stmt.setString(3, newLanguage);
            stmt.setString(4, newGenre);
            stmt.setString(5, newBorrowedDate);
            stmt.setString(6, newReturnDate);
            stmt.setInt(7, bookId);
            stmt.executeUpdate();
            System.out.println("Book record updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating book record: " + e.getMessage());
        }
    }

    private void deleteBookRecord() {
        int bookId = getValidIntInput("Enter Book ID to delete: ");
        String sql = "DELETE FROM tbl_books WHERE b_id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            System.out.println("Book record deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting book record: " + e.getMessage());
        }
    }

    private String getValidStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    private int getValidIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
