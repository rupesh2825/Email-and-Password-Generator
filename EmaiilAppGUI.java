package emailApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("unused")
public class EmailAppGUI {

    private JFrame mainFrame;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> departmentComboBox;
    private JButton generateButton;
    private JButton showEmailsButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmailAppGUI window = new EmailAppGUI();
                window.mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmailAppGUI() {
        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setBounds(100, 100, 800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);  // Center the frame
        mainFrame.getContentPane().setLayout(null);

        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(10, 20, 100, 30);
        lblFirstName.setFont(new Font("Arial", Font.BOLD, 16));
        mainFrame.getContentPane().add(lblFirstName);

        firstNameField = new JTextField();
        firstNameField.setBounds(120, 20, 200, 30);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        mainFrame.getContentPane().add(firstNameField);
        firstNameField.setColumns(10);

        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(10, 70, 100, 30);
        lblLastName.setFont(new Font("Arial", Font.BOLD, 16));
        mainFrame.getContentPane().add(lblLastName);

        lastNameField = new JTextField();
        lastNameField.setBounds(120, 70, 200, 30);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        mainFrame.getContentPane().add(lastNameField);
        lastNameField.setColumns(10);

        JLabel lblDepartment = new JLabel("Department:");
        lblDepartment.setBounds(10, 120, 100, 30);
        lblDepartment.setFont(new Font("Arial", Font.BOLD, 16));
        mainFrame.getContentPane().add(lblDepartment);

        departmentComboBox = new JComboBox<>(new String[]{
            "Sales", "Development", "Accounting", "HR", "Training and Development",
            "Compliance", "Corporate Strategy", "Facilities Management", "Internal Audit",
            "Risk Management", "Project Management", "Design", "Health and Safety",
            "Administration", "Corporate Communications", "Data Analytics", "Purchasing"
        });
        departmentComboBox.setBounds(120, 120, 200, 30);
        departmentComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        mainFrame.getContentPane().add(departmentComboBox);

        generateButton = new JButton("Generate Email & Password");
        generateButton.setBounds(10, 180, 300, 40);
        generateButton.setFont(new Font("Arial", Font.BOLD, 16));
        generateButton.setBackground(new Color(100, 150, 255));
        generateButton.setForeground(Color.WHITE);
        mainFrame.getContentPane().add(generateButton);

        showEmailsButton = new JButton("Show Existing Emails");
        showEmailsButton.setBounds(10, 230, 300, 40);
        showEmailsButton.setFont(new Font("Arial", Font.BOLD, 16));
        showEmailsButton.setBackground(new Color(100, 150, 255));
        showEmailsButton.setForeground(Color.WHITE);
        mainFrame.getContentPane().add(showEmailsButton);

        // Action listener for generating email and password
        generateButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String department = (String) departmentComboBox.getSelectedItem();

            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                email newEmail = new email(firstName, lastName, department);
                EmailApp.insertEmployeeRecord(newEmail);
                JOptionPane.showMessageDialog(mainFrame, "Email and Password Generated and Saved!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please enter both first name and last name.");
            }
        });

        // Action listener for showing existing emails in a new frame
        showEmailsButton.addActionListener(e -> showExistingEmailsInNewFrame());
    }

    // Method to open a new frame and display existing emails in a table with update options
    private void showExistingEmailsInNewFrame() {
        JFrame emailFrame = new JFrame("Existing Emails");
        emailFrame.setBounds(100, 100, 1200, 600);  // Increased width for buttons
        emailFrame.setLocationRelativeTo(null);  // Center the frame
        emailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this frame

        // Table to display emails with update buttons
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Password", "Department", "Update Email", "Update Password"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable emailsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(emailsTable);
        emailFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Fetch existing emails and add to table model
        fetchExistingEmails(model, emailsTable);

        // Adjust the column widths
        adjustColumnWidths(emailsTable);

        emailFrame.setVisible(true);
    }

    // Method to update employee email
    private void updateEmployeeEmail(int employeeId, String newEmail) {
        String sql = "UPDATE employees SET email = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(EmailApp.DB_URL, EmailApp.USER, EmailApp.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update employee password
    private void updateEmployeePassword(int employeeId, String newPassword) {
        String sql = "UPDATE employees SET password = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(EmailApp.DB_URL, EmailApp.USER, EmailApp.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch existing emails from the database and add them to the table model
    private void fetchExistingEmails(DefaultTableModel model, JTable emailsTable) {
        String sql = "SELECT e.id, e.first_name, e.last_name, e.email, e.password, d.department_name " +
                     "FROM employees e " +
                     "JOIN departments d ON e.department_id = d.department_id";

        try (Connection conn = DriverManager.getConnection(EmailApp.DB_URL, EmailApp.USER, EmailApp.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");  // Fetch password
                String departmentName = rs.getString("department_name");

                // Add a row with data and buttons for updating email and password
                model.addRow(new Object[]{id, firstName, lastName, email, password, departmentName, "Update Email", "Update Password"});
            }

            // Set custom renderer and editor for buttons
            emailsTable.getColumn("Update Email").setCellRenderer(new ButtonRenderer());
            emailsTable.getColumn("Update Email").setCellEditor(new ButtonEditor(new JCheckBox(), emailsTable, true));

            emailsTable.getColumn("Update Password").setCellRenderer(new ButtonRenderer());
            emailsTable.getColumn("Update Password").setCellEditor(new ButtonEditor(new JCheckBox(), emailsTable, false));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to adjust the column widths based on content
    private void adjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = 100; // Minimum width
            int maxWidth = 300;

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // Set maximum width limit
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    // Custom renderer for the buttons in the table
    @SuppressWarnings("serial")
	class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setBackground(new Color(200, 220, 255));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom editor for the buttons in the table
    @SuppressWarnings("serial")
	class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isEmail;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table, boolean isEmail) {
            super(checkBox);
            this.isEmail = isEmail;
            this.table = table;
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(new Color(150, 200, 255));
            button.setForeground(Color.BLACK);
            button.addActionListener(e -> handleButtonClick(row, isEmail));
            return button;
        }

        private void handleButtonClick(int row, boolean isEmail) {
            int employeeId = (int) table.getValueAt(row, 0);  // Get employee ID from the table
            String firstName = (String) table.getValueAt(row, 1);
            String lastName = (String) table.getValueAt(row, 2);

            if (isEmail) {
                String newEmail = JOptionPane.showInputDialog("Enter new email for " + firstName + " " + lastName + ":");
                if (newEmail != null && !newEmail.isEmpty()) {
                    updateEmployeeEmail(employeeId, newEmail);
                    JOptionPane.showMessageDialog(null, "Email updated successfully!");
                }
            } else {
                String newPassword = JOptionPane.showInputDialog("Enter new password for " + firstName + " " + lastName + ":");
                if (newPassword != null && !newPassword.isEmpty()) {
                    updateEmployeePassword(employeeId, newPassword);
                    JOptionPane.showMessageDialog(null, "Password updated successfully!");
                }
            }
            fireEditingStopped();  // To stop editing mode
        }
    }
}
