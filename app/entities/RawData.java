package entities;

import java.util.*;

public class RawData{


    private long timestamp;

    private IMU imu1;
    private IMU imu2;
    private IMU imu3;

    private EMG emg1;
    private EMG emg2;

    public RawData(Date timestamp, IMU imu1, IMU imu2, IMU imu3, EMG emg1, EMG emg2){
        this.timestamp=timestamp.getTime();
        this.imu1=imu1;
        this.imu2=imu2;
        this.imu3=imu3;
        this.emg1=emg1;
        this.emg2=emg2;
    }

    public RawData(long t){
        this.timestamp=t;
        this.imu1=new IMU();
        this.imu2=new IMU();
        this.imu3=new IMU();
        this.emg1=new EMG();
        this.emg2=new EMG();
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public IMU getImu1(){
        return this.imu1;
    }

    public IMU getImu2(){
        return this.imu2;
    }
    public IMU getImu3(){
        return this.imu3;
    }

    public EMG getEmg1(){
        return this.emg1;
    }

    public EMG getEmg2(){
        return this.emg2;
    }

    public RawData average(RawData rd){
        return new RawData(new Date(this.timestamp),
                imu1.average(rd.getImu1()),
                imu2.average(rd.getImu2()),
                imu3.average(rd.getImu3()),
                emg1.average(rd.getEmg1()),
                emg2.average(rd.getEmg2()));
    }

    @Override
    public String toString(){
        return "timestamp: "+this.timestamp+ ": EMG1 ["+this.emg1+"], "+
                "EMG2 ["+this.emg2+"]\n"+
                "IMU1 ["+this.imu1+"]\n"+
                "IMU2 ["+this.imu2+"]\n"+
                "IMU3 ["+this.imu3+"]";
    }
}