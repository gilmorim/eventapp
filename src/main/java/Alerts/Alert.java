package Alerts;

import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Alert {
    private double x;
    private double y;
    private String creationInstant;
    private String expirationInstant;
    private String description;
    private int duration;

    public Alert(double x, double y, String creationInstant){
        this.x = x;
        this.y = y;
        this.creationInstant = creationInstant;
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

    public void setExpirationInstant() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date creationDate = format.parse(creationInstant);
        Date expirationDate = DateUtils.addMinutes(creationDate, duration);
        expirationInstant = format.format(expirationDate);
    }

    public boolean isExpired() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Date expirationDate = format.parse(expirationInstant);
        return currentDate.after(expirationDate);
    }
}
