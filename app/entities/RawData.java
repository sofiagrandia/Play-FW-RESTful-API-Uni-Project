package entities;

import java.util.*;

public class RawData{


    public double time;
    public float emg1;
    public float emg2;
    public List<Float> imu1;
    public List<Float>imu2;
    public List<Float> imu3;

    public RawData(double time, float emg1, float emg2, List<Float> imu1, List<Float> imu2, List<Float> imu3) {
        this.time = time;
        this.emg1 = emg1;
        this.emg2 = emg2;
        this.imu1 = imu1;
        this.imu2 = imu2;
        this.imu3 = imu3;
    }

    public RawData(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getEmg1() {
        return emg1;
    }

    public void setEmg1(float emg1) {
        this.emg1 = emg1;
    }

    public float getEmg2() {
        return emg2;
    }

    public void setEmg2(float emg2) {
        this.emg2 = emg2;
    }

    public List<Float> getImu1() {
        return imu1;
    }

    public void setImu1(List<Float>imu1) {
        this.imu1 = imu1;
    }

    public List<Float> getImu2() {
        return imu2;
    }

    public void setImu2(List<Float> imu2) {
        this.imu2 = imu2;
    }

    public List<Float> getImu3() {
        return imu3;
    }

    public void setImu3(List<Float> imu3) {
        this.imu3 = imu3;
    }

    @java.lang.Override
    public java.lang.String toString() {
        String outputi1="(";
        for(Float i1 : imu1){
            outputi1+=i1+",";
        }
        outputi1+=")";
        String outputi2="(";
        for(Float i2 : imu2){
            outputi2+=i2+",";
        }
        outputi2+=")";
        String outputi3="(";
        for(Float i3 : imu3){
            outputi3+=i3+",";
        }
        outputi3+=")";
        return "RawData{" +
                "\ntime=" + time +
                "\nemg1=" + emg1 +
                "\nemg2=" + emg2 +
                "\nimu1=" + outputi1 +
                "\nimu2=" + outputi2 +
                "\nimu3=" + outputi3 +
                '}';
    }

    /*private IMU imu1;
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
    }*/
}