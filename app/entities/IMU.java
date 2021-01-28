package entities;

import java.util.*;

public class IMU{

    private float[] data;

    public IMU(float data1, float data2, float data3, float data4, float data5, float data6, float data7, float data8, float data9){
        data = new float[9];
        data[0]=data1;
        data[1]=data2;
        data[2]=data3;
        data[3]=data4;
        data[4]=data5;
        data[5]=data6;
        data[6]=data7;
        data[7]=data8;
        data[8]=data9;
    }

    public IMU(float[] data){
        this.data = data;
    }
    public IMU(){
        this.data = new float[9];
        Random r = new Random(new Date().getTime());

        for(int i=0;i<9;i++){
            this.data[i]=r.nextFloat();
        }
    }

    public float[] getData(){
        return this.data;
    }

    public void setData(float[] data){
        this.data=data;
    }

    public IMU average(IMU imu2){
        float[] d = new float[9];
        for(int i=0; i<9; i++){
            d[i]=(this.data[i]+imu2.data[i])/2;
        }
        return new IMU(d);
    }

    @Override
    public String toString(){
        String output="(";
        for(float d : this.data){
            output+= d+", ";
        }
        output+=")";
        return output;
    }
}