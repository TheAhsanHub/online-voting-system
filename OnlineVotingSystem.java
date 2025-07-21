import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class OnlineVotingSystem {
    public static void main(String[] args) {
        new MainMenu();
    }
}

// === Main Menu ===
class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Online Voting System - Main Menu");
        setSize(250, 120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10));

        JButton userLoginBtn = new JButton("User Login");
        JButton adminLoginBtn = new JButton("Admin Login");

        userLoginBtn.addActionListener(e -> {
            dispose();
            new UserLogin();
        });

        adminLoginBtn.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });

        add(userLoginBtn);
        add(adminLoginBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// === User Login ===
class UserLogin extends JFrame {
    public UserLogin() {
        setTitle("User Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);

        JButton loginBtn = new JButton("Login");
        add(new JLabel(""));
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && email.contains("@")) {
                dispose();
                new UserPanel(name, email);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid name or email.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// === Admin Login ===
class AdminLogin extends JFrame {
    public AdminLogin() {
        setTitle("Admin Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JTextField adminField = new JTextField();
        JPasswordField passField = new JPasswordField();

        add(new JLabel("Admin ID:"));
        add(adminField);
        add(new JLabel("Password:"));
        add(passField);

        JButton loginBtn = new JButton("Login");
        add(new JLabel(""));
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            String admin = adminField.getText();
            String pass = new String(passField.getPassword());

            if (admin.equals("admin") && pass.equals("admin123")) {
                dispose();
                new AdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect credentials.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// === User Panel ===
class UserPanel extends JFrame implements ActionListener {
    private JTextField ageField;
    private JComboBox<String> genderBox, candidateList;
    private JTextArea resultArea;
    private JButton registerButton, voteButton;

    private boolean isRegistered = false;
    private String username;
    private String email;

    private HashMap<String, Integer> votes;

    public UserPanel(String name, String email) {
        this.username = name;
        this.email = email;

        setTitle("User Panel - " + name);
        setSize(600, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        votes = new HashMap<>();
        String[] candidates = {"Shadan", "Piyash", "Ahsan", "Sahid"};
        for (String c : candidates) votes.put(c, 0);

        add(new JLabel("Name: " + name));
        add(new JLabel("Email: " + email));

        add(new JLabel("Age:"));
        ageField = new JTextField(5);
        add(ageField);

        add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        add(genderBox);

        add(new JLabel("Vote for:"));
        candidateList = new JComboBox<>(candidates);
        add(candidateList);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        add(registerButton);

        voteButton = new JButton("Vote");
        voteButton.addActionListener(this);
        voteButton.setEnabled(false);
        add(voteButton);

        resultArea = new JTextArea(12, 50);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String age = ageField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();

            if (age.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your age.");
                return;
            }

            isRegistered = true;
            voteButton.setEnabled(true);
            registerButton.setEnabled(false);
            ageField.setEditable(false);

            saveUserProfile(username, email, age, gender);
            JOptionPane.showMessageDialog(this, "Registration successful!");
        }

        else if (e.getSource() == voteButton && isRegistered) {
            String candidate = (String) candidateList.getSelectedItem();
            votes.put(candidate, votes.get(candidate) + 1);
            resultArea.setText("✅ You voted for: " + candidate);
            saveVoteRecord(username, candidate);
            voteButton.setEnabled(false);
        }
    }

    private void saveUserProfile(String name, String email, String age, String gender) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("user_profiles.txt", true)))) {
            out.println("Name: " + name + ", Email: " + email + ", Age: " + age + ", Gender: " + gender);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user profile.");
        }
    }

    private void saveVoteRecord(String name, String candidate) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("votes.txt", true)))) {
            out.println("Name: " + name + ", Voted for: " + candidate);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving vote record.");
        }
    }
}

// === Admin Panel with GridLayout ===
class AdminPanel extends JFrame {
    private JTextArea adminArea;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(800, 450); // Increased size to fit all buttons
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        adminArea = new JTextArea();
        adminArea.setEditable(false);

        JButton viewUsersBtn = new JButton("View Registered Users");
        JButton resetVotesBtn = new JButton("Reset All Votes");
        JButton viewVotesBtn = new JButton("See Who Voted for Whom");
        JButton totalVotesBtn = new JButton("Show Total Votes");

        // View Registered Users
        viewUsersBtn.addActionListener(e -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("user_profiles.txt"))) {
                StringBuilder sb = new StringBuilder("👤 Registered Users:\n");
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                adminArea.setText(sb.toString());
            } catch (IOException ex) {
                adminArea.setText("⚠️ Could not read user profiles.");
            }
        });

        // Reset Votes
        resetVotesBtn.addActionListener(e -> {
            try (PrintWriter writer = new PrintWriter("votes.txt")) {
                writer.print(""); // Clear vote file
            } catch (IOException ex) {
                adminArea.setText("⚠️ Failed to clear vote records.");
                return;
            }
            adminArea.setText("✅ All votes have been reset.");
        });

        // View Who Voted for Whom
        viewVotesBtn.addActionListener(e -> {
            File voteFile = new File("votes.txt");
            if (!voteFile.exists() || voteFile.length() == 0) {
                adminArea.setText("❗ No votes have been recorded yet.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(voteFile))) {
                StringBuilder sb = new StringBuilder("🗳️ Voting Records:\n");
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                adminArea.setText(sb.toString());
            } catch (IOException ex) {
                adminArea.setText("⚠️ Could not read vote records.");
            }
        });

        // Show Total Votes
        totalVotesBtn.addActionListener(e -> {
            int count = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader("votes.txt"))) {
                while (reader.readLine() != null) {
                    count++;
                }
                adminArea.setText("📊 Total Votes Casted: " + count);
            } catch (IOException ex) {
                adminArea.setText("⚠️ Could not read vote records to count total.");
            }
        });

        // GridLayout to ensure visibility of all buttons
        JPanel topPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        topPanel.add(viewUsersBtn);
        topPanel.add(resetVotesBtn);
        topPanel.add(viewVotesBtn);
        topPanel.add(totalVotesBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(adminArea), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}