package fr.benvolat;

import fr.benvolat.gui.MainInterface;
import fr.benvolat.utils.DBConnection;

import javax.swing.*;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) {

        DBConnection.initDB();
        // Launch the main app
        SwingUtilities.invokeLater(() -> {
            MainInterface main = new MainInterface();
            main.setVisible(true);
        });
    }
}