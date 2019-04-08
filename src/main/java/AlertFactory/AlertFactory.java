package AlertFactory;

import Alerts.RoadHole;
import Utils.TimeStamper;
import Utils.Vars;

import java.util.Arrays;
import java.util.Date;

public class AlertFactory {
    public AlertFactory(){}

    public GeneratedAlert generate(String type, double x, double y, String vin, String detail){
        GeneratedAlert generatedAlert = new GeneratedAlert();

        switch (type){
            case "-h" : {
                try{
                    if(!isPositionValid(x, y)){
                        generatedAlert.appendError("invalid position");
                    }
                    if(!isRoadHoleDetailValid(detail)) {
                        generatedAlert.appendError("invalid detail");
                    }

                    if(generatedAlert.getErrorMessage() == null){
                        RoadHole roadHole = new RoadHole(vin, x, y, new TimeStamper().now(new Date()), detail);
                        generatedAlert.setAlert(roadHole);
                    } else {
                        generatedAlert.setAlert(null);
                    }
                } catch (NumberFormatException e){
                    generatedAlert.setAlert(null);
                    generatedAlert.appendError("invalid position");
                }
                break;
            }
            default:{
                generatedAlert.appendError("invalid alert type " + type);
            }
        }
        return generatedAlert;
    }

    public boolean isPositionValid(double x, double y){
        return !( x > 99.9) && !(x < 0.0) && !(y > 99.9) && !(y < 0.0);
    }

    public boolean isRoadHoleDetailValid(String detail){
        return Arrays.asList(Vars.ROADHOLE_STATUSES).contains(detail);
    }
}

