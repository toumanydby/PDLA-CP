package fr.benvolat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_015";
    private static final String user = "projet_gei_015";
    private static final String password = "oFie9oov";

    /**
     * Methode permettant de se connecter a la bdd
     *
     * @return Connection entity, that will give us the possibility to modify our DB
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static void initDB() {
        String createTableUserQuery = "CREATE TABLE IF NOT EXISTS Users (" +
                "    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(255) NOT NULL," +
                "    email VARCHAR(255) NOT NULL UNIQUE," +
                "    password VARCHAR(255) NOT NULL," +
                "    role VARCHAR(50) NOT NULL" +
                ")";

        String createTableMissionRequestQuery = "CREATE TABLE IF NOT EXISTS Missions_requests (" +
                "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "requester_id INT NOT NULL," +
                "description TEXT NOT NULL," +
                "status VARCHAR(50) NOT NULL," +
                "volunteer_id INT," +
                "motif_refus TEXT," +
                "FOREIGN KEY (requester_id) REFERENCES Users(id)," +
                "FOREIGN KEY (volunteer_id) REFERENCES Users(id))";

        String createTableUsersReviews = "CREATE TABLE IF NOT EXISTS UsersReviews (" +
                "    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "    user_id INT," +
                "    review TEXT NOT NULL," +
                "    FOREIGN KEY (user_id) REFERENCES Users(id)" +
                ")";

        try (Connection conn = getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(createTableUserQuery);
             PreparedStatement stmt2 = conn.prepareStatement(createTableMissionRequestQuery);
             PreparedStatement stmt3 = conn.prepareStatement(createTableUsersReviews)
        ) {

            if (stmt1.executeUpdate() == 1) {
                System.out.println("Table Users created");
            }
            if (stmt2.executeUpdate() == 1) {
                System.out.println("Table Missions created");
            }
            if (stmt3.executeUpdate() == 1) {
                System.out.println("Table UsersReviews created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
