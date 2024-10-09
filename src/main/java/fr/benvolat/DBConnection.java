package fr.benvolat;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_015";
    private static final String user = "projet_gei_015";
    private static final String password = "oFie9oov";
    // mysql -h srv-bdens.insa-toulouse.fr --port=3306 -u projet_gei_015 -p projet_gei_015

/**
    // Recuperation des informations de connection dans le fichier db.properties
    static {
        try {
            InputStream ip = DBConnection.class.getClassLoader().getResourceAsStream("db.properties");
            Properties prop = new Properties();
            prop.load(ip);
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/

    /**
     * Methode permettant de se connecter a la bdd
     * @return Connection entity, that will give us the possibility to modify our DB
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        //System.out.println(url);
        return DriverManager.getConnection(url,user,password);
    }
}
