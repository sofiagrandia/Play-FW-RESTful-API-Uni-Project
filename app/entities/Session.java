package entities;

import java.util.*;
import java.lang.*;

public class Session {

    private static long time=new Date().getTime();

    private static int numSessions=0;

    private String sessionID;
    private Date timestamp;
    private List<RawData>data = new ArrayList<>();

    public Session(){
        this.sessionID=""+numSessions++;
        time+=60*1000;
        this.timestamp=new Date(time);
        for(int i=0; i<100;i++){
            this.data.add(new RawData(time+i));
        }
    }

    public Session(String sessionID, Date date, List<RawData> data){
        this.sessionID=sessionID;
        this.timestamp=date;
        this.data=data;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<RawData> getData(){
        return this.data;
    }

    public void setData(ArrayList<RawData> data){
        this.data=data;
    }


    @java.lang.Override
    public java.lang.String toString() {
        String raw = "\n";
        for(RawData rd : data){
            raw+="    "+rd.toString()+"\n";
        }

        return "Session{" +
                "sessionID='" + sessionID + '\'' +
                ", timestamp=" + timestamp + raw +
                '}';
    }
}
