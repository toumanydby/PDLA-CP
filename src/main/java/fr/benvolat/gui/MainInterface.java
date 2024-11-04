package fr.benvolat.gui;

import fr.benvolat.models.User;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static ArrayList<JFrame> frames = new ArrayList<>();
    private final UserGUI userGUI;
    private final BenevoleGUI benevoleGUI;
    private final ModeratorGUI moderatorGUI;
    public static final int frameWidth = 600;
    public static final int frameHeight = 400;

    // Column Names
    public static String[] columnNames = {"id", "Nom", "Description", "Status"};

    public MainInterface() throws SQLException {
        // Set title and default close operation
        setTitle("Volunteer App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(frameWidth, frameHeight));
        setLocationRelativeTo(null);  // Center the window
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create instances of your other UI panels
        RegisterUI registerUI = new RegisterUI(this);  // Pass MainInterface as argument
        SignInUI signInUI = new SignInUI(this);
        userGUI = new UserGUI(this, null);
        benevoleGUI = new BenevoleGUI(this, null);
        moderatorGUI = new ModeratorGUI(this, null);

        frames.add(this);
        frames.add(registerUI);
        frames.add(signInUI);
        frames.add(userGUI);
        frames.add(benevoleGUI);
        frames.add(moderatorGUI);

        // Add panels to contentPanel with unique identifiers
        mainPanel.add(createMainMenuPanel(), "MainMenu");
        mainPanel.add(registerUI.getPanel(), "Register");
        mainPanel.add(signInUI.getPanel(), "SignIn");
        mainPanel.add(userGUI.getPanel(), "User");
        mainPanel.add(benevoleGUI.getPanel(), "Benevole");
        mainPanel.add(moderatorGUI.getPanel(), "Moderator");

        add(mainPanel);
        // Set default panel
        showPanel("MainMenu", null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JFrame getFrame(int position) {
        return frames.get(position);
    }


    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));  // Two rows, one column
        // Load the logo image
        ImageIcon logoIcon = new ImageIcon("src/main/resources/images/logo.png");  // Replace with the actual path to your logo

        // Resize the image if needed (optional)
        Image logoImage = logoIcon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH); // Resizing the image
        ImageIcon resizedLogoIcon = new ImageIcon(logoImage);

        // Create a JLabel to hold the logo
        JLabel logoLabel = new JLabel(resizedLogoIcon);

        // Center the logo within the panel (optional)
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.CENTER);

        JButton registerButton = new JButton("Register");
        JButton signInButton = new JButton("Sign In");

        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBounds(15, 15, 300, 150);
        signInButton.setBounds(15, 15, 300, 150);
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(registerButton);
        buttonPanel.add(signInButton);

        // Add the logo to the top of the panel
        panel.add(logoLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // Switch to Register panel
        registerButton.addActionListener(e -> showPanel("Register", null));
        // Switch to Sign In panel
        signInButton.addActionListener(e -> showPanel("SignIn", null));

        return panel;
    }

    public void showPanel(String title, User user) {
        switch (title) {
            case "User" -> userGUI.setUserConnected(user);
            case "Benevole" -> benevoleGUI.setUserConnected(user);
            //case "Admin" ->
            case "Moderator" -> moderatorGUI.setUserConnected(user);
        }
        cardLayout.show(mainPanel, title);
    }

    public void reset() {
        // for all JTextFields and JTextAreas
        ArrayList<JTextComponent> textComponents = null;
        for (JFrame frame : frames) {
            if (frame instanceof RegisterUI) {
                textComponents = ((RegisterUI) frame).getTextComponents();
            } else if (frame instanceof SignInUI) {
                textComponents = ((SignInUI) frame).getTextComponents();
            } else if (frame instanceof UserGUI) {
                textComponents = ((UserGUI) frame).getTextComponents();
            } else if (frame instanceof fr.benvolat.gui.ModeratorGUI) {
                textComponents = ((fr.benvolat.gui.ModeratorGUI) frame).getTextComponents();
            }

            if (textComponents != null) {
                for (JTextComponent textComponent : textComponents) {
                    textComponent.setText("");
                }
            }
        }
    }
}
