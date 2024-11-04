package fr.benvolat.gui;

import fr.benvolat.models.Mission;
import fr.benvolat.models.User;
import fr.benvolat.service.MissionService;
import fr.benvolat.service.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.util.ArrayList;

import static fr.benvolat.gui.BenevoleGUI.getMouseAdapter;

public class UserGUI extends JFrame {
    private JPanel panel;
    private JLabel topLabel;
    private JTextField missionNameTextField;
    private JTextArea missionDescriptionTextArea;
    private JButton resetButton;
    private JButton demanderButton;
    private JButton seDeconnecterButton;
    private JTable missionsTable;
    private JLabel missionsTableLabel;
    private JButton voirDemandesButton;
    private JButton donnerMonAvisButton;
    private JButton soumettreButton;
    private JTextArea reviewTextArea;
    private JScrollBar scrollBar1;
    private final UserService userService;
    private final MissionService missionService;
    private User userConnected;
    private MainInterface mainInterface;


    public UserGUI(MainInterface mainInterface, User user) throws SQLException {
        this.mainInterface = mainInterface;
        this.userConnected = user;
        userService = new UserService();
        missionService = new MissionService();

        missionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    Color back = table.isRowSelected(row) ? Color.GREEN : table.getBackground();
                    setBackground(back);
                } else {
                    setBackground(table.getBackground());
                }
                return this;
            }
        });


        // Disable row selection by overriding the ListSelectionModel
        missionsTable.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // Prevent selection of the first row (index 0)
                if (index0 == 0 || index1 == 0) {
                    // Do nothing if the first row is selected
                    return;
                }
                super.setSelectionInterval(index0, index1);
            }
        });

        seDeconnecterButton.addActionListener(e -> {
            resetUserGUI();
            mainInterface.reset();
            mainInterface.showPanel("MainMenu", null);
        });

        resetButton.addActionListener(e -> {
            missionNameTextField.setText("");
            missionDescriptionTextArea.setText("");
            reviewTextArea.setText("");
        });

        donnerMonAvisButton.addActionListener(e -> {
            reviewTextArea.setVisible(true);
            soumettreButton.setVisible(true);
        });

        soumettreButton.addActionListener(e -> {
            String review = reviewTextArea.getText();
            if (review.isEmpty()) {
                JOptionPane.showMessageDialog(mainInterface, "You have to write a review", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean isSent = userService.sendRewiew(userConnected.getUserID(), review);
                if (isSent) {
                    JOptionPane.showMessageDialog(mainInterface, "Your review was successfully sent", "Sent review", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainInterface, "Your review was not sent", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            reviewTextArea.setText("");
            reviewTextArea.setVisible(false);
            soumettreButton.setVisible(false);
        });

        scrollBar1.addAdjustmentListener(e -> {
            // Move the panel up/dow
            mainInterface.getMainPanel().setLocation(0, -e.getValue());
        });

        demanderButton.addActionListener(e -> {
            String missionName = missionNameTextField.getText();
            String missionDescription = missionDescriptionTextArea.getText();
            if (missionName.isEmpty() || missionDescription.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Veuillez s'il vous plait remplir tous les champs pour pouvoir confirmer votre demande");
            } else {
                boolean isSubmitted = missionService.submitMissionRequest(missionName, userConnected.getUserID(), missionDescription);
                if (isSubmitted) {
                    JOptionPane.showMessageDialog(panel, "Votre demande d'aide a bien ete enregistre, nos moderateurs vous informerons de la confirmation de votre demande ");
                } else {
                    JOptionPane.showMessageDialog(panel, "Votre demande d'aide n'a pas pu etre enregistre, vueillez s'il vous plait reassayer !!");
                }
            }
        });

        voirDemandesButton.addActionListener(e -> {

            DefaultTableModel tableModel = new DefaultTableModel(MainInterface.columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableModel.setColumnIdentifiers(MainInterface.columnNames);
            tableModel.addRow(MainInterface.columnNames);
            String[][] missionsData = missionService.getMissionsDataByUserId(userConnected.getUserID());
            for (String[] missionsDatum : missionsData) {
                tableModel.addRow(missionsDatum);
            }

            // Set the table model to the JTable
            missionsTable.setModel(tableModel);
            // Disable cell and row selection
            missionsTable.setCellSelectionEnabled(false);
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)

            MouseAdapter mouseAdapterTab = getMouseAdapter(missionsTable, 0);
            missionsTable.addMouseListener(mouseAdapterTab);

            missionsTableLabel.setVisible(true);

        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setUserConnected(User userConnected) {
        this.userConnected = userConnected;
    }

    public void resetUserGUI() {
        missionNameTextField.setText("");
        missionDescriptionTextArea.setText("");
        reviewTextArea.setText("");
        reviewTextArea.setVisible(false);
        soumettreButton.setVisible(false);
        missionsTableLabel.setVisible(false);
    }


    public ArrayList<JTextComponent> getTextComponents() {
        ArrayList<JTextComponent> textComponents = new ArrayList<>();
        textComponents.add(missionNameTextField);
        textComponents.add(missionDescriptionTextArea);
        return textComponents;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 5, new Insets(10, 10, 10, 10), -1, -1));
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createTitledBorder(null, "Volunteer App", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        topLabel = new JLabel();
        topLabel.setText("Creer une demande d'aide");
        panel.add(topLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionNameTextField = new JTextField();
        panel.add(missionNameTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        missionDescriptionTextArea = new JTextArea();
        missionDescriptionTextArea.setLineWrap(true);
        missionDescriptionTextArea.setWrapStyleWord(true);
        panel.add(missionDescriptionTextArea, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nom de l'aide");
        panel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Description");
        panel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset");
        panel.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        demanderButton = new JButton();
        demanderButton.setText("Demander");
        panel.add(demanderButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seDeconnecterButton = new JButton();
        seDeconnecterButton.setText("Se déconnecter");
        panel.add(seDeconnecterButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionsTable = new JTable();
        panel.add(missionsTable, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        missionsTableLabel = new JLabel();
        missionsTableLabel.setText("Mes demandes d'aide");
        missionsTableLabel.setVisible(false);
        panel.add(missionsTableLabel, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voirDemandesButton = new JButton();
        voirDemandesButton.setLabel("Voir demandes");
        voirDemandesButton.setText("Voir demandes");
        panel.add(voirDemandesButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        donnerMonAvisButton = new JButton();
        donnerMonAvisButton.setText("Donner mon avis");
        panel.add(donnerMonAvisButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        soumettreButton = new JButton();
        soumettreButton.setText("Soumettre");
        soumettreButton.setVisible(false);
        panel.add(soumettreButton, new com.intellij.uiDesigner.core.GridConstraints(8, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reviewTextArea = new JTextArea();
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setVisible(false);
        panel.add(reviewTextArea, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        scrollBar1 = new JScrollBar();
        panel.add(scrollBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 9, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label1.setLabelFor(missionNameTextField);
        label2.setLabelFor(missionDescriptionTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }


}
