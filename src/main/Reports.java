package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reports {
    private config dbConfig;
    private Scanner scanner;

    public Reports(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }

    private void showBookList() {
        String sqlQuery = "SELECT b_id, b_title, b_copies_available FROM tbl_books";  

        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.printf("%-10s%-30s%-20s\n", "Book ID", "Title", "Copies Available");
            System.out.println("--------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int bookId = rs.getInt("b_id");
                String title = rs.getString("b_title");
                int copiesAvailable = rs.getInt("b_copies_available");

                System.out.printf("%-10d%-30s%-20d\n", bookId, title, copiesAvailable);
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching book list: " + e.getMessage());
            System.out.println("--------------------------------------------------------------------------------------------");
        }
    }

    private void showStudentList() {
        String sqlQuery = "SELECT s_id, s_fname || ' ' || s_lname AS full_name, s_email, s_status FROM tbl_students";  

        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.printf("%-10s%-30s%-30s%-20s\n", "Student ID", "Full Name", "Email", "Status");
            System.out.println("--------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int s_id = rs.getInt("s_id");
                String fullName = rs.getString("full_name");
                String s_email = rs.getString("s_email");
                String s_status = rs.getString("s_status");

                System.out.printf("%-10d%-30s%-30s%-20s\n", s_id, fullName, s_email, s_status);
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching student list: " + e.getMessage());
        }
    }

    public void viewBookReport() {
        showBookList();

        int bookId = getValidIntInput("Enter Book ID to view details: ");
        
        String sqlQuery = "SELECT b_id, b_title, b_copies_available, b_language, b_gen, " +
                          "b_borrowed_date, b_return_date FROM tbl_books WHERE b_id = ?";

        String[] columnHeaders = {"Book ID", "Title", "Copies Available", "Language", "Genre", "Borrowed Date", "Return Date"};
        String[] columnNames = {"b_id", "b_title", "b_copies_available", "b_language", "b_gen", "b_borrowed_date", "b_return_date"};

        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No records found for Book ID: " + bookId);
                    return;
                }

                printTableHeader(columnHeaders);

                while (rs.next()) {
                    printTableRow(rs, columnNames);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error viewing book report: " + e.getMessage());
        }
    }

    public void viewStudentReport() {
        showStudentList();

        int studentId = getValidIntInput("Enter Student ID to view details: ");
        
        String sqlQuery = "SELECT s_id, s_fname, s_lname, s_email, s_status FROM tbl_students WHERE s_id = ?";

        String[] columnHeaders = {"Student ID", "First Name", "Last Name", "Email", "Status"};
        String[] columnNames = {"s_id", "s_fname", "s_lname", "s_email", "s_status"};

        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No records found for Student ID: " + studentId);
                    return;
                }

                printTableHeader(columnHeaders);

                while (rs.next()) {
                    printTableRow(rs, columnNames);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error viewing student report: " + e.getMessage());
        }
    }

    private int getValidIntInput(String prompt) {
        int input = -1; 
        boolean valid = false;
        
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                scanner.nextLine(); 
                if (input <= 0) {
                    System.out.println("Please enter a valid positive integer.");
                } else {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  
            }
        }
        return input;
    }

    private void printTableHeader(String[] headers) {
        System.out.println("--------------------------------------------------------------------------------------------");
        for (String header : headers) {
            System.out.printf("%-25s", header);  
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    private void printTableRow(ResultSet rs, String[] columnNames) throws SQLException {
        for (String columnName : columnNames) {
            System.out.printf("%-25s", rs.getString(columnName)); 
        }
        System.out.println();
    }
}
