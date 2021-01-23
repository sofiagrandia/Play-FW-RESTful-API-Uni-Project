package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Employee;
import entities.Patient;
import entities.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.EmployeeService;
import services.PatientService;
import services.SessionService;
import utils.ApplicationUtil;

import java.util.Set;

public class DBController extends Controller {
    private static final Logger logger = LoggerFactory.getLogger("controller");

    public Result createPatient(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createPatient(), input is: {}", json.toString());
        Patient patient = PatientService.getInstance().addPatient(Json.fromJson(json, Patient.class));
        JsonNode jsonObject = Json.toJson(patient);
        return created(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result updatePatient(String id, Http.Request request) {
        logger.debug("In DBController.updatePatient()");
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting Json data", false));
        }
        Patient patient = PatientService.getInstance().updatePatient(id,Json.fromJson(json, Patient.class));
        logger.debug("In DBController.updatePatient(), Patient is: {}",id);
        if (patient == null) {
            return notFound(ApplicationUtil.createResponse("Patient not found", false));
        }

        JsonNode jsonObject = Json.toJson(patient);
        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result retrievePatient(String id) {
        logger.debug("In DBController.retrievePatient(), retrieve patient with id: {}",id);
        if (PatientService.getInstance().getPatient(id) == null) {
            return notFound(ApplicationUtil.createResponse("Patient with id:" + id + " not found", false));
        }
        JsonNode jsonObjects = Json.toJson(PatientService.getInstance().getPatient(id));
        logger.debug("In DBController.retrievePatient(), result is: {}",jsonObjects.toString());
        return ok(ApplicationUtil.createResponse(jsonObjects, true));
    }

    public Result listPatients() {
        Set<Patient> result = PatientService.getInstance().getPatients();
        logger.debug("In DBController.listPatients(), result is: {}",result.toString());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(ApplicationUtil.createResponse(jsonData, true));

    }

    public Result deletePatient(String id) {
        logger.debug("In DBController.retrievePatient(), delete patient with id: {}",id);
        if (!PatientService.getInstance().deletePatient(id)) {
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
        Session session = SessionService.getInstance().addSession(id,Json.fromJson(json, Session.class));
        JsonNode jsonObject = Json.toJson(session);
        return created(ApplicationUtil.createResponse(jsonObject, true));
    }

    public Result retrieveSession(String patient_id, String id) {
        logger.debug("In DBController.retrieveSession(), retrieve session with id: {}",id);
        if (SessionService.getInstance().getSession(patient_id,id) == null) {
            return notFound(ApplicationUtil.createResponse("Session with id:" + id + " not found", false));
        }
        JsonNode jsonObjects = Json.toJson(SessionService.getInstance().getSession(patient_id,id));
        logger.debug("In DBController.retrieveSession(), result is: {}",jsonObjects.toString());
        return ok(ApplicationUtil.createResponse(jsonObjects, true));
    }

    public Result listSessions(String patient_id) {
        Set<Session> result = SessionService.getInstance().getSessions(patient_id);
        logger.debug("In DBController.listSessions(), result is: {}",result.toString());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(ApplicationUtil.createResponse(jsonData, true));

    }

    public Result deleteSession(String patient_id,String id) {
        logger.debug("In DBController.retrieveSession(), delete session with id: {}",id);
        if (!SessionService.getInstance().deleteSession(patient_id, id)) {
            return notFound(ApplicationUtil.createResponse("Session with id:" + id + " not found", false));
        }
        return ok(ApplicationUtil.createResponse("Session with id:" + id + " deleted", true));
    }
}
