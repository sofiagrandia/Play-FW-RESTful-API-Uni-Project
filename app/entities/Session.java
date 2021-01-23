package entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Session {
    private String sessionID;
    private Timestamp timestamp;
    private float EMG1;
    private float EMG2;
    private List<Float>IMU1 = new ArrayList<>();
    private List<Float> IMU2 = new ArrayList<>();
    private List<Float> IMU3 = new ArrayList<>();


    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getEMG1() {
        return EMG1;
    }

    public void setEMG1(float EMG1) {
        this.EMG1 = EMG1;
    }

    public float getEMG2() {
        return EMG2;
    }

    public void setEMG2(float EMG2) {
        this.EMG2 = EMG2;
    }

    public List<Float> getIMU1() {
        return IMU1;
    }

    public void setIMU1(List<Float> IMU1) {
        this.IMU1 = IMU1;
    }

    public List<Float> getIMU2() {
        return IMU2;
    }

    public void setIMU2(List<Float> IMU2) {
        this.IMU2 = IMU2;
    }

    public List<Float> getIMU3() {
        return IMU3;
    }

    public void setIMU3(List<Float> IMU3) {
        this.IMU3 = IMU3;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Session{" +
                "sessionID='" + sessionID + '\'' +
                ", timestamp=" + timestamp +
                ", EMG1=" + EMG1 +
                ", EMG2=" + EMG2 +
                ", IMU1=" + IMU1 +
                ", IMU2=" + IMU2 +
                ", IMU3=" + IMU3 +
                '}';
    }
}
