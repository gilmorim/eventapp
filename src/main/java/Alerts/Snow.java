package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Snow extends Alert {

    String type;

    public Snow(String originVehicle, double x, double y, String timestamp, String type){
        super(originVehicle, x, y, timestamp);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Snow(Snow s){
        super(s.getOriginVehicle(), s.getX(), s.getY(), s.getCreationInstant());
        this.setDescription(this.getClass().getSimpleName());
        this.type = s.getType();
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
                + " " + getType());
        return sb.toString();
    }

    @Override
    public Snow clone(){
        return new Snow(this);
    }
}