package Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AlertDurationMap {
    private HashMap<String, Integer> timeMap;

    public AlertDurationMap(){
        timeMap = new HashMap<>();
    }

    public HashMap<String, Integer> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(HashMap<String, Integer> timeMap) {
        this.timeMap = timeMap;
    }

    public void fromFile(String file){
        String[] alertTime;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null){
                alertTime = line.split(" ");
                timeMap.put(alertTime[0], Integer.parseInt(alertTime[1]));
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("configuration file not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int durationByAlertType(String alertType){
        return timeMap.get(alertType);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(String s : timeMap.keySet()){
            sb.append(s + " " + timeMap.get(s) + "\n");
        }

        return sb.toString();
    }
}
