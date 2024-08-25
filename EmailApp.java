package emailApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmailApp {

    // Database URL, username, and password
    static final String DB_URL = "jdbc:mysql://localhost:3306/emailDB"; // Update database name
    static final String USER = "root"; // Update with your MySQL username
    static final String PASS = "ra.one"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Display menu options
        System.out.println("Choose an option:");
        System.out.println("1. Generate Email and Password");
        System.out.println("2. Show Existing Emails");
        int option = sc.nextInt();
        sc.nextLine(); // Consume newline

        email obj = null; // Initialize the object

        switch (option) {
            case 1:
                // Generate email and password
                System.out.print("Enter First Name: ");
                String firstName = sc.nextLine();
                System.out.print("Enter Last Name: ");
                String lastName = sc.nextLine();
                System.out.print("Enter Department: ");
                String departmentName = sc.nextLine();

                // Insert department if it doesn't exist
                //insertDepartment(departmentName);

                obj = new email(firstName, lastName, departmentName);

                // Insert new employee record into the database
                insertEmployeeRecord(obj);
                break;

            case 2:
                // Show existing emails
                System.out.println(getEmails());
                return; // Exit after showing emails

            default:
                System.out.println("Invalid option. Please restart the program and choose a valid option.");
                return; // Exit the program
        }

        // Further operations after generating email and password
        char r;
        do {
            System.out.println("Enter what you want to change:");
            System.out.println("1 Change Email \n2 Change Password \n3 Get Email \n4 Get Password");
            int ch = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (ch) {
                case 1:
                    System.out.print("Enter new Email: ");
                    String newEmail = sc.nextLine();
                    obj.setAlternateEmail(newEmail);
                    updateEmployeeRecord(obj);
                    break;

                case 2:
                    System.out.print("Enter new Password: ");
                    String newPassword = sc.nextLine();
                    obj.changePassword(newPassword);
                    updateEmployeeRecord(obj);
                    break;

                case 3:
                    System.out.println("Your Email: " + obj.getAlternateEmail());
                    break;

                case 4:
                    System.out.println("Your Password: " + obj.getPassword());
                    break;

                default:
                    System.out.println("Enter a valid choice");
            }

            System.out.println("Do you want to continue? (y/n)");
            r = sc.next().charAt(0);

        } while (r == 'y' || r == 'Y');
        sc.close(); // Close scanner to avoid resource leak
    }

    // Method to insert a new department if it doesn't already exist
    /*public static void insertDepartment(String departmentName) {
        String sql = "INSERT IGNORE INTO departments (department_name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departmentName);
            pstmt.executeUpdate();

            System.out.println("Department inserted or already exists!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    // Method to insert a new employee record into the database
    public static void insertEmployeeRecord(email obj) {
        String sql = "INSERT INTO employees (first_name, last_name, email, password, department_id, alternate_email) " +
                     "VALUES (?, ?, ?, ?, (SELECT department_id FROM departments WHERE department_name = ?), ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, obj.getFirstName());
            pstmt.setString(2, obj.getLastName());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getPassword());
            pstmt.setString(5, obj.getDepartment());
            pstmt.setString(6, obj.getAlternateEmail());
            pstmt.executeUpdate();

            System.out.println("Employee record inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing employee record in the database
    public static void updateEmployeeRecord(email obj) {
        String sql = "UPDATE employees SET password = ?, alternate_email = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, obj.getPassword());
            pstmt.setString(2, obj.getAlternateEmail());
            pstmt.setString(3, obj.getEmail());
            pstmt.executeUpdate();

            System.out.println("Record updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all existing emails from the database
    public static String getEmails() {
        StringBuilder emails = new StringBuilder();
        String sql = "SELECT e.id, e.first_name, e.last_name, e.email, d.department_name FROM employees e " +
                     "JOIN departments d ON e.department_id = d.department_id";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String departmentName = rs.getString("department_name");

                emails.append("ID: ").append(id)
                      .append(", Name: ").append(firstName).append(" ").append(lastName)
                      .append(", Department: ").append(departmentName)
                      .append(", Email: ").append(email)
                      .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving emails from the database.";
        }
        return emails.toString();
    }

}
