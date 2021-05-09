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
        /*
        System.out.println(json);
        System.out.println(patient);
        System.out.println(jsonObject);*/

        Date date = patient.getDob();
        String d = dformat.format(date);
        StringBuilder sb = new StringBuilder("INSERT INTO upper_limb.user_info (user_id, dob, first_name, last_name) VALUES ('")
                .append(id).append("', '").append(date.getYear()+1900).append("-").append(date.getMonth()+1)
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
        Patient patient = Json.fromJson(json, Patient.class);;
        logger.debug("In DBController.updatePatient(), Patient is: {}",id);
        if (patient == null) {
            return notFound(ApplicationUtil.createResponse("Patient not found", false));
        }
        JsonNode jsonObject = Json.toJson(patient);
        CqlSession session = CqlSession.builder()
                .build();
        DateFormat dformat = new SimpleDateFormat("YYYY-MM-dd");
        String fn = json.get("first_name").asText();
        String ln = json.get("last_name").asText();
        //id = id.replaceAll("\"", "");
        fn = fn.replaceAll("\"", "");
        ln = ln.replaceAll("\"", "");
        Date date = patient.getDob();
        String d = dformat.format(date);
        StringBuilder sb = new StringBuilder("UPDATE upper_limb.user_info SET dob = '")
                .append(date.getYear()+1900).append("-").append(date.getMonth()+1)
                .append("-").append(date.getDate()).append("', first_name = '").append(fn)
                .append("', last_name = '").append(ln).append("' WHERE user_id = '").append(id).append("';");
        String query = sb.toString();
        session.execute(query);
        session.close();



        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }



    public Result retrievePatient(String id) {

        /*logger.debug("In DBController.retrievePatient(), retrieve patient with id: {}",id);
        if (DataStore.getPatient(id) == null) {
            return notFound(ApplicationUtil.createResponse("Patient with id:" + id + " not found", false));
        }
        JsonNode jsonObjects = Json.toJson(DataStore.getPatient(id));
        logger.debug("In DBController.retrievePatient(), result is: {}",jsonObjects.toString());
        return ok(ApplicationUtil.createResponse(jsonObjects, true));*/

        /*Cluster cluster = null;
        cluster = Cluster.builder()
                    .addContactPoint("127.0.0.1")
                    .build();
            CqlSession session = cluster.connect();*/
        CqlSession session = CqlSession.builder()
                //.addContactPoint(127.0.0.1)
                .build();
        Patient patient = new Patient();
        ResultSet rs = session.execute("SELECT * FROM upper_limb.user_info WHERE user_id ='"+ id + "';");
        Row row = rs.one();
        if (row != null) {
            patient.setUserID(row.getString("user_id"));
            String d = row.getString("dob");
            DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
            try {
                Date date = format.parse(d);
                patient.setDob(date);       }catch (Exception e){
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
        /*List<Patient> result = DataStore.getPatients();
        logger.debug("In DBController.listPatients(), result is: {}",result.toString());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(ApplicationUtil.createResponse(jsonData, true));*/

        CqlSession session = CqlSession.builder()
                //.addContactPoint(127.0.0.1)
                .build();
        session.execute("SELECT * FROM upper_limb.user_info;");
        session.close();
        return ok(views.html.index.render());

    }

    public Result deletePatient(String id) {
        logger.debug("In DBController.deletePatient(), delete patient with id: {}",id);
        /*if (!DataStore.deletePatient(id)) {
            return notFound(ApplicationUtil.createResponse("Patient with id:" + id + " not found", false));
        }*/
        CqlSession session = CqlSession.builder()
                //.addContactPoint(127.0.0.1)
                .build();
        Patient patient = new Patient();
        ResultSet rs = session.execute("DELETE FROM upper_limb.user_info WHERE user_id ='"+ id + "';");
        session.close();
        return ok(ApplicationUtil.createResponse("Patient with id:" + id + " deleted", true));
    }



    public Result createSession(String id,Http.Request request) throws Exception{
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest(ApplicationUtil.createResponse("Expecting JSON data", false));
        }
        logger.debug("In DBController.createSession(), input is: {}", json.toString());
        //

        //JsonNode jsonObject = Json.toJson(sess);
        CqlSession session = CqlSession.builder()
                .build();
        //Sess sess = DataStore.addSession(id,Json.fromJson(json, Sess.class));
        Sess sess = Json.fromJson(json, Sess.class);
        JsonNode jsonObject = Json.toJson(sess);
        System.out.println("Json to json"+jsonObject);
        System.out.println("\nJson from json"+Json.fromJson(jsonObject, Sess.class));
        System.out.println("\nJson to json get session data"+jsonObject.get("session_data"));

        //System.out.println(Json.fromJson(json, Sess.class));
        DateFormat dformat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = new Date();
        ObjectMapper om = new ObjectMapper();
        String j = om.writeValueAsString(json.get("session_data"));

        j = j.replaceAll("\"", "");
        System.out.println("\ndatos recogidos: "+j);
        //String d = json.get("session_data").asText();
        //System.out.println("As text" + d);
        //d = d.replaceAll("\"", "");

        //System.out.println("get solo"+json.get("session_data"));
        //System.out.println(d);

        /*System.out.println(json);
        String ids = json.get("session_id").asText();
        String emg1 = json.get("emg1").asText();
        String emg2 = json.get("emg2").asText();
        JsonNode imu1 = json.get("imu1");
        JsonNode imu2 = json.get("imu2");
        JsonNode imu3 = json.get("imu3");
        String i1[] = new String[9];
        String i2[] = new String[9];
        String i3[] = new String[9];
        List<Float> im1 = new ArrayList<>();
        List<Float> im2 = new ArrayList<>();
        List<Float> im3 = new ArrayList<>();
        for (int i = 0; i<9 ; i++){
            i1[i]=imu1.get(i).asText();
            System.out.println(i1[i]);
            i2[i]=imu2.get(i).asText();
            i3[i]=imu3.get(i).asText();
        }
        for (int i = 0; i<9 ; i++){
            im1.add(Float.parseFloat(i1[i]));
            im2.add(Float.parseFloat(i2[i]));
            im3.add(Float.parseFloat(i3[i]));
        }
        System.out.println(ids);
        System.out.println(i1);
        System.out.println(imu1.get(2).asText());
        //System.out.println(im1[2]);

        ids = ids.replaceAll("\"", "");
        /*emg1 = emg1.replaceAll("\"", "");
        emg2 = emg2.replaceAll("\"", "");
        imu1 = imu1.replaceAll("\"", "");
        imu2 = imu2.replaceAll("\"", "");
        imu3 = imu3.replaceAll("\"", "");

        System.out.println(emg1);
        System.out.println(imu1);
        /*
        System.out.println(json);
        System.out.println(patient);
        System.out.println(jsonObject);


        float e1 = Float.parseFloat(emg1);
        float e2 = Float.parseFloat(emg2);
        /*
        float i1 = Float.parseFloat(imu1);
        float i2 = Float.parseFloat(imu2);
        float i3 = Float.parseFloat(imu3);

        System.out.println(e1);
        System.out.println(im1);
        System.out.println(im2);
        System.out.println(im3);*/
        /*StringBuilder sb = new StringBuilder("BEGIN BATCH INSERT INTO upper_limb.session_info (session_id, emg1, emg2, imu1, imu2, imu3, timestamp) VALUES (%s, %s, %s, %s, %s, %s, %s);")
                .append(ids).append("', '").append(e1).append("', '").append(e2).append("', '").append(imu1)
                .append("', '").append(imu2).append("', '").append(imu3).append("', '").append(dformat.format(date))
                .append("');")
                .append("UPDATE upper_limb.user_info SET sessions = sessions + ['").append(ids).append("'] WHERE user_id = '")
                .append(id).append("'; APPLY BATCH;");*/
        //String query = sb.toString();
        //System.out.println(query);


        session.execute("BEGIN BATCH INSERT INTO upper_limb.session_info (session_id, sessdata, timestamp) VALUES('" +
                jsonObject.get("session_id").textValue()+ "', "+jsonObject.get("session_data")+", '" + dformat.format(date) + "');"+
                "UPDATE upper_limb.user_info SET sessions = sessions + ['" + json.get("session_id").asText() + "'] WHERE user_id = '" + id +"'; APPLY BATCH;");
        //session.execute(query, (ids, e1, e2, imu1, imu2, imu3, dformat.format(date)));
        session.close();

        return created(ApplicationUtil.createResponse(jsonObject, true));
    }


    public Result retrieveSession(String patient_id, String id, java.util.Optional<java.lang.Long> init, java.util.Optional<java.lang.Long> end) throws ParseException {
        logger.debug("In DBController.retrieveSession(), retrieve session with id: {}",id);
        /*if (DataStore.getSession(patient_id,id) == null) {
            return notFound(ApplicationUtil.createResponse("Session with id:" + id + " not found", false));
        }*/

        //JsonNode jsonObject = null;
        Sess s = new Sess();
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
                     //.addContactPoint(127.0.0.1)
                    .build();
            CodecRegistry codecRegistry = session.getContext().getCodecRegistry();
            // The target user-defined type
            UserDefinedType dataUdt =
                session
                        .getMetadata()
                        .getKeyspace("...")
                        .flatMap(ks -> ks.getUserDefinedType("raw_data"))
                        .orElseThrow(IllegalStateException::new);
        // The "inner" codec that handles the conversions from CQL from/to UdtValue
        TypeCodec<UdtValue> innerCodec = codecRegistry.codecFor(dataUdt);
// The mapping codec that will handle the conversions from/to UdtValue and Coordinates
        DataCodec dataCodec = new DataCodec(innerCodec);
// Register the new codec
        ((MutableCodecRegistry) codecRegistry).register(dataCodec);

            ResultSet rs = session.execute("SELECT * FROM upper_limb.session_info WHERE session_id ='"+ id + "';");
            Row row = rs.one();
            System.out.println("hola");
            if (row != null) {
                s.setSessionID(row.getString("session_id"));
                String d= row.getString("timestamp");
                Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(d);
                //ZoneId defaultZoneId = ZoneId.systemDefault();
                s.setTimestamp(date1);
                List<RawData> rawDataList = row.getList("sessdata",RawData.class);
                System.out.println(rawDataList);
                ArrayList<RawData> rawDataArrayList = new ArrayList<>();
                rawDataArrayList.addAll(rawDataList);
                s.setData(rawDataArrayList);
                System.out.println(s);
                /*DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                try {
                    Date date = format.parse(d);
                    patient.setDob(date);       }catch (Exception e){
                    e.printStackTrace();
                }*/


            //}
            }else{
                System.out.println("It was null");
            }
            JsonNode jsonObject = Json.toJson(s);
            session.close();
        logger.debug("In DBController.retrieveSession(), result is: {}",jsonObject.toString());

        return ok(ApplicationUtil.createResponse(jsonObject, true));
    }



    public Result listSessions(String patient_id) {
        java.util.Map<String,Sess> result = DataStore.getSessions(patient_id);
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
