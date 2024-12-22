package fr.benvolat.service;

import fr.benvolat.dao.MissionDataAccess;
import fr.benvolat.models.Mission;

import java.util.ArrayList;

public class MissionService {

    private final MissionDataAccess missionDAO;

    public MissionService() {
        this.missionDAO = new MissionDataAccess();
    }

    // Method to submit a new mission request
    public boolean submitMissionRequest(String name, int requesterId, String description) {
        boolean bool;
        Mission request = new Mission(name, requesterId, description);
        bool = missionDAO.addMissionRequest(request);
        return bool;
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

    public boolean moderateMission(int missionId, Mission.STATUS status, String motifRefus) {
        boolean res = false;
        Mission request = missionDAO.findMissionRequestById(missionId);

        if (request != null) {
            request.setStatus(status);
            if(status.equals(Mission.STATUS.REFUSED)) request.setMotifRefus(motifRefus);
            res = missionDAO.updateMissionRequest(request);
        }

        return res;
    }

    public String[][] getMissionsDataByStatus(String status) {
        ArrayList<Mission> missionArrayList = missionDAO.findMissionsRequestsByStatus(status);
        return getStringsArrays(missionArrayList);
    }

    public String[][] getMissionsDataByVolunteer(int volunteerId) {
        ArrayList<Mission> missionArrayList = missionDAO.getMissionsByVolunteerId(volunteerId);
        return getStringsArrays(missionArrayList);
    }

    public String[][] getMissionsDataByUserId(int userId) {
        ArrayList<Mission> missionArrayList = missionDAO.getMissionsByUserId(userId);
        return getStringsArrays(missionArrayList);
    }


    private String[][] getStringsArrays(ArrayList<Mission> missionArrayList) {
        ArrayList<ArrayList<String>> missionData = new ArrayList<>();
        for (Mission mission : missionArrayList) {
            ArrayList<String> missionDataRow = new ArrayList<>();
            missionDataRow.add(Integer.toString(mission.getMissionID()));
            missionDataRow.add(mission.getName());
            missionDataRow.add(mission.getDescription());
            missionDataRow.add(mission.getStatus());
            missionData.add(missionDataRow);
        }
        return MissionService.convertTo2DArray(missionData);
    }

    public static String[][] convertTo2DArray(ArrayList<ArrayList<String>> arrayList) {
        // Determine the maximum number of columns by checking the largest inner list size
        int maxColumns = 0;
        for (ArrayList<String> innerList : arrayList) {
            maxColumns = Math.max(maxColumns, innerList.size());
        }

        // Create a new String[][] based on the size of the outer and inner lists
        String[][] result = new String[arrayList.size()][maxColumns];

        // Fill the result array
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<String> innerList = arrayList.get(i);
            for (int j = 0; j < innerList.size(); j++) {
                result[i][j] = innerList.get(j);
            }
        }

        return result;
    }


}
