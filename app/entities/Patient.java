package entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Patient {
    private String userID;
    private String firstName;
    private String lastName;
    private Date dob;
    private Map<String, Session> sessions = new HashMap<>();

    public Map<String, Session> getSessions() {
        return sessions;
    }

    public void setSessions(Map<String, Session> sessions) {
        this.sessions = sessions;
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
        return "Patient{" +
                "userID='" + userID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                '}';
    }



}
