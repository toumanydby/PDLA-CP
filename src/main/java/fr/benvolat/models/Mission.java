package fr.benvolat.models;

public class Mission {
    public enum STATUS {
        PENDING,
        VALIDATE,
        REFUSED,
        REALISED
    }

    private int missionID;
    private String name;
    private String description;
    private String status;
    private String motifRefus;
    private int requesterID;
    private int volunteerID;

    public Mission(){
    }
    // Constructor for creating a new mission
    public Mission(String name, int requesterID,String description) {
        this.name = name;
        this.description = description;
        this.requesterID = requesterID;
        this.status = STATUS.PENDING.toString();
    }

    // Constructor for existing mission
    public Mission(int missionID, String name,int requesterID, String description, String status, 
            int volunteerID, String motifRefus) {
        this.missionID = missionID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.motifRefus = motifRefus;
        this.requesterID = requesterID;
        this.volunteerID = volunteerID;
    }

    // Getters
    public int getMissionID() { return missionID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getMotifRefus() { return motifRefus; }
    public int getRequesterID() { return requesterID; }
    public int getVolunteerID() { return volunteerID; }

    // Setters
    public void setMissionID(int missionID) { this.missionID = missionID; }
    public void setName(String name){ this.name = name;}
    public void setStatus(STATUS status) { this.status = status.toString(); }
    public void setMotifRefus(String motifRefus) { this.motifRefus = motifRefus; }
    public void setDescription(String description) { this.description = description; }
    public void setVolunteerID(int volunteerID) { this.volunteerID = volunteerID; }
    public void setRequesterID(int requesterID) { this.requesterID = requesterID; }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + missionID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", requesterID=" + requesterID +
                ", volunteerID=" + volunteerID +
                '}';
    }
}
