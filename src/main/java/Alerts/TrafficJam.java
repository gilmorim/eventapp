package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TrafficJam extends Alert {

    String type;

    public TrafficJam(String originVehicle, double x, double y, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, timestamp, remainingTransmissions);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public TrafficJam(String originVehicle, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, creationInstant, timestamp, remainingTransmissions);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }
    public TrafficJam(TrafficJam tj){
        super(tj.getOriginVehicle(), tj.getX(), tj.getY(), tj.getCreationInstant(), tj.getRemainingTransmissions());
        this.setDescription(this.getClass().getSimpleName());
        this.type = tj.getType();
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
    public TrafficJam clone(){
        return new TrafficJam(this);
    }
}
