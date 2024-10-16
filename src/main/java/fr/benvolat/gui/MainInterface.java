package fr.benvolat.gui;

import fr.benvolat.models.User;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static ArrayList<JFrame> frames = new ArrayList<>();
    private RegisterUI registerUI;
    private SignInUI signInUI;
    private UserGUI userGUI;
    private BenevoleGUI benevoleGUI;
    public static final int frameWidth = 600;
    public static final int frameHeight = 400;

    // Column Names
    public static String[] columnNames = {"id", "Nom", "Description", "Status"};

    public MainInterface() throws SQLException {
        // Set title and default close operation
        setTitle("Volunteer App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(frameWidth,frameHeight));
        setLocationRelativeTo(null);  // Center the window
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create instances of your other UI panels
        registerUI = new RegisterUI(this);  // Pass MainInterface as argument
        signInUI = new SignInUI(this);
        userGUI = new UserGUI(this,null);
        benevoleGUI = new BenevoleGUI(this,null);

        frames.add(this);
        frames.add(registerUI);
        frames.add(signInUI);
        frames.add(userGUI);
        frames.add(benevoleGUI);

        // Add panels to contentPanel with unique identifiers
        mainPanel.add(createMainMenuPanel(), "MainMenu");
        mainPanel.add(registerUI.getPanel(), "Register");
        mainPanel.add(signInUI.getPanel(), "SignIn");
        mainPanel.add(userGUI.getPanel(), "User");
        mainPanel.add(benevoleGUI.getPanel(),"Benevole");

        add(mainPanel,BorderLayout.SOUTH);
        // Set default panel
        showPanel("MainMenu",null);
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }

    public JFrame getFrame( int position){
        return frames.get(position);
    }


    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));  // Two rows, one column

        JButton registerButton = new JButton("Register");
        JButton signInButton = new JButton("Sign In");

        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        signInButton.setFont(new Font("Arial", Font.PLAIN, 12));
//        registerButton.setSize(60, 72);
        registerButton.setBounds(10,10,250,100);
        signInButton.setBounds(10,10,250,100);
//        signInButton.setSize(42, 72);
        panel.add(registerButton);
        panel.add(signInButton);

        // Switch to Register panel
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("Register",null);
            }
        });

        // Switch to Sign In panel
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("SignIn",null);
            }
        });

        return panel;
    }

    public void showPanel(String title, User user) {
        switch (title) {
            case "User" -> userGUI.setUserConnected(user);
            case "Benevole" -> benevoleGUI.setUserConnected(user);
            //case "Admin" ->
            //case "Moderateur" ->
        }
        cardLayout.show(mainPanel, title);
    }


    public static JDialog createDialogModal(final JFrame frame, String title, String contenu) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setBounds(50,50,60,60);
        Container dialogContentPane = dialog.getContentPane();
        dialogContentPane.setLayout(new BorderLayout());
        JLabel txtBut = new JLabel(
                contenu,
                JLabel.CENTER
        );
        txtBut.setFont(new Font("Serif", Font.PLAIN, 10));
        txtBut.setForeground(new Color(0x111010));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JButton okButton = new JButton("CLOSE");
        okButton.addActionListener(actionEvent -> dialog.setVisible(false));
        panel.add(okButton);

        dialogContentPane.add(txtBut, BorderLayout.CENTER);
        dialogContentPane.add(panel, BorderLayout.SOUTH);
        dialog.setVisible(true);

        return dialog;
    }

    public void reset() {
        // for all JTextFields and JTextAreas
        ArrayList<JTextComponent> textComponents = null;
        for (JFrame frame:frames){
            if(frame instanceof RegisterUI){
                textComponents = ((RegisterUI) frame).getTextComponents();
            } else if(frame instanceof SignInUI){
                textComponents = ((SignInUI) frame).getTextComponents();
            } else if(frame instanceof UserGUI){
                textComponents = ((UserGUI) frame).getTextComponents();
            }/* else if(frame instanceof BenevoleGUI){
                ArrayList<JTextComponent> textComponents = ((BenevoleGUI) frame).getTextComponents();
            }*/

            if(textComponents != null){
                for (JTextComponent textComponent: textComponents){
                    textComponent.setText("");
                }
            }
        }
    }

    public static void main(String[] args)  {
        // Run the GUI in the Event Dispatch Thread

        SwingUtilities.invokeLater(()->{
            MainInterface mainInterface;
            try {
                mainInterface = new MainInterface();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            mainInterface.setVisible(true);
        });
    }


}
