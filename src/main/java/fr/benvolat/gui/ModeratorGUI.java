package fr.benvolat.gui;

import fr.benvolat.models.Mission;
import fr.benvolat.models.User;
import fr.benvolat.service.MissionService;
import fr.benvolat.service.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModeratorGUI extends JFrame {
    private JButton seDeconnecterButton;
    private JButton soumettreButton;
    private JTable missionsTable;
    private JLabel missionTableLabel;
    private JPanel panel;
    private JButton donnerMonAvisButton;
    private JTextArea reviewTextArea;
    private JScrollBar scrollBar1;
    private static MissionService missionService;
    private static UserService userService;
    private static User userConnected;
    private MainInterface mainInterface;


    public JPanel getPanel() {
        return this.panel;
    }

    public JTable getMissionsTable() {
        return this.missionsTable;
    }

    public void setUserConnected(User user) {
        userConnected = user;
    }

    public ModeratorGUI(MainInterface mainInterface, User user) throws SQLException {
        this.mainInterface = mainInterface;
        userConnected = user;
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

        missionsTable.addMouseListener(getMouseAdapterForModerator(missionsTable));

        DefaultTableModel tableModel = new DefaultTableModel(MainInterface.columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadData(tableModel, missionsTable);

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

    }

    private MouseAdapter getMouseAdapterForModerator(JTable missionsTable) {
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
                        showMissionModalForModerator((String) missionId, (String) missionName, (String) missionDescription, (String) missionStatus);
                    }
                }
            }
        };
    }

    public ArrayList<JTextComponent> getTextComponents() {
        ArrayList<JTextComponent> textComponents = new ArrayList<>();
        textComponents.add(reviewTextArea);
        return textComponents;
    }

    private void showModalForMotifRefus(JDialog dialog, Mission mission) {
        // Create the second modal dialog
        JDialog secondDialog = new JDialog(dialog, "Motif de refus", true);
        secondDialog.setSize(300, 200);
        secondDialog.setLocationRelativeTo(null);

        // Text area and submit button for the second dialog
        JTextArea textArea = new JTextArea(5, 20);
        JButton submitButton = new JButton("Submit");

        // Action listener for the submit button to close both modals
        submitButton.addActionListener(e -> {
            String motifRefus = textArea.getText();
            if (!motifRefus.isEmpty()) {
                boolean good = missionService.moderateMission(mission.getMissionID(), Mission.STATUS.REFUSED, motifRefus);
                if (good) {
                    JOptionPane.showMessageDialog(secondDialog, "Motif de refus confirmé", "Validé", JOptionPane.INFORMATION_MESSAGE);
                    secondDialog.dispose();
                    dialog.dispose();
                    refreshTableData((DefaultTableModel) this.getMissionsTable().getModel(), getMissionsTable());
                } else {
                    JOptionPane.showMessageDialog(secondDialog, "Erreur lors de la requete , vueillez reassayer !!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(secondDialog, "Veuillez entrer un motif de refus s'il vous plait", "Error motif vide", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout for the second dialog
        secondDialog.setLayout(new BorderLayout());
        secondDialog.add(new JScrollPane(textArea), BorderLayout.CENTER);
        secondDialog.add(submitButton, BorderLayout.SOUTH);

        // Show the second dialog
        secondDialog.setVisible(true);
    }

    // Method to show modal dialog with mission details
    private void showMissionModalForModerator(String missionID, String missionName, String missionDescription, String missionStatus) {
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
        JButton refuseButton = new JButton("Refuse");

        // Add action listeners to buttons
        refuseButton.addActionListener(e -> {
            dialog.dispose();
            Mission miss = new Mission(Integer.parseInt(missionID), missionName, 0, missionDescription, missionStatus, 0, "");
            this.showModalForMotifRefus(dialog, miss);
        });
        acceptButton.addActionListener(e -> {
            boolean isAssigned = missionService.moderateMission(Integer.parseInt(missionID), Mission.STATUS.VALIDATE, null);

            if (isAssigned) {
                JOptionPane.showMessageDialog(dialog, "Mission moderated!", "Modaration okay", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Error occurred during the request !", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
            refreshTableData((DefaultTableModel) this.getMissionsTable().getModel(), getMissionsTable());
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
        buttonPanel.add(refuseButton);

        dialog.add(buttonPanel, gbc);
        // Show the dialog
        dialog.setVisible(true);
    }

    private static void refreshTableData(DefaultTableModel tableModel, JTable missionsTable) {
        // Clear existing data
        tableModel.setRowCount(0);
        loadData(tableModel, missionsTable); // Load fresh data from the data source (e.g., database)
    }

    private static void loadData(DefaultTableModel tableModel, JTable missionsTable) {
        tableModel.setColumnIdentifiers(MainInterface.columnNames);
        tableModel.addRow(MainInterface.columnNames);
        String[][] missionsData = missionService.getMissionsDataByStatus(Mission.STATUS.PENDING.toString());
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(10, 10, 10, 10), -1, -1));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Moderateur page", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        seDeconnecterButton = new JButton();
        seDeconnecterButton.setText("Se déconnecter");
        panel.add(seDeconnecterButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        missionsTable = new JTable();
        panel.add(missionsTable, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        missionTableLabel = new JLabel();
        missionTableLabel.setText("Missions à modérer");
        missionTableLabel.setVisible(true);
        panel.add(missionTableLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        reviewTextArea = new JTextArea();
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setVisible(false);
        panel.add(reviewTextArea, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        soumettreButton = new JButton();
        soumettreButton.setText("Soumettre");
        soumettreButton.setVisible(false);
        panel.add(soumettreButton, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollBar1 = new JScrollBar();
        panel.add(scrollBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        donnerMonAvisButton = new JButton();
        donnerMonAvisButton.setText("Donner mon avis");
        panel.add(donnerMonAvisButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
