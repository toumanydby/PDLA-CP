package fr.benvolat.models;

public class Mission {

    public enum STATUS {
        PENDING, VALIDATE, REALISED, REFUSED
    }

    private int missionID;
    private String name;
    private String description;
    private String status;
    private String motifRefus;
    private int requesterID;
    private int volunteerID;

    public Mission(int id, String name, int requesterID, String description, String status, int volunteerID, String motifRefus) {
        this.missionID = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.requesterID = requesterID;
        this.volunteerID = volunteerID;
        this.motifRefus = motifRefus;
    }


    public Mission(String name, int requesterID, String description) {
        this.name = name;
        this.description = description;
        this.status = STATUS.PENDING.toString();
        this.requesterID = requesterID;
    }


    public int getMissionID() {
        return missionID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public int getRequesterID() {
        return requesterID;
    }

    public void setMissionID(int missionID) {
        this.missionID = missionID;
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        this.volunteerID = volunteerID;
    }

    public void setStatus(STATUS status) {
        this.status = status.toString();
    }

    public void setMotifRefus(String motifRefus) {
        if (this.status.equals(STATUS.REFUSED.toString())) {
            this.motifRefus = motifRefus;
        } else {
            System.out.println("La mission doit etre refusee pour qu'un motif soit place");
        }
    }
}
