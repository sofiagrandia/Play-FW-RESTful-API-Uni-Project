package entities;

import java.util.*;
import java.lang.*;

public class Sess {

    private static long time=new Date().getTime();

    private static int numSessions=0;

    private String session_id;
    private Date timestamp;
    private List<RawData>session_data = new ArrayList<>();

    public Sess(){
        this.session_id=""+numSessions++;
        time+=60*1000;
        this.timestamp=new Date(time);
        for(int i=0; i<100;i++){
            this.session_data.add(new RawData(time+i));
        }
    }

    public Sess(String sessionID, Date date, List<RawData> data){
        this.session_id=sessionID;
        this.timestamp=date;
        this.session_data=data;
    }

    public String getSessionID() {
        return session_id;
    }

    public void setSessionID(String sessionID) {
        this.session_id = sessionID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<RawData> getData(){
        return this.session_data;
    }

    public void setData(ArrayList<RawData> data){
        this.session_data=data;
    }


    @java.lang.Override
    public java.lang.String toString() {
        String raw = "\n";
        for(RawData rd : session_data){
            raw+="    "+rd.toString()+"\n";
        }

        return "Session{" +
                "sessionID='" + session_id + '\'' +
                ", timestamp=" + timestamp + raw +
                '}';
    }
}
