package services;

import entities.Patient;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PatientService {
    private static PatientService instance;
    private Map<String, Patient> patients = new HashMap<>();
    public static PatientService getInstance(){
        if (instance == null){
            instance = new PatientService();
        }
        return instance;
    }

    public Patient addPatient(Patient patient){
        String id = patient.getUserID();
        patients.put(id,patient);
        return patient;
    }

    public Patient getPatient(String id){
        return patients.get(id);
    }

    public Set<Patient> getPatients(){
        return new HashSet<>(patients.values());
    }

    public Patient updatePatient(String id){
        Patient patient = patient.getPatient(id);
        if(patients.containsKey(id)){
            patients.put(id,patient);
            return patient;
        }
        return null;
    }

    public boolean deletePatient(String id){
        return patients.remove(id)!=null;
    }

}
