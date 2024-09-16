package emailApp;

public class email {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String department;

    // Constructor to receive the first name, last name, and department
    public email(String firstName, String lastName, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;

        // Combine elements to generate email
        this.email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + this.department.toLowerCase() + ".company.com";

        // Generate a random password
        this.password = randomPassword(8);
    }

    // Generate a random password with at least one of each type of character
    private String randomPassword(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$&";

        String combinedChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;
        char[] password = new char[length];

        // Ensure the password contains at least one of each type of character
        password[0] = upperCaseLetters.charAt((int) (Math.random() * upperCaseLetters.length()));
        password[1] = lowerCaseLetters.charAt((int) (Math.random() * lowerCaseLetters.length()));
        password[2] = digits.charAt((int) (Math.random() * digits.length()));
        password[3] = specialChars.charAt((int) (Math.random() * specialChars.length()));

        // Fill the remaining length with random characters from the combined set
        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt((int) (Math.random() * combinedChars.length()));
        }
        return new String(password);
    }

    // Change the password
    public void changePassword(String password) {
        this.password = password;
    }

    // Getters
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
    
    public String getPassword() {
        return password;
    }
}
