package entities;

import java.util.*;

public class Patient {
    private String userID;
    private String firstName;
    private String lastName;
    private Date dob;
    private List<String> sessions;


    public Patient(String userID, String firstName, String lastName, Date dob){
        this();
        this.userID=userID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.dob=dob;
    }

    public Patient(){
        this.userID="";
        this.firstName="";
        this.lastName="";
        this.dob=new Date();
        this.sessions= new ArrayList<String>();
    }

    public List<String> getSessions(){
        return this.sessions;
    }

    public void setSessions(List<String> sessions){
        this.sessions=sessions;
    }

    public void addSession(String sessionId){
        this.sessions.add(sessionId);
    }

    public boolean removeSession(String sessionId){
        return sessions.remove(sessionId);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        String output="(";
        for(String s : sessions){
            output+=s+",";
        }
        output+=")";
        return "Patient{" +
                "userID='" + userID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +", sessions= "+output+
                '}';
    }



}
