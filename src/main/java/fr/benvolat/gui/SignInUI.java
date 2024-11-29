package fr.benvolat.gui;

import fr.benvolat.models.User;
import fr.benvolat.service.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import com.intellij.uiDesigner.core.*;

public class SignInUI extends JFrame {
    private JPanel panel;
    private JTextField userNameTextField;
    private JPasswordField passwordTextField;
    private JPasswordField confirmPasswordTextField;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel confPasswordLabel;
    private JButton resetButtuon;
    private JButton signInButton;
    private JButton backButton;

    private final UserService userService;

    private MainInterface mainInterface;

    public SignInUI(MainInterface mainInterface) throws SQLException {
        this.mainInterface = mainInterface;
        userService = new UserService();

        setupUI();

        backButton.addActionListener(e -> {
            mainInterface.reset();
            mainInterface.showPanel("MainMenu", null);
        });

        signInButton.addActionListener(actionEvent -> {
            String userName = userNameTextField.getText();
            String password = passwordTextField.getText();

            if (!password.equals(confirmPasswordTextField.getText())) {
                JOptionPane.showMessageDialog(panel, "Les mots de passe entres ne sont pas valides ", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                User userAuthenticated = userService.authenticateUser(userName, password);

                if (userAuthenticated != null) {
                    switch (userAuthenticated.getUserRole()) {
                        case "ADMIN" -> mainInterface.showPanel("Admin", userAuthenticated);
                        case "USER" -> mainInterface.showPanel("User", userAuthenticated);
                        case "MODERATOR" -> mainInterface.showPanel("Moderator", userAuthenticated);
                        case "BENEVOLE" -> mainInterface.showPanel("Benevole", userAuthenticated);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Erreur de connexion", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        resetButtuon.addActionListener(actionEvent -> {
            userNameTextField.setText("");
            passwordTextField.setText("");
            confirmPasswordTextField.setText("");
        });
    }

    public void resetSignInGUI() {
        userNameTextField.setText("");
        passwordTextField.setText("");
        confirmPasswordTextField.setText("");
    }

    public JPanel getPanel() {
        return panel;
    }

    public ArrayList<JTextComponent> getTextComponents() {
        ArrayList<JTextComponent> textComponents = new ArrayList<>();
        textComponents.add(userNameTextField);
        textComponents.add(passwordTextField);
        textComponents.add(confirmPasswordTextField);
        return textComponents;
    }

    private void setupUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(5, 3, new Insets(10, 10, 10, 10), -1, -1));
        panel.setVerifyInputWhenFocusTarget(true);
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createTitledBorder(null, "Sign In", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.getFont(null, Font.BOLD, 20, panel.getFont()), null));
        userNameTextField = new JTextField();
        panel.add(userNameTextField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordTextField = new JPasswordField();
        panel.add(passwordTextField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        confirmPasswordTextField = new JPasswordField();
        panel.add(confirmPasswordTextField, new GridConstraints(2, 1, 2, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        userNameLabel = new JLabel();
        userNameLabel.setText("Username");
        panel.add(userNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        panel.add(passwordLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        confPasswordLabel = new JLabel();
        confPasswordLabel.setText("Confirm password");
        panel.add(confPasswordLabel, new GridConstraints(2, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        resetButtuon = new JButton();
        resetButtuon.setText("Reset");
        panel.add(resetButtuon, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        signInButton = new JButton();
        signInButton.setText("Sign in");
        panel.add(signInButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Label");
        label1.setVisible(false);
        panel.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel.add(backButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }
}
