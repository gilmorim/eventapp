import AlertFactory.AlertFactory;
import AlertFactory.GeneratedAlert;
import AlertList.AlertList;
import Alerts.Alert;
import Runnables.AlertRemover;
import Utils.AlertDurationMap;
import Utils.Vars;
import Vehicle.Vehicle;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Timer;

public class TestClass{
    public static void main(String[] args) throws IOException, ParseException {
        Vehicle v = new Vehicle();
        v.fromFile("./src/main/resources/cardata.cfg");

        AlertDurationMap alertDurationMap = new AlertDurationMap();
        alertDurationMap.fromFile("./src/main/resources/alerttime.cfg");

        AlertList alertList = new AlertList();

        AlertFactory alertFactory = new AlertFactory();

        File cleanupTimeFile = new File("./src/main/resources/alertcleanup.cfg");
        if(!cleanupTimeFile.exists()){
            System.out.println("Cleanup time file not found. Exiting...");
            return;
        }

        BufferedReader timeReader = new BufferedReader(new FileReader(cleanupTimeFile));
        String cleanupStrAux;
        int cleanupPeriod ;

        // Read cleanup time file
        try{
            cleanupStrAux = timeReader.readLine();
            cleanupPeriod = Integer.parseInt(cleanupStrAux);

            // Make sure time range is between 1 and 30 minutes
            if(cleanupPeriod < 60000 || cleanupPeriod > 1800000){
                System.out.println("Cleanup time file bad range: make sure it is between 60000 and 1800000");
                return;
            }

        } catch (IOException e){
            System.out.println("Exiting with error reading time cleanup file: " + e.getMessage());
            return;
        } catch (NumberFormatException e){
            System.out.println(e.getMessage() + ": exiting with error. Please guarantee that file contains a valid value ");
            return;
        }

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

                // TODO: this probably can be done better
                // generate alert
                case "alert" : {
                    if(options.length < 5) {
                        System.out.println("not enough arguments for alert option");
                    } else {

                        StringBuilder errorMessage = new StringBuilder();
                        double x, y;

                        try {
                            x = Double.parseDouble(options[2]);
                            y = Double.parseDouble(options[3]);
                        } catch (NumberFormatException e){
                            System.out.println("error creating alert: invalid position");
                            break;
                        }
                        String eventType = options[1];
                        String vin = v.getVin();
                        String detail = options[4];

                        if(!isPositionValid(x, y)){
                            errorMessage.append("invalid position; ") ;
                        }

                        if(!isDetailValid(detail, eventType)){
                            errorMessage.append("invalid detail");
                        }

                        if (errorMessage.toString().equals("")) {

                            GeneratedAlert generatedAlert;
                            generatedAlert = alertFactory.generate(eventType, x, y, vin, detail);

                            Alert alert = generatedAlert.getAlert();
                            alert.setDuration(alertDurationMap.durationByAlertType(alert.getClass().getSimpleName()));
                            alert.setExpirationInstant();
                            alertList.addAlert(alert);

                            System.out.println("Alert added successfully: " + alert.toString());

                        } else {
                            System.out.println("error creating alert: " + errorMessage);
                        }
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

    private static void displayHelp(){
        System.out.println("helper menu");
    }

    public static boolean isPositionValid(double x, double y){
        return !( x > 99.9) && !(x < 0.0) && !(y > 99.9) && !(y < 0.0);
    }

    public static boolean isDetailValid(String detail, String type){
        boolean valid = false;
        switch (type){
            case "-c" : {
                valid = Arrays.asList(Vars.CRASH_TYPES).contains(detail);
                break;
            }
            case "-f" : {
                valid = Arrays.asList(Vars.FOG_TYPES).contains(detail);
                break;
            }
            case "-r" : {
                valid = Arrays.asList(Vars.RAIN_TYPES).contains(detail);
                break;
            }
            case "-b" : {
                valid = Arrays.asList(Vars.ROADBLOCK_TYPES).contains(detail);
                break;
            }
            case "-h" : {
                valid = Arrays.asList(Vars.ROADHOLE_STATUSES).contains(detail);
                break;
            }
            case "-s" : {
                valid = Arrays.asList(Vars.SNOW_TYPES).contains(detail);
                break;
            }
            case "-t" : {
                valid = Arrays.asList(Vars.TRAFFICJAM_TYPE).contains(detail);
                break;
            }
        }
        return valid;
    }
}