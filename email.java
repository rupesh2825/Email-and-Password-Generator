package emailApp;

import java.util.Scanner;

public class email {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String department;
    private String alternateEmail;

    // Constructor to receive the first name and last name
    public email(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        System.out.println("Email Created: " + this.firstName + " " + this.lastName);

        // Call a method asking for the department, return the department
        this.department = setDepartment();
        System.out.println("Department: " + this.department);

        // Combine elements to generate email
        email = firstName.toLowerCase() + lastName.toLowerCase() + "@" + department + ".Company.com";
        System.out.println("Your Email: " + email);

        // Call a method asking for the random password
        this.password = randomPassword(8);
        System.out.println("Your Password: " + this.password);
    }

    // Ask for the department
    private String setDepartment() {
        System.out.print("DEPARTMENT CODE\n1 Sales \n2 Development \n3 Accounting \n0 None \nEnter the Department Code: ");
        Scanner sc = new Scanner(System.in);
        int depChoice = sc.nextInt();

        if (depChoice == 1) {
            return "Sales";
        } else if (depChoice == 2) {
            return "Development";
        } else if (depChoice == 3) {
            return "Accounting";
        } else {
            return "";
        }
    }

    // Generate a random password
    private String randomPassword(int length) {
        String passwordSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$&";
        char[] password = new char[length];

        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * passwordSet.length());
            password[i] = passwordSet.charAt(rand);
        }
        return new String(password);
    }

    // Set the alternate email
    public void setAlternateEmail(String altEmail) {
        this.alternateEmail = altEmail;
    }

    // Change the password
    public void changePassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public String getPassword() {
        return password;
    }
}
