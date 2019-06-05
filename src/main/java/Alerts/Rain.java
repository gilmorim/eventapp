package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Rain extends Alert {

    String type;

    public Rain(String origin, double x, double y, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, timestamp, remainingTransmissions, lastRetransmitter);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Rain(String origin, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, creationInstant, timestamp, remainingTransmissions, lastRetransmitter);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Rain(Rain r){
        super(r.getOrigin(), r.getX(), r.getY(), r.getCreationInstant(), r.getRemainingTransmissions(), r.getLastRetransmitter());
        this.setDescription(this.getClass().getSimpleName());
        this.type = r.getType();
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
    public Rain clone(){
        return new Rain(this);
    }
}