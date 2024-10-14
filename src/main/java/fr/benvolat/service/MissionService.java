package fr.benvolat.service;

import fr.benvolat.dao.MissionDataAccess;
import fr.benvolat.models.Mission;
import fr.benvolat.models.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class MissionService {

    private final MissionDataAccess missionDAO;

    public MissionService(MissionDataAccess missionDAO) throws SQLException {
        this.missionDAO = new MissionDataAccess();
    }

    // Method to submit a new mission request
    public void submitMissionRequest(String name, int requesterId, String description) {
        Mission request = new Mission(name, requesterId, description);
        missionDAO.addMissionRequest(request);
    }

    // Method to assign a volunteer to a mission request
    public boolean assignVolunteerToMission(int missionId, int volunteerId) {
        boolean bool = false;
        Mission request = missionDAO.findMissionRequestById(missionId);

        if (request != null && Mission.STATUS.VALIDATE.toString().equals(request.getStatus())) {
            request.setVolunteerID(volunteerId);
            request.setStatus(Mission.STATUS.REALISED);
            missionDAO.updateMissionRequest(request);
            bool = true;  // Assignment successful
        }

        return bool;
    }

    // Method to get all pending help requests
    public ArrayList<Mission> getPendingMissionsRequests() {
        return missionDAO.findMissionsRequestsByStatus("Pending");
    }

    public boolean moderateMission(int missionId, Mission.STATUS status){
        boolean res = false;
        Mission request = missionDAO.findMissionRequestById(missionId);

        if(request != null){
            request.setStatus(status);
            res = missionDAO.updateMissionRequest(request);
        }

        return res;
    }

}
