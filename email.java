package emailApp;

public class email {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String department;
    private String alternateEmail;

    // Constructor to receive the first name, last name, and department
    public email(String firstName, String lastName, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;

        System.out.println("Email Created: " + this.firstName + " " + this.lastName);
        System.out.println("Department: " + this.department);

        // Combine elements to generate email
        this.email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + this.department.toLowerCase() + ".company.com";
        System.out.println("Your Email: " + this.email);

        // Generate a random password
        this.password = randomPassword(8);
        System.out.println("Your Password: " + this.password);
    }

    // Generate a random password with at least one of each type of character
    private String randomPassword(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$&";

        String combinedChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;
        char[] password = new char[length];

        password[0] = upperCaseLetters.charAt((int) (Math.random() * upperCaseLetters.length()));
        password[1] = lowerCaseLetters.charAt((int) (Math.random() * lowerCaseLetters.length()));
        password[2] = digits.charAt((int) (Math.random() * digits.length()));
        password[3] = specialChars.charAt((int) (Math.random() * specialChars.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt((int) (Math.random() * combinedChars.length()));
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
