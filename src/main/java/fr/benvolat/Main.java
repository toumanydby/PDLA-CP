package fr.benvolat;

import fr.benvolat.gui.MainInterface;
import fr.benvolat.utils.DBConnection;

import javax.swing.*;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) {
        AtomicReference<MainInterface> main = new AtomicReference<>();
        DBConnection.initDB();

        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                main.set(new MainInterface());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            main.get().setVisible(true);
        });
    }
}