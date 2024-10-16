package fr.benvolat.gui;

import fr.benvolat.models.Mission;
import fr.benvolat.models.User;
import fr.benvolat.service.MissionService;
import fr.benvolat.service.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class BenevoleGUI extends JFrame {
    private JButton seDeconnecterButton;
    private JButton voirMissionsButton;
    private JButton voirMissionsRealiseesButton;
    private JTable missionsTable;
    private JButton voirMissionsEnCoursButton;
    private JLabel missionTableLabel;
    private JPanel panel;
    private static MissionService missionService;
    private static UserService userService;
    private static User userConnected;
    private MainInterface mainInterface;

    public BenevoleGUI(MainInterface mainInterface, User user) throws SQLException {
        this.mainInterface = mainInterface;
        userConnected = user;
        userService = new UserService();
        missionService = new MissionService();


        seDeconnecterButton.addActionListener(e -> {
            mainInterface.reset();
            mainInterface.showPanel("MainMenu", null);
        });

        voirMissionsButton.addActionListener(e -> {
            DefaultTableModel tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableModel.setColumnIdentifiers(MainInterface.columnNames);
            tableModel.addRow(MainInterface.columnNames);
            String[][] missionsData = missionService.getMissionsDataByStatus(Mission.STATUS.VALIDATE.toString());
            for (String[] missionsDatum : missionsData) {
                tableModel.addRow(missionsDatum);
            }

            // Set the table model to the JTable
            missionsTable.setModel(tableModel);
            // Disable cell selection
            missionsTable.setCellSelectionEnabled(false);

            // Disable row selection by overriding the ListSelectionModel
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.setColumnSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
            missionTableLabel.setText("Missions");
            missionTableLabel.setVisible(true);
            missionTableLabel.setVisible(true);
            // Add mouse listener to detect double-click on rows
            missionsTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Check if the event is a double-click
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();  // Prevent further processing if needed

                        // Get the index of the clicked row
                        int row = missionsTable.getSelectedRow();

                        // If the row is valid (not -1), do something
                        if (row != -1) {
                            // Example action: print row data
                            Object missionId = missionsTable.getModel().getValueAt(row, 0);  // Get ID from the first column
                            Object missionName = missionsTable.getModel().getValueAt(row, 1);  // Get Name from the second column
                            Object missionDescription = missionsTable.getModel().getValueAt(row, 2);  // Get Age from the third column
                            Object missionStatus = missionsTable.getModel().getValueAt(row, 3);
                            System.out.println(missionId + " " + missionName + " \n" + missionDescription + " " + missionStatus);
                            showMissionModal((String) missionId, (String) missionName, (String) missionDescription, (String) missionStatus);
                        }
                    }
                }
            });

        });

        voirMissionsEnCoursButton.addActionListener(e -> {
            DefaultTableModel tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableModel.setColumnIdentifiers(MainInterface.columnNames);
            tableModel.addRow(MainInterface.columnNames);
            String[][] missionsData = missionService.getMissionsDataByVolunteer(userConnected.getUserID());
            for (String[] missionsDatum : missionsData) {
                tableModel.addRow(missionsDatum);
            }

            // Set the table model to the JTable
            missionsTable.setModel(tableModel);
            // Disable cell selection
            missionsTable.setCellSelectionEnabled(false);

            // Disable row selection by overriding the ListSelectionModel
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.setColumnSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
            missionTableLabel.setText("Missions en cours");
            missionTableLabel.setVisible(true);
            missionTableLabel.setVisible(true);
        });

        voirMissionsRealiseesButton.addActionListener(e -> {
            DefaultTableModel tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableModel.setColumnIdentifiers(MainInterface.columnNames);
            tableModel.addRow(MainInterface.columnNames);
            String[][] missionsData = missionService.getMissionsDataByVolunteerAndStatus(userConnected.getUserID(), Mission.STATUS.REALISED.toString());
            for (String[] missionsDatum : missionsData) {
                tableModel.addRow(missionsDatum);
            }

            // Set the table model to the JTable
            missionsTable.setModel(tableModel);
            // Disable cell selection
            missionsTable.setCellSelectionEnabled(false);

            // Disable row selection by overriding the ListSelectionModel
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.setColumnSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
            missionTableLabel.setText("Missions realisees");
            missionTableLabel.setVisible(true);
            missionTableLabel.setVisible(true);
        });
    }


    // Method to show modal dialog with mission details
    private static void showMissionModal(String missionID, String missionName, String missionDescription, String missionStatus) {
        // Create a modal dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Mission Details");
        dialog.setModal(true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);

        // Create labels and buttons for the dialog
        JLabel labelMissionName = new JLabel("Mission Name: " + missionName);
        JLabel labelMissionDescription = new JLabel("Description: " + missionDescription);
        JLabel labelMissionStatus = new JLabel("Status: " + missionStatus);

        JButton acceptButton = new JButton("Accept");
        JButton closeButton = new JButton("Close");

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(closeButton);

        // Add action listener to close button
        closeButton.addActionListener(e -> dialog.dispose());

        // Add action listener to accept button (you can add logic to accept the mission here)
        acceptButton.addActionListener(e -> {
            boolean isAssigned = missionService.assignVolunteerToMission(Integer.parseInt(missionID), userConnected.getUserID());

            if (isAssigned) {
                JOptionPane.showMessageDialog(dialog, "Mission accepted!", "Accepted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Error occurred when accepting the mission!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });

        // Add components to the dialog
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(labelMissionName);
        dialog.add(labelMissionDescription);
        dialog.add(labelMissionStatus);
        dialog.add(buttonPanel);

        // Show the dialog
        dialog.setVisible(true);
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void setUserConnected(User user) {
        userConnected = user;
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(10, 10, 10, 10), -1, -1));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Benevole page", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        seDeconnecterButton = new JButton();
        seDeconnecterButton.setText("Se déconnecter");
        panel.add(seDeconnecterButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        voirMissionsButton = new JButton();
        voirMissionsButton.setText("Voir missions");
        panel.add(voirMissionsButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionsTable = new JTable();
        panel.add(missionsTable, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        voirMissionsRealiseesButton = new JButton();
        voirMissionsRealiseesButton.setText("Voir missions realisées");
        panel.add(voirMissionsRealiseesButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voirMissionsEnCoursButton = new JButton();
        voirMissionsEnCoursButton.setText("Voir missions en cours");
        panel.add(voirMissionsEnCoursButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionTableLabel = new JLabel();
        missionTableLabel.setText("");
        missionTableLabel.setVisible(false);
        panel.add(missionTableLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
