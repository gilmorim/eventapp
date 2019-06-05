package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Crash extends Alert {

    private String type;

    public Crash(String origin, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, creationInstant, timestamp, remainingTransmissions, lastRetransmitter);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public Crash(String origin, double x, double y, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, timestamp, remainingTransmissions, lastRetransmitter);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public Crash(Crash c){
        super(c.getOrigin(), c.getX(), c.getY(), c.getCreationInstant(), c.getRemainingTransmissions(), c.getLastRetransmitter());
        this.type = c.getType();
        this.setDescription(this.getClass().getSimpleName());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toJson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        return gson.toJson(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDescription()
                + " " + super.getOrigin()
                + " " + super.getX()
                + " " + super.getY()
                + " " + super.getExpirationInstant()
                + " " + super.getRemainingTransmissions()
                + " " + getType());
        return sb.toString();
    }

    @Override
    public Crash clone(){
        return new Crash(this);
    }
}