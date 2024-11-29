package fr.benvolat.gui;

import fr.benvolat.models.Mission;
import fr.benvolat.models.User;
import fr.benvolat.service.MissionService;
import fr.benvolat.service.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import com.intellij.uiDesigner.core.*;


public class BenevoleGUI extends JFrame {

    private JButton seDeconnecterButton;
    private JButton voirMissionsButton;
    private static JTable missionsTable;
    private JButton voirMissionsEnCoursButton;
    private JLabel missionTableLabel;
    private JPanel panel;
    private JButton donnerMonAvisButton;
    private JTextArea reviewTextArea;
    private JButton soumettreButton;
    private JScrollBar scrollBar1;
    private static MissionService missionService;
    private static UserService userService;
    private static User userConnected;


    public JPanel getPanel() {
        return this.panel;
    }

    public void setUserConnected(User user) {
        userConnected = user;
    }

    public BenevoleGUI(MainInterface mainInterface, User user) throws SQLException {
        userConnected = user;
        userService = new UserService();
        missionService = new MissionService();
        
        setupUI();

        MouseAdapter mouseAdapterTab1 = getMouseAdapter(missionsTable, 1);
        MouseAdapter mouseAdapterTab2 = getMouseAdapter(missionsTable, 0);

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
            mainInterface.reset();
            mainInterface.showPanel("MainMenu", null);
        });

        donnerMonAvisButton.addActionListener(e -> {
            reviewTextArea.setVisible(true);
            soumettreButton.setVisible(true);
        });

        scrollBar1.addAdjustmentListener(e -> {
            // Move the panel up/dow
            mainInterface.getMainPanel().setLocation(0, -e.getValue());
        });

        soumettreButton.addActionListener(e -> {
            String review = reviewTextArea.getText();
            if (review.isEmpty()) {
                JOptionPane.showMessageDialog(mainInterface, "You hava to write a review", "Error", JOptionPane.ERROR_MESSAGE);
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

        voirMissionsButton.addActionListener(e -> {
            missionsTable.removeMouseListener(mouseAdapterTab2);
            missionsTable.addMouseListener(mouseAdapterTab1);


            DefaultTableModel tableModel = new DefaultTableModel(MainInterface.columnNames, 0) {
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
            // Disable cell and row selection
            missionsTable.setCellSelectionEnabled(false);
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
            missionTableLabel.setText("Missions");
            missionTableLabel.setVisible(true);
            missionTableLabel.setVisible(true);

        });

        voirMissionsEnCoursButton.addActionListener(e -> {
            missionsTable.removeMouseListener(mouseAdapterTab1);
            missionsTable.addMouseListener(mouseAdapterTab2);


            DefaultTableModel tableModel = new DefaultTableModel(MainInterface.columnNames, 0) {
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
            // Disable cell and row selection
            missionsTable.setCellSelectionEnabled(false);
            missionsTable.setRowSelectionAllowed(false);
            missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
            missionTableLabel.setText("Missions en cours");
            missionTableLabel.setVisible(true);
            missionTableLabel.setVisible(true);
        });
    }

    static MouseAdapter getMouseAdapter(JTable missionsTable, int indice) {
        return new MouseAdapter() {
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
                        showMissionModal((String) missionId, (String) missionName, (String) missionDescription, (String) missionStatus, indice);
                    }
                }
            }
        };
    }

    // Method to show modal dialog with mission details
    private static void showMissionModal(String missionID, String missionName, String missionDescription, String missionStatus, int indice) {
        // Create a modal dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Mission Details");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        // Create labels and text components
        JLabel labelMissionName = new JLabel("Mission Name: ");
        JTextArea missionNameTextArea = new JTextArea(3, 20);
        missionNameTextArea.setEditable(false);
        missionNameTextArea.setLineWrap(true);
        missionNameTextArea.setWrapStyleWord(true);
        missionNameTextArea.setText(missionName);
        JScrollPane missionNameScrollPane = new JScrollPane(missionNameTextArea); // Add scroll pane for the JTextArea

        JLabel labelMissionDescription = new JLabel("Description: ");
        JTextArea missionDescriptionTextArea = new JTextArea(5, 20);
        missionDescriptionTextArea.setEditable(false);
        missionDescriptionTextArea.setLineWrap(true);
        missionDescriptionTextArea.setWrapStyleWord(true);
        missionDescriptionTextArea.setText(missionDescription);
        JScrollPane missionDescriptionScrollPane = new JScrollPane(missionDescriptionTextArea); // Add scroll pane for the JTextArea

        JLabel labelMissionStatus = new JLabel("Status: ");
        JTextField missionStatusTextField = new JTextField(20);
        missionStatusTextField.setEditable(false);
        missionStatusTextField.setText(missionStatus);

        // Create buttons
        JButton acceptButton = new JButton("Accept");
        JButton closeButton = new JButton("Close");

        // Add action listeners to buttons
        closeButton.addActionListener(e -> dialog.dispose());
        acceptButton.addActionListener(e -> {
            boolean isAssigned = missionService.assignVolunteerToMission(Integer.parseInt(missionID), userConnected.getUserID());

            if (isAssigned) {
                JOptionPane.showMessageDialog(dialog, "Mission accepted!", "Accepted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Error occurred when accepting the mission!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
            /*switch (indice){
                case 0:
                    refreshTableData((DefaultTableModel) getMissionsTable().getModel(), getMissionsTable(),missionService.getMissionsDataByVolunteer(userConnected.getUserID()));
                    missionsTable.setVisible(true);
                    break;
                case 1:
                    refreshTableData((DefaultTableModel) getMissionsTable().getModel(), getMissionsTable(),missionService.getMissionsDataByStatus(Mission.STATUS.VALIDATE.toString()));
                    missionsTable.setVisible(true);
                    break;
            }*/
        });

        // Set up GridBagLayout
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Mission Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(labelMissionName, gbc);

        gbc.gridx = 1;
        dialog.add(missionNameScrollPane, gbc);

        // Row 2: Mission Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(labelMissionDescription, gbc);

        gbc.gridx = 1;
        dialog.add(missionDescriptionScrollPane, gbc);

        // Row 3: Mission Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(labelMissionStatus, gbc);

        gbc.gridx = 1;
        dialog.add(missionStatusTextField, gbc);

        // Row 4: Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(closeButton);

        if (indice != 0) dialog.add(buttonPanel, gbc);

        // Show the dialog
        dialog.setVisible(true);
    }

    private static JTable getMissionsTable() {
        return missionsTable;
    }


    private static void refreshTableData(DefaultTableModel tableModel, JTable missionsTable, String[][] missionsData) {
        // Clear existing data
        tableModel.setRowCount(0);
        loadData(tableModel, missionsTable, missionsData); // Load fresh data from the data source (e.g., database)
    }

    private static void loadData(DefaultTableModel tableModel, JTable missionsTable, String[][] missionsData) {
        tableModel.setColumnIdentifiers(MainInterface.columnNames);
        tableModel.addRow(MainInterface.columnNames);
        for (String[] missionsDatum : missionsData) {
            tableModel.addRow(missionsDatum);
        }

        // Set the table model to the JTable
        missionsTable.setModel(tableModel);
        // Disable cell and row selection
        missionsTable.setCellSelectionEnabled(false);
        missionsTable.setRowSelectionAllowed(false);
        missionsTable.removeColumn(missionsTable.getColumnModel().getColumn(0));  // This will hide the first column (ID)
        missionsTable.setVisible(true);
    }

    private void setupUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(6, 4, new Insets(10, 10, 10, 10), -1, -1));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Benevole page", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        seDeconnecterButton = new JButton();
        seDeconnecterButton.setText("Se déconnecter");
        panel.add(seDeconnecterButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        voirMissionsButton = new JButton();
        voirMissionsButton.setText("Voir missions");
        panel.add(voirMissionsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionsTable = new JTable();
        panel.add(missionsTable, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        voirMissionsEnCoursButton = new JButton();
        voirMissionsEnCoursButton.setText("Voir mes missions acceptées");
        panel.add(voirMissionsEnCoursButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionTableLabel = new JLabel();
        missionTableLabel.setText("");
        missionTableLabel.setVisible(false);
        panel.add(missionTableLabel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        donnerMonAvisButton = new JButton();
        donnerMonAvisButton.setText("Donner mon avis");
        panel.add(donnerMonAvisButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reviewTextArea = new JTextArea();
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setVisible(false);
        panel.add(reviewTextArea, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        soumettreButton = new JButton();
        soumettreButton.setText("Soumettre");
        soumettreButton.setVisible(false);
        panel.add(soumettreButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollBar1 = new JScrollBar();
        panel.add(scrollBar1, new GridConstraints(0, 3, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }


}
