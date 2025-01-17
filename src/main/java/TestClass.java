/**
 * @author Gilberto Morim
 * @email gilgmorim@gmail.com
 * @create date 2019-05-14 14:54:50
 * @modify date 2019-05-14 14:54:50
 * @desc Main class for DTN application
 */

import AlertFactory.AlertFactory;
import AlertFactory.GeneratedAlert;
import AlertList.Display;
import AlertList.IncomingAlertsBuffer;
import Alerts.Alert;
import DTN.DTNAddressList;
import DTN.DTNReceiver;
import DTN.DTNSender;
import Runnables.AlertRemover;
import Statistics.Statistics;
import Utils.AlertDurationMap;
import Utils.Vars;
import Vehicle.Vehicle;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass{
    public static void main(String[] args) throws IOException, ParseException {
        int retransmissions = 5;

        /* Read vehicle information from configuration file */
        Vehicle v = Vehicle.getInstance();
        v.fromFile("cardata.cfg");

        /* Read lifespan of each alert type from configuration file */    
        AlertDurationMap alertDurationMap = new AlertDurationMap();
        alertDurationMap.fromFile("alerttime.cfg");

        /* Two alert buffers: one for display to driver, other to store alerts generated, or received from other sources */    
        Display display = new Display();
        IncomingAlertsBuffer alertBuffer  = new IncomingAlertsBuffer();

        /* Alert generator */
        AlertFactory alertFactory = new AlertFactory();

        // Logger logger = new Logger();
        ReentrantLock lock = new ReentrantLock();

        /* Read file with cleanup timer, to remove expired alerts */
        File cleanupTimeFile = new File("alertcleanup.cfg");
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

        Statistics statistics = Statistics.getInstance();


        // scheduled task to clean expired alerts from queue
        // TODO: apply concurrency control to alert list access
        Timer removerTimer = new Timer();
        removerTimer.schedule(new AlertRemover(display, lock), cleanupPeriod, cleanupPeriod);

        /* Reach all interfaces in a local link */
        InetAddress group = InetAddress.getByName("ff02::01");

        DTNAddressList addressList = new DTNAddressList(group);
        addressList.initialize();
        // System.out.println("valid addresses: " + addressList.getAddressList().toString());

        /* Two objects to handle transmission and reception of alerts, each running on their respective thread */
        DTNSender sender = new DTNSender(addressList, alertBuffer, lock);
        DTNReceiver receiver = new DTNReceiver(addressList, alertBuffer, display, lock);
        receiver.start();

        Timer senderTimer = new Timer();
        senderTimer.schedule(sender, sender.getDelayInMillis(), 5000);

        /**
         *
         * User input section. Can generate single alert, read alerts from a file, list all alerts
         *
         */
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String userIn;
        System.out.println("Welcome to the event app generator. Type \"help\"  to display usage instructions and examples\n");
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

                case "at" : {
                    if(options.length < 3) {
                        System.out.println("not enough arguments for at option");
                    } else {
                        v.setCurrentX(Double.parseDouble(options[1]));
                        v.setCurrentY(Double.parseDouble(options[2]));
                    }
                    break;
                }

                // TODO: this probably can be done better
                // generate alert
                case "alert" : {
                    if(options.length < 3) {
                        System.out.println("not enough arguments for alert option");
                    } else {

                        StringBuilder errorMessage = new StringBuilder();

                        String eventType = options[1];
                        String vin = v.getVin();
                        String detail = options[2];

                        if (errorMessage.toString().equals("")) {

                            GeneratedAlert generatedAlert;
                            generatedAlert = alertFactory.generateFromParams(eventType, v.getCurrentX(), v.getCurrentY(), vin, detail, retransmissions, vin);

                            Alert alert = generatedAlert.getAlert();
                            alert.setDuration(alertDurationMap.durationByAlertType(alert.getClass().getSimpleName()));
                            alert.setExpirationInstant();

                            sender.sendSingleAlert(alert);

                            // filter repeated alerts
                            if(!display.hasAlert(alert) || !alertBuffer.hasAlert(alert)){
                                display.addAlert(alert);
                                display.appendToDisplayFile(alert.toString());
                                alertBuffer.addAlert(alert);
                                statistics.increaseGeneratedAlerts(alert.getDescription());
                                // System.out.println("Alert added successfully: " + alert.toString());
                            }
                            else
                                System.out.println("alert already exists. ignoring...");
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

                case "display" : {
                    if(display.getAlertList().isEmpty()){
                        System.out.println("alert list is empty");
                    } else {
                        System.out.println(display.toString());
                    }
                    break;
                }

                case "buffer" : {
                    System.out.println(sender.getAlertBuffer().toString());
                    break;
                }

                case "recv": {
                    System.out.println(statistics.receivedIndividual());
                    break;
                }

                case "recvpct" : {
                    System.out.println(statistics.receivedPercentages());
                    break;
                }

                case  "gen" : {
                    System.out.println(statistics.generatedIndividual());
                    break;
                }

                case "genpct" : {
                    System.out.println(statistics.generatedPercentages());
                    break;
                }

                case "trm" : {
                    System.out.println(statistics.transmittedIndividual());
                    break;
                }

                case "trmpct" : {
                    System.out.println(statistics.transmittedPercentages());
                    break;
                }

                case "alltrm" : {
                    System.out.println("Total of alerts transmitted: " + statistics.getAlertsTransmitted());
                    break;
                }

                case "allrecv" : {
                    System.out.println("Total of alerts received: " + statistics.getAlertsReceived());
                    break;
                }

                case "allgen" : {
                    System.out.println("Total of alerts generated: " + statistics.getAlertsGenerated());
                    break;
                }



                // TODO: duplicate code here, find way to recycle earlier code
                // read alerts from file instead of standard input
                case "rdfile" : {
                    if(options.length < 2){
                        System.out.println("insufficient arguments for rdfile option");
                    } else {
                        String line;
                        String path = options[1];
                        double x, y;
                        BufferedReader reader = Files.newBufferedReader(Paths.get(path));

                        int lineNumber = 0;
                        while((line = reader.readLine()) != null){
                            StringBuilder errorMessage = new StringBuilder();
                            lineNumber++;
                            String[] words = line.split(" ");
                            // comment or empty line support
                            if(words[0].charAt(0) == '#' || words[0].charAt(0) == '\n')
                                continue;
                            if(words.length < 2){
                                System.out.println("invalid line " + lineNumber +  ": " + line + " - not enough arguments. Skipping...");
                            } else {

                                String eventType = words[0];
                                String vin = v.getVin();
                                String detail = words[1];


                                if (errorMessage.toString().equals("")) {

                                    GeneratedAlert generatedAlert;
                                    generatedAlert = alertFactory.generateFromParams(eventType, v.getCurrentX(), v.getCurrentY(), vin, detail, retransmissions, vin);

                                    Alert alert = generatedAlert.getAlert();
                                    alert.setDuration(alertDurationMap.durationByAlertType(alert.getClass().getSimpleName()));
                                    alert.setExpirationInstant();

                                    // filter repeated alerts
                                    if(!display.hasAlert(alert) || !alertBuffer.hasAlert(alert)){
                                        display.addAlert(alert);
                                        display.appendToDisplayFile(alert.toString());
                                        alertBuffer.addAlert(alert);
                                        statistics.increaseGeneratedAlerts(alert.getDescription());
                                        // logger.logGenerationSuccess(alert.toString());
                                        //System.out.println("Alert added successfully: " + alert.toString());
                                    }
                                    else
                                        System.out.println("alert already exists. ignoring...");
                                } else {
                                    System.out.println("line " + lineNumber + " - error creating alert: " + errorMessage + ". Skipping...");
                                }
                            }
                        }
                    }
                    break;
                }

                case "db" : {
                    Vars.DEBUG_ENABLED = true;
                    break;
                }

                case "ndb" : {
                    Vars.DEBUG_ENABLED = false;
                    break;
                }

                default: {
                    System.out.println("unknown command: " + userIn);
                    break;
                }
            }
            System.out.print(">> ");
        }


        removerTimer.cancel();
        removerTimer.purge();

        senderTimer.cancel();
        senderTimer.purge();

        receiver.interrupt();
        // receiver.stop();

        System.out.println("exiting...");
    }

    /**
     *
     * Display help message when user inputs "help"
     *
     */
    private static void displayHelp(){
        System.out.println("###### EventApp Usage ###### \n" +
                "***** Alert creation:\n" +
                "\tSingle alert:\n\talert [OCCURRENCE OPTION] [TYPE]\n\n" +
                "\t\t\tOCCURRENCES\tOPTION\tTYPE\n" +
                "\t\t\tRoadHole\t-h\t\tlight, medium, rough\n" +
                "\t\t\tCrash\t\t-c\t\tsingle, chain\n" +
                "\t\t\tFog\t\t\t-f\t\tlight, medium, dense\n" +
                "\t\t\tRain\t\t-r\t\tlight, medium, pouring\n" +
                "\t\t\tRoadBlock\t-b\t\tobject, authority\n" +
                "\t\t\tSnow\t\t-s\t\tlight, medium, heavy\n" +
                "\t\t\tTrafficJam\t-t\t\tshort, medium, long\n\n" +
                "\t\t\tExample usage:\talert -h medium\n\n" +

                "\tFrom file:\trdfile [FILE]\n" +
                "\tExample usage:\trdfile alerts.txt\n\n" +

                "***** Display:\n" +
                "\tdisplay\t\tPrints active alerts\n" +
                "\tbuffer\t\tPrints transmission buffer content\n\n" +

                "***** Statistics:\n" +
                "\trecv\t\tPrints received alerts by type\n" +
                "\trecvpct\t\tPrints received alerts by type in percentage\n" +
                "\tallrecv\t\tPrints total number of received alerts\n" +
                "\tgen\t\t\tPrints generated alerts by type\n" +
                "\tgenpct\t\tPrints generated alerts by type in percentage\n" +
                "\tallgen\t\tPrints total number of generated alerts\n" +
                "\ttrm\t\t\tPrints transmitted alerts by type\n" +
                "\ttrmpct\t\tPrints transmitted alerts by type in percentage\n" +
                "\talltrm\t\tPrints total number of transmitted alerts\n");
    }

    /**
     * Makes sure input coordinates are within appropriate ranges
     * For x, has to be between 0.0 and 99.9
     * For y, has to be between 0.0 and 99.9
     */
    private static boolean isPositionValid(double x, double y){
        return !( x > 99.9) && !(x < 0.0) && !(y > 99.9) && !(y < 0.0);
    }

    /**
     * Guarantees that the inserted detail is one of a valid group of details, and rejects all others
     */
    private static boolean isDetailValid(String detail, String type){
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