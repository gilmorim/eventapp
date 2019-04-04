import AlertFactory.AlertFactory;
import AlertFactory.GeneratedAlert;
import AlertList.AlertList;
import Alerts.Alert;
import Runnables.AlertRemover;
import Utils.AlertDurationMap;
import Utils.TimeStamper;
import Vehicle.Vehicle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Timer;

public class TestClass{
    public static void main(String[] args) throws IOException, ParseException {
        Vehicle v = new Vehicle();
        v.fromFile("./src/main/resources/cardata.cfg");

        AlertDurationMap alertDurationMap = new AlertDurationMap();
        alertDurationMap.fromFile("./src/main/resources/alerttime.cfg");

        AlertList alertList = new AlertList();

        AlertFactory alertFactory = new AlertFactory();

        // TODO: improve timestamper syntax
        TimeStamper timeStamper = new TimeStamper();

        // TODO: add file error control
        BufferedReader timeReader = new BufferedReader(new FileReader("./src/main/resources/alertcleanup.cfg"));
        int cleanupPeriod = Integer.parseInt(timeReader.readLine());

        // scheduled task to clean expired alerts from queue
        // TODO: apply concurrency control to alert list access
        Timer removerTimer = new Timer();
        removerTimer.schedule(new AlertRemover(alertList), cleanupPeriod, cleanupPeriod);


        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String userIn;
        System.out.print(">> ");

        // TODO: add error codes
        while(!(userIn = userInput.readLine()).equals("exit")){
            String[] options = userIn.split(" ");
            switch(options[0]){
                // list vehicle information
                case "vinfo" : {
                    System.out.println(v.toString());
                    break;
                }

                // generate alert
                case "alert" : {
                    GeneratedAlert generatedAlert = new GeneratedAlert();
                    generatedAlert = alertFactory.generate(
                            options[1],
                            Double.parseDouble(options[2]),
                            Double.parseDouble(options[3]),
                            v.getVin(),
                            options[4]);

                    Alert alert = generatedAlert.getAlert();
                    int errorCode = generatedAlert.getErrorCode();

                    if(errorCode == 0){
                        alert.setDuration(alertDurationMap.durationByAlertType(alert.getClass().getSimpleName()));
                        alert.setExpirationInstant();
                        alertList.addAlert(alert);
                        System.out.println("Alert added successfully: " + alert.toString());
                    } else {
                        System.out.println("error creating alert");
                    }
                    break;
                }

                case "help" : {
                    displayHelp();
                    break;
                }
                default: {
                    System.out.println("unknown command: " + userIn);
                }
            }
            System.out.print(">> ");
        }
    }

    public static void displayHelp(){
        System.out.println("helper menu");
    }
}