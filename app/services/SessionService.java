package services;

import entities.Patient;
import entities.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionService {

    private static SessionService instance;
    private Map<String, Session> sessions = new HashMap<>();

    public static SessionService getInstance(){
        if (instance==null){
            instance = new SessionService();
        }
        return instance;
    }

    //no sé si es necesario poder crear una sesión???
    public Session addSession(Patient patient, Session session){
        String id = session.getSessionID();
        patient.getSessions().put(id,session);
        return session;
    }

    public Session getSession(String patient_id, String id){
        Patient patient=PatientService.getInstance().getPatient(patient_id);
        return patient.getSessions().get(id);
    }

    public Set<Session> getSessions(String patient_id){
        Patient patient=PatientService.getInstance().getPatient(patient_id);
        return new HashSet<>(patient.getSessions().values());
    }

    public boolean deleteSession(String patient_id, String id){
        Patient patient=PatientService.getInstance().getPatient(patient_id);
        return patient.getSessions().remove(id) !=null;
    }
}
