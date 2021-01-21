package entities;

import java.sql.Timestamp;

public class Session {
    private String sessionID;
    private Timestamp timestamp;
    private float EMG1;
    private float EMG2;
    private float[] IMU1;
    private float[] IMU2;
    private float[] IMU3;

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

    public float[] getIMU1() {
        return IMU1;
    }

    public void setIMU1(float[] IMU1) {
        this.IMU1 = IMU1;
    }

    public float[] getIMU2() {
        return IMU2;
    }

    public void setIMU2(float[] IMU2) {
        this.IMU2 = IMU2;
    }

    public float[] getIMU3() {
        return IMU3;
    }

    public void setIMU3(float[] IMU3) {
        this.IMU3 = IMU3;
    }
}
