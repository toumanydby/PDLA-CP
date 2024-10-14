package fr.benvolat.dao;

import fr.benvolat.models.*;
import fr.benvolat.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class UserDataAccess {

    private final Connection connection;

    /**
     *
     * @throws SQLException
     */
    public UserDataAccess() throws SQLException {
        connection = DBConnection.getConnection();
    }

    public void addUser(User user) {
        String query = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getUserRole());

            int rowAffected = stmt.executeUpdate();
            if(rowAffected > 0){
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if(rs.next()){
                        user.setUserID(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());;
                }
                System.out.println( user.getUserRole() + " created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Retrouve un utilisateur a partir de son adresse mail
     * @param email adresse email de l'utilisateur que vous rechercher, ( email unique par user )
     * @return user trouve ou null
     */
    public User findUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE email = ?";
        User user = null;
        try(PreparedStatement st = connection.prepareStatement(query);){
            st.setString(1,email);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                user = getUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     *  Fonction permettant de trouver tous les utilisateurs
     * @return Une ArrayList contenant les differents User de l'appli
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> usersList = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()
        ) {
            User user = null;
            while (rs.next()) {
                user = getUser(rs);
                usersList.add(user);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usersList;
    }

    /**
     * Fonction nous permettant de retrouver un utilisateur a partir d'une requete vers la base de donne
     * @param rs Resultat de la requete sql executee
     * @return Utilisateur trouve en fonction de la requete ou null
     */
    private User getUser(ResultSet rs) {
        User user = null;
        try{
            user = switch (rs.getString("role")){
                case "ADMIN" -> new Admin(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                case "USER" -> new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                case "BENEVOLE" -> new Benevole(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                case "MODERATOR" -> new Moderateur(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                default -> user;
            };
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }



    public boolean updateUser(User user) {
        boolean bool = false;
        String query = "UPDATE INTO Missions_requests " +
                "SET name = ?," +
                " email = ?," +
                " password = ?," +
                " role = ?," +
                "WHERE id = ?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, user.getName());
            st.setString(2,user.getEmail());
            st.setString(3,user.getPassword());
            st.setString(4,user.getUserRole());
            st.executeUpdate();
            bool = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bool;
    }


    public boolean deleteUser(int userId) {
        boolean bool = false;
        String query = "DELETE FROM Users WHERE id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, userId);
            st.executeUpdate();
            bool = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bool;
    }
}
