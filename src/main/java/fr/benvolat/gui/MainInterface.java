package fr.benvolat.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {

    public static final int frameWidth = 800;
    public static final int frameHeight = 600;
    public static final int formWidth = 500;
    public static final int formHeight = 400;

    public MainInterface() {
        // Set title and default close operation
        setTitle("Volunteer App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(frameWidth,frameHeight));
        setLocationRelativeTo(null);  // Center the window

        // Create a panel to hold the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));  // Two rows, one column, with spacing

        // Create Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setSize(50, 50);
        panel.add(registerButton);

        // Create Sign In button
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.PLAIN, 18));
        signInButton.setSize(50, 50);
        panel.add(signInButton);

        // Add panel to the frame
        add(panel,BorderLayout.SOUTH);

        // Register action listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the registration form
                setContentPane(new RegisterUI().getPanel());
                //RegisterUI registerForm = new RegisterUI();

                //dispose();  // Close current window
            }
        });

        // Register action listener for Sign In button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the sign-in form

                setContentPane(new SignInUI().getPanel());
                //SignInUI singInForm = new SignInUI();
                //dispose();  // Close current window
            }
        });

        // Set the frame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(MainInterface::new);
    }
}
