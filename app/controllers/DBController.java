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
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.codec.ExtraTypeCodecs;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;

import java.util.*;
import java.text.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class DBController extends Controller {


    private static final Logger logger = LoggerFactory.getLogger("controller");

    public Result createPatient(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createPatient(), input is: {}", json.toString());
        Patient patient = Json.fromJson(json, Patient.class);
        JsonNode jsonObject = Json.toJson(patient);
        CqlSession session = CqlSession.builder()
                .build();
        DateFormat dformat = new SimpleDateFormat("YYYY-MM-dd");
        String id = json.get("user_id").asText();
        String fn = json.get("first_name").asText();
        String ln = json.get("last_name").asText();
        id = id.replaceAll("\"", "");
        fn = fn.replaceAll("\"", "");
        ln = ln.replaceAll("\"", "");

        Date date = patient.getDob();
        String d = dformat.format(date);
        StringBuilder sb = new StringBuilder("INSERT INTO upper_limb.user_info (user_id, dob, first_name, last_name) VALUES ('")
                .append(id).append("', '").append(date.getYear() + 1900).append("-").append(date.getMonth() + 1)
                .append("-").append(date.getDate()).append("', '").append(fn)
                .append("', '").append(ln).append("');");
        String query = sb.toString();
        session.execute(query);
        session.close();

        return ok(ApplicationUtil.createResponse(json, true));

    }

    public Result updatePatient(String id, Http.Request request) {
        logger.debug("In DBController.updatePatient()");
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting Json data", false));
        }
        Patient patient = Json.fromJson(json, Patient.class);
        ;
        logger.debug("In DBController.updatePatient(), Patient is: {}", id);
        if (patient == null) {
            return notFound(ApplicationUtil.createResponse("Patient not found", false));
        }
        JsonNode jsonObject = Json.toJson(patient);
        CqlSession session = CqlSession.builder()
                .build();
        DateFormat dformat = new SimpleDateFormat("YYYY-MM-dd");
        String fn = json.get("first_name").asText();
        String ln = json.get("last_name").asText();
        fn = fn.replaceAll("\"", "");
        ln = ln.replaceAll("\"", "");
        Date date = patient.getDob();
        String d = dformat.format(date);
        StringBuilder sb = new StringBuilder("UPDATE upper_limb.user_info SET dob = '")
                .append(date.getYear() + 1900).append("-").append(date.getMonth() + 1)
                .append("-").append(date.getDate()).append("', first_name = '").append(fn)
                .append("', last_name = '").append(ln).append("' WHERE user_id = '").append(id).append("';");
        String query = sb.toString();
        session.execute(query);
        session.close();
        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }


    public Result retrievePatient(String id) {

        logger.debug("In DBController.retrievePatient(), retrieve patient with id: {}", id);

        CqlSession session = CqlSession.builder()
                .build();
        Patient patient = new Patient();
        ResultSet rs = session.execute("SELECT * FROM upper_limb.user_info WHERE user_id ='" + id + "';");
        Row row = rs.one();
        if (row != null) {
            patient.setUserID(row.getString("user_id"));
            String d = row.getString("dob");
            DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            try {
                Date date = format.parse(d);
                patient.setDob(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            patient.setFirstName(row.getString("first_name"));
            patient.setLastName(row.getString("last_name"));

        }
        JsonNode jsonObject = Json.toJson(patient);
        session.close();
        return ok(ApplicationUtil.createResponse(jsonObject, true));

    }


    public Result listPatients() {

        Patient patient = new Patient();
        CqlSession session = CqlSession.builder()
                .build();

        ResultSet rs = session.execute("SELECT * FROM upper_limb.user_info;");
        Iterator<Row> itr = rs.iterator();
        List<JsonNode> ps = new ArrayList<>();
        Row row;
        while (itr.hasNext()) {
            row = itr.next();
            if (row != null) {
                patient.setUserID(row.getString("user_id"));
                String d = row.getString("dob");
                DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                try {
                    Date date = format.parse(d);
                    patient.setDob(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                patient.setFirstName(row.getString("first_name"));
                patient.setLastName(row.getString("last_name"));

                JsonNode jsonObject = Json.toJson(patient);
                ps.add(jsonObject);
                //}
            } else {
                System.out.println("It was null");
            }
        }
        System.out.println(ps);
        session.close();
        return ok(ApplicationUtil.createResponse(ps.get(1), true));

    }

    public Result deletePatient(String id) {
        logger.debug("In DBController.deletePatient(), delete patient with id: {}", id);
        CqlSession session = CqlSession.builder()
                .build();
        Patient patient = new Patient();
        ResultSet rs = session.execute("DELETE FROM upper_limb.user_info WHERE user_id ='" + id + "';");
        session.close();
        return ok(ApplicationUtil.createResponse("Patient with id:" + id + " deleted", true));
    }


    public Result createSession(String id, Http.Request request) throws Exception {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createSession(), input is: {}", json.toString());

        CqlSession session = CqlSession.builder()
                .build();
        Sess sess = Json.fromJson(json, Sess.class);
        JsonNode jsonObject = Json.toJson(sess);
        System.out.println("Json to json" + jsonObject);
        System.out.println("\nJson from json" + Json.fromJson(jsonObject, Sess.class));
        System.out.println("\nJson to json get session data" + jsonObject.get("session_data"));

        DateFormat dformat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = new Date();
        ObjectMapper om = new ObjectMapper();
        String j = om.writeValueAsString(json.get("session_data"));

        j = j.replaceAll("\"", "");
        System.out.println("\ndatos recogidos: " + j);

        session.execute("BEGIN BATCH INSERT INTO upper_limb.session_info (session_id, sessdata, timestamp) VALUES('" +
                jsonObject.get("session_id").textValue() + "', " + jsonObject.get("session_data") + ", '" + dformat.format(date) + "');" +
                "UPDATE upper_limb.user_info SET sessions = sessions + ['" + json.get("session_id").asText() + "'] WHERE user_id = '" + id + "'; APPLY BATCH;");

        session.close();

        return created(ApplicationUtil.createResponse(jsonObject, true));
    }


    public Result retrieveSession(String patient_id, String id, java.util.Optional<java.lang.Long> init, java.util.Optional<java.lang.Long> end) throws ParseException {
        logger.debug("In DBController.retrieveSession(), retrieve session with id: {}", id);
        Sess s = new Sess();

        //*******CÓDIGO PARA COGER LA MEDIA QUE PROBABLEMENTE HAYA QUE CAMBIAR********
        //if(init.isPresent()&&end.isPresent()){
        // long initial_time=init.get();
        //long final_time=end.get();

        // System.out.println("Initial_time: "+initial_time);
        // System.out.println("End_time: "+final_time);

        //Nos piden la media de los valores cuyo timestamp esté entre init y end
        //Sess sess = DataStore.getSession(patient_id, id);
            /*Sess sess = new Sess();

            List<RawData> interval = new ArrayList();

            for(RawData rd : sess.getData()){
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
            }*/
        //jsonObjects = Json.toJson(new Sess(sess.getSessionID(), sess.getTimestamp(), value));
        // }
        //else {
        // Nos piden toda la sesión
            /*CqlSession session = CqlSession.builder()
                    //.addContactPoint(127.0.0.1)
                    .build();
            ResultSet rs = session.execute("SELECT * FROM upper_limb.session_info WHERE session_id ='"+ id + "';");
            Row row = rs.one();
            if (row != null) {
                sess.setSessionId(row.getString("session_id"));
                String d = row.getString("timestamp");
                DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                try {
                    Date date = format.parse(d);
                    sess.setTimestamp(date);       }catch (Exception e){
                    e.printStackTrace();
                }

                sess.setFirstName(row.getString("first_name"));
                patient.setLastName(row.getString("last_name"));

            }
            JsonNode jsonObject = Json.toJson(patient);
            session.close();
            jsonObjects = Json.toJson(DataStore.getSession(patient_id, id));
        }*/
        CqlSession session = CqlSession.builder()
                .build();
        CodecRegistry codecRegistry = session.getContext().getCodecRegistry();
        // The target user-defined type
        UserDefinedType dataUdt =
                session
                        .getMetadata()
                        .getKeyspace("upper_limb")
                        .flatMap(ks -> ks.getUserDefinedType("raw_data"))
                        .orElseThrow(IllegalStateException::new);
        // The "inner" codec that handles the conversions from CQL from/to UdtValue
        TypeCodec<UdtValue> innerCodec = codecRegistry.codecFor(dataUdt);
        // The mapping codec that will handle the conversions from/to UdtValue and Coordinates
        DataCodec dataCodec = new DataCodec(innerCodec);
        // Register the new codec
        ((MutableCodecRegistry) codecRegistry).register(dataCodec);

        ResultSet rs = session.execute("SELECT * FROM upper_limb.session_info WHERE session_id ='" + id + "' ;");
        Row row = rs.one();
        System.out.println("hola");
        if (row != null) {
            s.setSessionID(row.getString("session_id"));
            String d = row.getString("timestamp");
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(d);
            s.setTimestamp(date1);
            List<RawData> rawDataList = row.getList("sessdata", RawData.class);
            ArrayList<RawData> rawDataArrayList = new ArrayList<>();
            rawDataArrayList.addAll(rawDataList);
            s.setData(rawDataArrayList);
        } else {
            System.out.println("It was null");
        }
        JsonNode jsonObject = Json.toJson(s);
        session.close();
        logger.debug("In DBController.retrieveSession(), result is: {}", jsonObject.toString());

        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }


    public Result listSessions(String patient_id) throws ParseException {
        Sess s = new Sess();
        CqlSession session = CqlSession.builder()
                .build();
        CodecRegistry codecRegistry = session.getContext().getCodecRegistry();
        // The target user-defined type
        UserDefinedType dataUdt =
                session
                        .getMetadata()
                        .getKeyspace("upper_limb")
                        .flatMap(ks -> ks.getUserDefinedType("raw_data"))
                        .orElseThrow(IllegalStateException::new);
        // The "inner" codec that handles the conversions from CQL from/to UdtValue
        TypeCodec<UdtValue> innerCodec = codecRegistry.codecFor(dataUdt);
        // The mapping codec that will handle the conversions from/to UdtValue and Coordinates
        DataCodec dataCodec = new DataCodec(innerCodec);
        // Register the new codec
        ((MutableCodecRegistry) codecRegistry).register(dataCodec);
        List<JsonNode> sessions = new ArrayList<>();
        ResultSet rs = session.execute("SELECT * FROM upper_limb.session_info;");
        Iterator<Row> itr = rs.iterator();
        Row row;
        while (itr.hasNext()) {
            row = itr.next();
            System.out.println("hola");
            if (row != null) {
                s.setSessionID(row.getString("session_id"));
                String d = row.getString("timestamp");
                Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(d);
                s.setTimestamp(date1);
                List<RawData> rawDataList = row.getList("sessdata", RawData.class);
                ArrayList<RawData> rawDataArrayList = new ArrayList<>();
                rawDataArrayList.addAll(rawDataList);
                s.setData(rawDataArrayList);
                JsonNode jsonObject = Json.toJson(s);
                sessions.add(jsonObject);
            } else {
                System.out.println("It was null");
            }
        }

        session.close();
        System.out.println(sessions);
        return ok(ApplicationUtil.createResponse(sessions.get(1), true));

    }


    public Result deleteSession(String patient_id, String id) {
        logger.debug("In DBController.retrieveSession(), delete session with id: {}", id);
        CqlSession session = CqlSession.builder()
                .build();
        Sess s = new Sess();
        ResultSet rs = session.execute("BEGIN BATCH DELETE FROM upper_limb.session_info WHERE session_id ='" + id + "';" +
                "UPDATE upper_limb.user_info SET sessions = sessions - ['"+id+"'] WHERE user_id = '"+patient_id+"'; APPLY BATCH;");
        session.close();
        return ok(ApplicationUtil.createResponse("Session with id:" + id + " deleted", true));
    }
}
