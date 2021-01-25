package services;

import entities.Patient;
import entities.Session;

import java.util.Map;

public class SessionService {

    private static SessionService instance;
    private java.util.Map<String, Session> sessions = new java.util.HashMap<>();
    private  java.util.Map<String , java.util.Map<String, Session>> session_patient = new java.util.HashMap<>();

    public static SessionService getInstance(){
        if (instance==null){
            instance = new SessionService();
        }
        return instance;
    }

    //no sé si es necesario poder crear una sesión???
    public Session addSession(String patient_id, Session session){
        String id = session.getSessionID();
        sessions.put(id,session);
        session_patient.put(patient_id,sessions);
        return session;
    }

    public Session getSession(String patient_id, String id){
        Patient patient=PatientService.getInstance().getPatient(patient_id);

        return session_patient.get(patient_id).get(id);
    }

    public Map<String, Session> getSessions(String patient_id){


        return session_patient.get(patient_id);
    }

    public boolean deleteSession(String patient_id, String id){

        return session_patient.get(patient_id).remove(id) !=null;
    }
}
