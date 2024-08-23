package emailApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EmailApp {

    // Database URL, username, and password
    static final String DB_URL = "jdbc:mysql://localhost:3306/emailDB";
    static final String USER = "root";
    static final String PASS = "ra.one"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();

        email obj = new email(firstName, lastName);

        // Insert new email record into the database
        insertEmailRecord(obj);

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
                    updateEmailRecord(obj);
                    break;

                case 2:
                    System.out.print("Enter new Password: ");
                    String newPassword = sc.nextLine();
                    obj.changePassword(newPassword);
                    updateEmailRecord(obj);
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
    }

    // Method to insert a new email record into the database
    public static void insertEmailRecord(email obj) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO emails (first_name, last_name, email, password, department, alternate_email) VALUES (?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, obj.getFirstName());
            pstmt.setString(2, obj.getLastName());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getPassword());
            pstmt.setString(5, obj.getDepartment());
            pstmt.setString(6, obj.getAlternateEmail());
            pstmt.executeUpdate();

            System.out.println("Record inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing email record in the database
    public static void updateEmailRecord(email obj) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE emails SET password = ?, alternate_email = ? WHERE email = ?")) {

            pstmt.setString(1, obj.getPassword());
            pstmt.setString(2, obj.getAlternateEmail());
            pstmt.setString(3, obj.getEmail());
            pstmt.executeUpdate();

            System.out.println("Record updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
