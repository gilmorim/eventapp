package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* TODO: CHANGE DURATION TO THE INHERITED CLASSES, AND REMOVE FROM HERE, SINCE EACH ALERT HAS DIFFERENT LIFESPAN
*   */
public abstract class Alert {
    private double x;
    private double y;
    private String creationInstant;
    private String expirationInstant;
    private String description;
    private int duration;
    private int remainingTransmissions;
    private String originVehicle;
    private boolean transmissible;

    public Alert(String originVehicle, double x, double y, String creationInstant, int remainingTransmissions){
        this.originVehicle = originVehicle;
        this.x = x;
        this.y = y;
        this.creationInstant = creationInstant;
        this.remainingTransmissions = remainingTransmissions;
        transmissible = true;
        duration = 2880; // default value of 2 days
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getCreationInstant() {
        return creationInstant;
    }

    public void setCreationInstant(String timestamp) {
        this.creationInstant = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getExpirationInstant() {
        return expirationInstant;
    }

    public int getRemainingTransmissions() {
        return remainingTransmissions;
    }

    public void setRemainingTransmissions(int remainingTransmissions) {
        this.remainingTransmissions = remainingTransmissions;
    }

    public boolean isTransmissible() {
        return transmissible;
    }

    public void setTransmissible(boolean transmissible) {
        this.transmissible = transmissible;
    }

    // TODO: SPAGHET CODE, THINK A BETTER WAY TO DO THIS
    public void setExpirationInstant() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date creationDate = format.parse(creationInstant);
        Date expirationDate = DateUtils.addMinutes(creationDate, duration);
        expirationInstant = format.format(expirationDate);
    }

    public void setExpirationInstant(String expirationInstant) {
        this.expirationInstant = expirationInstant;
    }

    public String getOriginVehicle() {
        return originVehicle;
    }

    public void setOriginVehicle(String originVehicle) {
        this.originVehicle = originVehicle;
    }


    public boolean isExpired() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Date expirationDate = format.parse(expirationInstant);
        return currentDate.after(expirationDate);
    }

    public String toJson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        return gson.toJson(this);
    }

    public void decreaseRemainingTransmissions(){
        remainingTransmissions--;
    }

    public boolean isTransmittable(){
        boolean transmittable = true;

        if(getRemainingTransmissions() < 1)
            transmittable = false;

        return transmittable;
    }

    public boolean equals(Alert a){
        return (a.getX() == this.getX() && a.getY() == this.getY() && a.getDescription() == this.getDescription());
    }
}
