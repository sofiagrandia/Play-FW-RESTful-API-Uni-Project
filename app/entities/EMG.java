package entities;

import java.util.*;

public class EMG {

    private float value;

    public EMG(float value){
        this.value=value;
    }

    public EMG(){
        Random r = new Random(new Date().getTime());
        this.value=r.nextFloat();
    }

    public float getValue(){
        return this.value;
    }

    public void setValue(float value){
        this.value=value;
    }

    public EMG average(EMG emg2){
        return new EMG((this.value+emg2.value)/2);
    }

    @Override
    public String toString(){

        return "( value: "+this.value+" )";
    }
}