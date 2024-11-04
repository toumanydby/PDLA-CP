package fr.benvolat.dao;

import fr.benvolat.models.Mission;
import fr.benvolat.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Types;

public class MissionDataAccess {

    private final Connection connection;

    public MissionDataAccess() throws SQLException {
        connection = DBConnection.getConnection();
    }

    public boolean addMissionRequest(Mission mission) {
        String query = "INSERT INTO Missions_requests (name, requester_id, description, status) VALUES (?, ?, ?, ?)";
        boolean bool = false;
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, mission.getName());
            statement.setInt(2, mission.getRequesterID());
            statement.setString(3, mission.getDescription());
            statement.setString(4, mission.getStatus());

            int rowAff = statement.executeUpdate();
            if (rowAff > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mission.setMissionID(generatedKeys.getInt(1));
                        bool = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bool;
    }

    public boolean updateMissionRequest(Mission mission) {
        boolean bool = false;
        String query = "UPDATE Missions_requests " +
                "SET name = ?," +
                " requester_id = ?," +
                " description = ?," +
                " status = ?," +
                " volunteer_id = ?," +
                " motif_refus = ? " +
                "WHERE id = ?";


        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, mission.getName());
            st.setInt(2, mission.getRequesterID());
            st.setString(3, mission.getDescription());
            st.setString(4, mission.getStatus());
            if(mission.getVolunteerID() == 0){
                st.setNull(5, Types.INTEGER);
            } else{
                st.setInt(5, mission.getVolunteerID());
            }
            st.setString(6, mission.getMotifRefus());
            st.setInt(7, mission.getMissionID());
            st.executeUpdate();
            bool = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bool;
    }

    public ArrayList<Mission> findMissionsRequestsByStatus(String status) {
        ArrayList<Mission> missions = new ArrayList<>();

        String query = "SELECT * FROM Missions_requests WHERE status = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, status);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                missions.add(getMissions(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return missions;
    }

    public Mission findMissionRequestById(int missionID) {
        Mission mission = null;
        String query = "SELECT * FROM Missions_requests WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, missionID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                mission = getMissions(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mission;
    }

    public ArrayList<Mission> getMissionsByUserId(int userId) {
        ArrayList<Mission> missionsList = new ArrayList<>();
        String query = "SELECT * FROM Missions_requests WHERE requester_id = ?";

        return getMissions(userId, missionsList, query);
    }

    public ArrayList<Mission> getMissionsByVolunteerId(int volunteerId) {
        ArrayList<Mission> missionsList = new ArrayList<>();
        String query = "SELECT * FROM Missions_requests WHERE volunteer_id = ?";

        return getMissions(volunteerId, missionsList, query);
    }

    public ArrayList<Mission> getMissionsByVolunteerIdAndStatus(int volunteerId, String status) {
        ArrayList<Mission> missionsList = new ArrayList<>();
        String query = "SELECT * FROM Missions_requests WHERE volunteer_id = ? AND status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, volunteerId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            Mission mission;
            while (rs.next()) {
                mission = getMissions(rs);
                missionsList.add(mission);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return missionsList;
    }

    private ArrayList<Mission> getMissions(int userId, ArrayList<Mission> missionsList, String query) {
        try (PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            Mission mission;
            while (rs.next()) {
                mission = getMissions(rs);
                missionsList.add(mission);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return missionsList;
    }

    /**
     * Fonction nous permettant de retrouver une mission a partir d'une requete vers la base de donne
     *
     * @param rs Resultat de la requete sql executee
     * @return mission trouve en fonction de la requete ou null
     */
    private Mission getMissions(ResultSet rs) {
        Mission mission = null;
        try {
            mission = new Mission(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("requester_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("volunteer_id"),
                    rs.getString("motif_refus")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return mission;
    }

}
