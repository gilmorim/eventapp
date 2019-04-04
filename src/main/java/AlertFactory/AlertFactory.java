package AlertFactory;

import Alerts.RoadHole;
import Utils.TimeStamper;

import java.util.Date;

public class AlertFactory {
    public AlertFactory(){}

    //TODO: add validation to inserted parameters
    public GeneratedAlert generate(String type, double x, double y, String vin, String detail){
        GeneratedAlert generatedAlert = new GeneratedAlert();
        switch (type){
            case "-h" : {
                RoadHole roadHole = new RoadHole(vin, x, y, new TimeStamper().now(new Date()), detail);
                generatedAlert.setAlert(roadHole);
                generatedAlert.setErrorCode(0);
                break;
            }
            default:{

            }
        }
        return generatedAlert;
    }
}
