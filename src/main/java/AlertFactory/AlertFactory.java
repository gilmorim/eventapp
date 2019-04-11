package AlertFactory;

import Alerts.*;
import Utils.TimeStamper;

import java.util.Date;

public class AlertFactory {

    public AlertFactory() {
    }

    public GeneratedAlert generate(String type, double x, double y, String vin, String detail) {
        GeneratedAlert generatedAlert = new GeneratedAlert();

        switch (type) {
            case "-h": {
                RoadHole roadHole = new RoadHole(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(roadHole);
                break;
            }
            case "-c": {
                Crash crash = new Crash(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(crash);
                break;
            }
            case "-f": {
                Fog fog = new Fog(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(fog);
                break;
            }
            case "-r": {
                Rain rain = new Rain(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(rain);
                break;
            }
            case "-b": {
                RoadBlock roadBlock = new RoadBlock(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(roadBlock);
                break;
            }
            case "-s": {
                Snow snow = new Snow(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(snow);
                break;
            }
            case "-t": {
                TrafficJam trafficJam = new TrafficJam(vin, x, y, new TimeStamper().now(new Date()), detail);
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