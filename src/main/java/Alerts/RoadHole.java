package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoadHole extends Alert {
    private String type;

    public RoadHole(String originVehicle, double x, double y, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, timestamp, remainingTransmissions);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public RoadHole(String originVehicle, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, creationInstant, timestamp, remainingTransmissions);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public RoadHole(RoadHole rh){
        super(rh.getOriginVehicle(), rh.getX(), rh.getY(), rh.getCreationInstant(), rh.getRemainingTransmissions());
        type = rh.getType();
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
    public RoadHole clone(){
        return new RoadHole(this);
    }
}
