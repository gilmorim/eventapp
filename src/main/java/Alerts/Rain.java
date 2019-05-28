package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Rain extends Alert {

    String type;

    public Rain(String originVehicle, double x, double y, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, timestamp, remainingTransmissions);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Rain(String originVehicle, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, creationInstant, timestamp, remainingTransmissions);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Rain(Rain r){
        super(r.getOriginVehicle(), r.getX(), r.getY(), r.getCreationInstant(), r.getRemainingTransmissions());
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
                + " " + super.getOriginVehicle()
                + " " + super.getX()
                + " " + super.getY()
                + " " + super.getCreationInstant()
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