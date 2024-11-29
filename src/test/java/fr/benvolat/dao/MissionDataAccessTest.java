package fr.benvolat.dao;

import org.junit.jupiter.api.*;
import fr.benvolat.models.Mission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MissionDataAccessTest {

    private MissionDataAccess missionDataAccess = new MissionDataAccess();

    @AfterEach
    public void tearDown() {
        String deleteQuery = "DELETE FROM Missions_requests WHERE name = ?";

        try (Connection con = missionDataAccess.getConnection();
            PreparedStatement statement = con.prepareStatement(deleteQuery)) {
            statement.setString(1, "Test Mission");
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error during tearDown: " + e.getMessage());
        }
    }
    @Test
    public void testAddMissionRequest() {
        Mission mission = new Mission();
        mission.setName("Test Mission");
        mission.setRequesterID(8);
        mission.setDescription("This is a test mission.");
        mission.setStatus(Mission.STATUS.PENDING);

        boolean result = missionDataAccess.addMissionRequest(mission);

        assertTrue(result);
        assertTrue(mission.getMissionID() > 0);
    }

    @Test
    public void testUpdateMissionRequest() {
        Mission mission = new Mission();
        mission.setMissionID(6); // Assume this ID exists
        mission.setName("Updated Mission");
        mission.setRequesterID(8);
        mission.setDescription("Updated description.");
        mission.setStatus(Mission.STATUS.PENDING);
        mission.setVolunteerID(9);
        mission.setMotifRefus("");

        boolean result = missionDataAccess.updateMissionRequest(mission);

        assertTrue(result);
    }

    @Test
    public void testFindMissionRequestById() {
        int missionID = 2; // Assume this ID exists
        Mission mission = missionDataAccess.findMissionRequestById(missionID);

        assertNotNull(mission);
        assertEquals(missionID, mission.getMissionID());
    }

    @Test
    public void testFindMissionsRequestsByStatus() {
        String status = Mission.STATUS.PENDING.toString();
        ArrayList<Mission> missions = missionDataAccess.findMissionsRequestsByStatus(status);

        assertNotNull(missions);
        assertFalse(missions.isEmpty());
        assertEquals(status, missions.get(0).getStatus());
    }
}