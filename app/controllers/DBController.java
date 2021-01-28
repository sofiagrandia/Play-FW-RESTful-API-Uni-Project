package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import data.*;
import utils.ApplicationUtil;

import java.util.*;

public class DBController extends Controller {
    private static final Logger logger = LoggerFactory.getLogger("controller");

    public Result createPatient(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createPatient(), input is: {}", json.toString());
        Patient patient = DataStore.addPatient(Json.fromJson(json, Patient.class));
        JsonNode jsonObject = Json.toJson(patient);
        return created(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result updatePatient(String id, Http.Request request) {
        logger.debug("In DBController.updatePatient()");
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting Json data", false));
        }
        Patient patient = DataStore.updatePatient(id,Json.fromJson(json, Patient.class));
        logger.debug("In DBController.updatePatient(), Patient is: {}",id);
        if (patient == null) {
            return notFound(ApplicationUtil.createResponse("Patient not found", false));
        }

        JsonNode jsonObject = Json.toJson(patient);
        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result retrievePatient(String id) {
        logger.debug("In DBController.retrievePatient(), retrieve patient with id: {}",id);
        if (DataStore.getPatient(id) == null) {
            return notFound(ApplicationUtil.createResponse("Patient with id:" + id + " not found", false));
        }
        JsonNode jsonObjects = Json.toJson(DataStore.getPatient(id));
        logger.debug("In DBController.retrievePatient(), result is: {}",jsonObjects.toString());
        return ok(ApplicationUtil.createResponse(jsonObjects, true));
    }

    public Result listPatients() {
        List<Patient> result = DataStore.getPatients();
        logger.debug("In DBController.listPatients(), result is: {}",result.toString());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(ApplicationUtil.createResponse(jsonData, true));

    }

    public Result deletePatient(String id) {
        logger.debug("In DBController.retrievePatient(), delete patient with id: {}",id);
        if (!DataStore.deletePatient(id)) {
            return notFound(ApplicationUtil.createResponse("Patient with id:" + id + " not found", false));
        }
        return ok(ApplicationUtil.createResponse("Patient with id:" + id + " deleted", true));
    }
    public Result createSession(String id,Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createSession(), input is: {}", json.toString());
        Session session = DataStore.addSession(id,Json.fromJson(json, Session.class));
        JsonNode jsonObject = Json.toJson(session);
        return created(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result retrieveSession(String patient_id, String id, java.util.Optional<java.lang.Long> init, java.util.Optional<java.lang.Long> end) {
        logger.debug("In DBController.retrieveSession(), retrieve session with id: {}",id);
        if (DataStore.getSession(patient_id,id) == null) {
            return notFound(ApplicationUtil.createResponse("Session with id:" + id + " not found", false));
        }

        JsonNode jsonObjects = null;
        if(init.isPresent()&&end.isPresent()){
            long initial_time=init.get();
            long final_time=end.get();

            System.out.println("Initial_time: "+initial_time);
            System.out.println("End_time: "+final_time);

            //Nos piden la media de los valores cuyo timestamp esté entre init y end
            Session sesion = DataStore.getSession(patient_id, id);

            List<RawData> interval = new ArrayList();

            for(RawData rd : sesion.getData()){
                System.out.println("Rd: "+rd.getTimestamp());
                if(rd.getTimestamp()>=initial_time && rd.getTimestamp()<final_time){
                    System.out.println(rd);
                    interval.add(rd);
                }
            }

            RawData r1 = null;
            List<RawData> value = new ArrayList();
            if(!interval.isEmpty()){
                r1 = interval.get(0);
                for(int i=1;i<interval.size();i++){
                    r1.average(interval.get(i));
                }
                value.add(r1);
            }
            jsonObjects = Json.toJson(new Session(sesion.getSessionID(), sesion.getTimestamp(), value));
        }
        else {
            // Nos piden toda la sesión
            jsonObjects = Json.toJson(DataStore.getSession(patient_id, id));
        }
        logger.debug("In DBController.retrieveSession(), result is: {}",jsonObjects.toString());
        return ok(ApplicationUtil.createResponse(jsonObjects, true));
    }

    public Result listSessions(String patient_id) {
        java.util.Map<String,Session> result = DataStore.getSessions(patient_id);
        logger.debug("In DBController.listSessions(), result is: {}",result.toString());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(ApplicationUtil.createResponse(jsonData, true));

    }

    public Result deleteSession(String patient_id,String id) {
        logger.debug("In DBController.retrieveSession(), delete session with id: {}",id);
        if (!DataStore.deleteSession(patient_id, id)) {
            return notFound(ApplicationUtil.createResponse("Session with id:" + id + " not found", false));
        }
        return ok(ApplicationUtil.createResponse("Session with id:" + id + " deleted", true));
    }
}
