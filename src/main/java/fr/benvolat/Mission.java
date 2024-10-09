package fr.benvolat;

public class Mission {

    public enum STATUS{
        PENDING, VALIDATE, REALISED, REFUSED
    }

    private int missionID;
    private String name;
    private String description;
    private String status;
    private String motifRefus;
    private User requester;
    private Benevole volunteer;

    public Mission(int missionID, String name, String description, User requester) {
        this.missionID = missionID;
        this.name = name;
        this.description = description;
        this.status = STATUS.PENDING.toString();
        this.requester = requester;
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

    public void setVolunteer(Benevole volunteer) {
        this.volunteer = volunteer;
    }

    public void setStatus(STATUS status) {
        this.status = status.toString();
    }

    public void setMotifRefus(String motifRefus) {
        if(this.status.equals(STATUS.REFUSED.toString())){
            this.motifRefus = motifRefus;
        } else{
            System.out.println("La mission doit etre refusee pour qu'un motif soit place");
        }
    }
}
