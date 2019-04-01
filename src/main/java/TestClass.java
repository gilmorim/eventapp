import AlertList.AlertList;
import Alerts.RoadHole;
import Utils.AlertDurationMap;
import Utils.TimeStamper;
import Vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;

public class TestClass{
    public static void main(String[] args) throws IOException, ParseException {
        Vehicle v = new Vehicle();
        v.fromFile("./src/main/resources/cardata.cfg");

        AlertDurationMap alertDurationMap = new AlertDurationMap();
        alertDurationMap.fromFile("./src/main/resources/alerttime.cfg");

        AlertList al = new AlertList();

        TimeStamper timeStamper = new TimeStamper();

        RoadHole rh = new RoadHole(32.1, 54.4, timeStamper.now(new Date()), "rough");
        rh.setDuration(alertDurationMap.durationByAlertType(rh.getClass().getSimpleName()));
        rh.setExpirationInstant();

        al.addAlert(rh);
        System.out.println(al.toString() + rh.isExpired());

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String userIn;

        while(!(userIn = userInput.readLine()).equals("exit")){
            switch(userIn){
                case "vinfo" : {
                    System.out.println(v.toString());
                    break;
                }
                default: {
                    System.out.println("unknown command: " + userIn);
                }
            }
        }
    }
}