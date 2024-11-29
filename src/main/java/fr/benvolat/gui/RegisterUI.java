package fr.benvolat.gui;

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
import java.util.Objects;
import com.intellij.uiDesigner.core.*;

public class RegisterUI extends JFrame {
    private JPanel panel;
    private JTextField nameTextField;
    private JTextField userNameTextField;
    private JPasswordField passwordTextField;
    private JPasswordField confirmPasswordTextField;
    private JLabel nomLabel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel confPasswordLabel;
    private JLabel roleLabel;
    private JComboBox comboBox1;
    private JButton resetButtuon;
    private JButton signUpButton;
    private JButton backButton;
    private final UserService userService = new UserService();

    public RegisterUI(MainInterface mainInterface) throws SQLException {
        setupUI();

        backButton.addActionListener(e -> mainInterface.showPanel("MainMenu", null));

        signUpButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String userName = userNameTextField.getText();
            String password = String.valueOf(passwordTextField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordTextField.getPassword());
            String role = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();

            if (role.equalsIgnoreCase("DEMANDEUR")) {
                role = "USER";
            }
            if (role.equalsIgnoreCase("MODERATEUR")) {
                role = "MODERATOR";
            }

            if (name.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(mainInterface, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(mainInterface, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean isRegistered = userService.registerUser(name, userName, password, role);
                    if (isRegistered) {
                        JOptionPane.showMessageDialog(panel, "User registered", "Success", JOptionPane.INFORMATION_MESSAGE);
                        mainInterface.showPanel("SignIn", null);
                    } else {
                        JOptionPane.showMessageDialog(mainInterface, "Error on the registration, maybe the userName entered is already used, please retry !", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        resetButtuon.addActionListener(e -> {
            resetRegisterGUI();
        });
    }

    public void resetRegisterGUI() {
        nameTextField.setText("");
        userNameTextField.setText("");
        passwordTextField.setText("");
        confirmPasswordTextField.setText("");
    }

    public JPanel getPanel() {
        return panel;
    }

    public ArrayList<JTextComponent> getTextComponents() {
        ArrayList<JTextComponent> textComponents = new ArrayList<>();
        textComponents.add(nameTextField);
        textComponents.add(userNameTextField);
        textComponents.add(passwordTextField);
        textComponents.add(confirmPasswordTextField);
        return textComponents;
    }

    private void setupUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(6, 3, new Insets(10, 10, 10, 10), -1, -1));
        panel.setEnabled(true);
        panel.setPreferredSize(new Dimension(500, 400));
        panel.putClientProperty("html.disable", Boolean.FALSE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-2104859)), "Sing up", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.getFont("JetBrains Mono", Font.BOLD, 18, panel.getFont()), null));
        nameTextField = new JTextField();
        panel.add(nameTextField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        userNameTextField = new JTextField();
        panel.add(userNameTextField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordTextField = new JPasswordField();
        panel.add(passwordTextField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        confirmPasswordTextField = new JPasswordField();
        panel.add(confirmPasswordTextField, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        nomLabel = new JLabel();
        nomLabel.setText("Nom");
        panel.add(nomLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        userNameLabel = new JLabel();
        userNameLabel.setText("Username");
        panel.add(userNameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        panel.add(passwordLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        confPasswordLabel = new JLabel();
        confPasswordLabel.setText("Confirm password");
        panel.add(confPasswordLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        roleLabel = new JLabel();
        roleLabel.setText("Role");
        roleLabel.setToolTipText("Utilisé pour savoir si on est benevole ou demandeur");
        panel.add(roleLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(76, 17), null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("BENEVOLE");
        defaultComboBoxModel1.addElement("DEMANDEUR");
        defaultComboBoxModel1.addElement("MODERATEUR");
        comboBox1.setModel(defaultComboBoxModel1);
        panel.add(comboBox1, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButtuon = new JButton();
        resetButtuon.setText("Reset");
        panel.add(resetButtuon, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        signUpButton = new JButton();
        signUpButton.setText("Sign up");
        panel.add(signUpButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel.add(backButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
