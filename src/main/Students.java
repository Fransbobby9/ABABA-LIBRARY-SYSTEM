package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Students {
    Scanner sc = new Scanner(System.in);
    private config conf;
    
    Books books = new Books();

    public Students(config conf, Scanner sc) {
        this.conf = conf;
        this.sc = sc;
    }

    public void studentInfo() {
        String response;
        do {
            System.out.println(" STUDENT SYSTEM MANAGEMENT ");
            System.out.println("1.  ADD STUDENT RECORD ");
            System.out.println("2.  VIEW ALL STUDENT RECORDS ");
            System.out.println("3.  UPDATE STUDENT RECORD ");
            System.out.println("4.  DELETE STUDENT RECORD");
            System.out.println("5.   EXIT");

            System.out.print("ENTER (1-5) :");
            int action = getValidAction(1, 5); 

            switch (action) {
                case 1:
                    addStudentRecord();  
                    break;
                case 2:
                    viewStudentRecords(); 
                    break;
                case 3:
                    viewStudentRecords();
                    updateStudentRecord(); 
                    viewStudentRecords();
                    break;
                case 4:
                    viewStudentRecords();
                    deleteStudentRecord();
                    viewStudentRecords();
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

        System.out.println("Thank you for using the Student Management System!");
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

    private void addStudentRecord() {
        String firstName = getValidStringInput("Enter Student First Name: ");
        String lastName = getValidStringInput("Enter Student Last Name: ");
        String email = getValidEmailInput("Enter Student Email: ");
        String status = getValidStringInput("Enter Student Status (e.g., active, inactive): ");

        String sql = "INSERT INTO tbl_students (s_fname, s_lname, s_email, s_status) VALUES (?, ?, ?, ?)";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, status);
            stmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private void viewStudentRecords() {
        String sqlQuery = "SELECT * FROM tbl_students";
        String[] columnHeaders = {"Student ID", "First Name", "Last Name", "Email", "Status"};
        String[] columnNames = {"s_id", "s_fname", "s_lname", "s_email", "s_status"};
        conf.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

   
    private void updateStudentRecord() {
        int studentId = getValidIntInput("Enter Student ID to update: ");
        String newFirstName = getValidStringInput("Enter new first name: ");
        String newLastName = getValidStringInput("Enter new last name: ");
        String newEmail = getValidEmailInput("Enter new email: ");
        String newStatus = getValidStringInput("Enter new status (e.g., active, inactive): ");

        String sql = "UPDATE tbl_students SET s_fname = ?, s_lname = ?, s_email = ?, s_status = ? WHERE s_id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newFirstName);
            stmt.setString(2, newLastName);
            stmt.setString(3, newEmail);
            stmt.setString(4, newStatus);
            stmt.setInt(5, studentId);
            stmt.executeUpdate();
            System.out.println("Student record updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating student record: " + e.getMessage());
        }
    }

    private void deleteStudentRecord() {
        int studentId = getValidIntInput("Enter Student ID to delete: ");
        String sql = "DELETE FROM tbl_students WHERE s_id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
            System.out.println("Student record deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting student record: " + e.getMessage());
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

    private String getValidEmailInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {  
                return input;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
    }
}
