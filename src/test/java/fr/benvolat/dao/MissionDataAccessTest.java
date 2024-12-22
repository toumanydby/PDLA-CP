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
    public void testAddMissionRequestWithNull() {
        Mission mission = null;
        boolean result;
        try{
            result = missionDataAccess.addMissionRequest(mission);
            assertFalse(result, "Adding a null mission should return false.");
        }catch (NullPointerException e){
            assertThrowsExactly(NullPointerException.class, () -> {
                throw e;
            });
        }
    }

    @Test
    public void testAddMissionRequestWithIncompleteData() {
        Mission mission = new Mission();
        mission.setRequesterID(8); // Missing name and description

        boolean result = missionDataAccess.addMissionRequest(mission);

        assertFalse(result, "Adding a mission with incomplete data should return false.");
    }

    // We can actually insert duplicate mission, but it will be different missions by the id.
/*    @Test
    public void testAddMissionRequestDuplicateEntry() {
        Mission mission1 = new Mission();
        mission1.setName("Duplicate Mission");
        mission1.setRequesterID(8);
        mission1.setDescription("First entry for duplicate test.");
        mission1.setStatus(Mission.STATUS.PENDING);

        boolean firstResult = missionDataAccess.addMissionRequest(mission1);
        assertTrue(firstResult, "First insertion should succeed.");

        Mission mission2 = new Mission();
        mission2.setName("Duplicate Mission");
        mission2.setRequesterID(8);
        mission2.setDescription("Duplicate entry for duplicate test.");
        mission2.setStatus(Mission.STATUS.PENDING);

        boolean secondResult = missionDataAccess.addMissionRequest(mission2);

        assertFalse(secondResult, "Adding a duplicate mission should return false.");
    }*/

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