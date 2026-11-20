import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class OnlineVotingSystem extends JFrame implements ActionListener {
    private JTextField nameField, emailField, ageField;
    private JComboBox<String> genderBox, candidateList;
    private JButton registerButton, voteButton, resultsButton, adminPanelButton;
    private JTextArea resultArea;

    private HashMap<String, Integer> votes;
    private boolean isRegistered = false;
    private String registeredUser = "";
    private String email;

    public OnlineVotingSystem() {
        setTitle("Online Voting System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Initialize vote counts
        votes = new HashMap<>();
        String[] candidates = {"Alice", "Bob", "Charlie"};
        for (String candidate : candidates) {
            votes.put(candidate, 0);
        }

        // User Inputs
        add(new JLabel("Name:"));
        nameField = new JTextField(15);
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField(15);
        add(emailField);

        add(new JLabel("Age:"));
        ageField = new JTextField(5);
        add(ageField);

        add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Other"};
        genderBox = new JComboBox<>(genders);
        add(genderBox);

        add(new JLabel("Choose Candidate:"));
        candidateList = new JComboBox<>(candidates);
        add(candidateList);

        // Buttons
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        add(registerButton);

        voteButton = new JButton("Vote");
        voteButton.addActionListener(this);
        voteButton.setEnabled(false);  // Only enabled after registration
        add(voteButton);

        resultsButton = new JButton("Show Results");
        resultsButton.addActionListener(this);
        add(resultsButton);

        adminPanelButton = new JButton("Open Admin Panel");
        adminPanelButton.addActionListener(this);
        add(adminPanelButton);

        // Output Area
        resultArea = new JTextArea(12, 50);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String name = nameField.getText().trim();
            email = emailField.getText().trim();
            String age = ageField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || age.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            registeredUser = name;
            isRegistered = true;
            voteButton.setEnabled(true);
            registerButton.setEnabled(false);
            nameField.setEditable(false);
            emailField.setEditable(false);
            ageField.setEditable(false);

            saveUserProfile(name, email, age, gender);
            JOptionPane.showMessageDialog(this, "Registration successful! Welcome " + name);
        }

        else if (e.getSource() == voteButton && isRegistered) {
            String selected = (String) candidateList.getSelectedItem();
            votes.put(selected, votes.get(selected) + 1);
            JOptionPane.showMessageDialog(this, registeredUser + ", your vote for " + selected + " has been recorded.");
            resultArea.setText("You voted for: " + selected);
        }

        else if (e.getSource() == resultsButton) {
            displayResults();
        }

        else if (e.getSource() == adminPanelButton) {
            openAdminPanel();
        }
    }

    private void displayResults() {
        StringBuilder sb = new StringBuilder("📊 Voting Results:\n");
        for (String candidate : votes.keySet()) {
            sb.append(candidate).append(": ").append(votes.get(candidate)).append(" votes\n");
        }
        resultArea.setText(sb.toString());
    }

    private void openAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(500, 400);
        adminFrame.setLayout(new BorderLayout());

        JTextArea adminArea = new JTextArea();
        adminArea.setEditable(false);

        JButton viewUsersButton = new JButton("View Registered Users");
        viewUsersButton.addActionListener(ae -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("user_profiles.txt"))) {
                StringBuilder userList = new StringBuilder("👥 Registered Users:\n");
                String line;
                while ((line = reader.readLine()) != null) {
                    userList.append(line).append("\n");
                }
                adminArea.setText(userList.toString());
            } catch (IOException ex) {
                adminArea.setText("Could not read user profiles.");
            }
        });

        JButton clearVotesButton = new JButton("Reset All Votes");
        clearVotesButton.addActionListener(ae -> {
            for (String candidate : votes.keySet()) {
                votes.put(candidate, 0);
            }
            adminArea.setText("✅ All votes have been reset.");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewUsersButton);
        buttonPanel.add(clearVotesButton);

        adminFrame.add(buttonPanel, BorderLayout.NORTH);
        adminFrame.add(new JScrollPane(adminArea), BorderLayout.CENTER);
        adminFrame.setVisible(true);
    }

    private void saveUserProfile(String name, String email, String age, String gender) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("user_profiles.txt", true)))) {
            out.println("Name: " + name + ", Email: " + email + ", Age: " + age +
                    ", Gender: " + gender);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user profile.");
        }
    }

    public static void main(String[] args) {
        new OnlineVotingSystem();  // Entry point
    }
}