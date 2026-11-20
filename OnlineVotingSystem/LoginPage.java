import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField emailField, idField;
    private JButton loginButton;

    public LoginScreen() {
        setTitle("Login to Voting System");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        add(new JLabel("Enter Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Enter National ID:"));
        idField = new JTextField();
        add(idField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String email = emailField.getText().trim();
        String id = idField.getText().trim();

        if (email.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        } else {
            // Basic validation — you can improve this with file-based checking
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();  // close login screen
            new OnlineVotingSystem(email, id);  // pass email & ID to voting system
        }
    }

    public static void main(String[] args) {
        new LoginScreen();  // Program starts here
    }
}