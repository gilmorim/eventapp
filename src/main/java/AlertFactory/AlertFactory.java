package AlertFactory;

import Alerts.*;
import Utils.TimeStamper;
import com.google.gson.JsonObject;

import java.util.Date;

public class AlertFactory {

    public AlertFactory() {
    }

    public GeneratedAlert generateFromParams(String type, double x, double y, String vin, String detail, int retransmissions, String lastRetransmitter) {
        GeneratedAlert generatedAlert = new GeneratedAlert();

        switch (type) {
            case "-h": {
                RoadHole roadHole = new RoadHole(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(roadHole);
                break;
            }
            case "-c": {
                Crash crash = new Crash(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(crash);
                break;
            }
            case "-f": {
                Fog fog = new Fog(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(fog);
                break;
            }
            case "-r": {
                Rain rain = new Rain(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(rain);
                break;
            }
            case "-b": {
                RoadBlock roadBlock = new RoadBlock(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(roadBlock);
                break;
            }
            case "-s": {
                Snow snow = new Snow(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(snow);
                break;
            }
            case "-t": {
                TrafficJam trafficJam = new TrafficJam(vin, x, y, new TimeStamper().now(new Date()), detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(trafficJam);
                break;
            }
            default: {
                generatedAlert.setAlert(null);
                generatedAlert.appendError("invalid alert type " + type);
                break;
            }
        }
        return generatedAlert;
    }

    public GeneratedAlert generateFromJson(JsonObject jsonObject) {
        GeneratedAlert generatedAlert = new GeneratedAlert();
        String type = jsonObject.get("description").getAsString();
        String vin = jsonObject.get("origin").getAsString();
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        String creationInstant = "1900-01-01 00:00:00";
        String timestamp = jsonObject.get("expirationInstant").getAsString();
        String detail = jsonObject.get("type").getAsString();
        int retransmissions = 1;
        String lastRetransmitter = jsonObject.get("lastRetransmitter").getAsString();

        switch (type) {
            case "RoadHole": {
                RoadHole roadHole = new RoadHole(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(roadHole);
                break;
            }
            case "Crash": {
                Crash crash = new Crash(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(crash);
                break;
            }
            case "Fog": {
                Fog fog = new Fog(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(fog);
                break;
            }
            case "Rain": {
                Rain rain = new Rain(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(rain);
                break;
            }
            case "RoadBlock": {
                RoadBlock roadBlock = new RoadBlock(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(roadBlock);
                break;
            }
            case "Snow": {
                Snow snow = new Snow(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(snow);
                break;
            }
            case "TrafficJam": {
                TrafficJam trafficJam = new TrafficJam(vin, x, y, creationInstant, timestamp, detail, retransmissions, lastRetransmitter);
                generatedAlert.setAlert(trafficJam);
                break;
            }
            default: {
                generatedAlert.setAlert(null);
                generatedAlert.appendError("invalid alert type " + type);
                break;
            }
        }
        return generatedAlert;
    }
}