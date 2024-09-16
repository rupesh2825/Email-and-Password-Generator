package emailApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmailApp {

    // Database URL, username, and password
    static final String DB_URL = "jdbc:mysql://localhost:3306/emailDB"; // Update database name
    static final String USER = "root"; // Update with your MySQL username
    static final String PASS = "ra.one"; // Replace with your MySQL password

        
    // Method to insert a new employee record into the database
    public static void insertEmployeeRecord(email obj) {
        String sql = "INSERT INTO employees (first_name, last_name, email, password, department_id) " +
                     "VALUES (?, ?, ?, ?, (SELECT department_id FROM departments WHERE department_name = ?))";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, obj.getFirstName());
            pstmt.setString(2, obj.getLastName());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getPassword());
            pstmt.setString(5, obj.getDepartment());
            pstmt.executeUpdate();

            System.out.println("Employee record inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    
}
