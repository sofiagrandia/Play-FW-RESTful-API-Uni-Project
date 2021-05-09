package data;

import entities.*;
import java.util.*;
import java.time.LocalDate;

public class DataStore{

    private static final java.util.Map<String, Sess> sessions = new java.util.HashMap<>();
    private static final java.util.Map<String , java.util.Map<String, Sess>> session_patient = new java.util.HashMap<>();
    private static final Map<String, Patient> patients = initializePatients();



    private static Map<String, Patient> initializePatients(){
        Map<String, Patient> output = new HashMap<>();

        Patient patient1 = new Patient("id1", "john", "doe", new Date(1985,4,20));
        Sess session11 = new Sess();
        _addSession(patient1.getUserID(),session11);
        patient1.addSession(session11.getSessionID());

        Sess session12 = new Sess();
        _addSession(patient1.getUserID(),session12);
        patient1.addSession(session12.getSessionID());
        output.put(patient1.getUserID(),patient1);

        Patient patient2 = new Patient("id2", "claire", "smith", new Date(1992,7,10));
        Sess session21 = new Sess();
        _addSession(patient2.getUserID(),session21);
        patient2.addSession(session21.getSessionID());
        output.put(patient2.getUserID(),patient2);


        Patient patient3 = new Patient("id3", "rose", "mcdonald", new Date(1976,8,2));
        Sess session31 = new Sess();
        _addSession(patient3.getUserID(),session31);
        patient3.addSession(session31.getSessionID());
        output.put(patient3.getUserID(),patient3);

        return output;
    }

    public static Patient addPatient(Patient patient){
        String id = patient.getUserID();
        patients.put(id,patient);
        return patient;
    }

    public static Patient getPatient(String id){
        return patients.get(id);
    }

    public static List<Patient> getPatients(){
        return new ArrayList<Patient>(patients.values());
    }

    public static Patient updatePatient(String id, Patient patient){
        if(patients.containsKey(id)){
            patients.put(id,patient);
            return patient;
        }
        return null;
    }

    public static boolean deletePatient(String id){
        return patients.remove(id)!=null;
    }

    public static Sess addSession(String patient_id, Sess sess){
        String id = sess.getSessionID();
        sessions.put(id,sess);
        session_patient.put(patient_id,sessions);
        getPatient(patient_id).addSession(id);
        return sess;
    }

    private static Sess _addSession(String patient_id, Sess sess){
        String id = sess.getSessionID();
        sessions.put(id,sess);
        session_patient.put(patient_id,sessions);
        return sess;
    }

    public static Sess getSession(String patient_id, String id){
        Patient patient=getPatient(patient_id);

        return session_patient.get(patient_id).get(id);
    }

    public static Map<String, Sess> getSessions(String patient_id){
        return session_patient.get(patient_id);
    }

    public static boolean deleteSession(String patient_id, String id){
        getPatient(patient_id).removeSession(id);
        return session_patient.get(patient_id).remove(id) !=null;
    }

}