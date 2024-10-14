package fr.benvolat.models;

public class User {

    public enum ROLE{
        ADMIN, MODERATOR, USER, BENEVOLE
    }

    /**
     * ID unique de l'user
     */
    private int userID;
    private String name;
    private String email;
    private String password;
    private final String role;

    public User(int userID, String name, String email,String password,String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int userID, String name, String email,String password){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = ROLE.USER.toString();
    }

    public User(String name, String email,String password, String role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = ROLE.USER.toString();
    }

    public int getUserID(){
        return this.userID;
    }

    public void setUserID(int userID){
         this.userID = userID;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getUserRole(){
        return this.role;
    }

    public String getPassword(){
        return this.password;
    }

}
