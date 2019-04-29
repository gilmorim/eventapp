package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoadBlock extends Alert {

    String type;

    public RoadBlock(String originVehicle, double x, double y, String timestamp, String type, int remainingTransmissions){
        super(originVehicle, x, y, timestamp, remainingTransmissions);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public RoadBlock(RoadBlock rb){
        super(rb.getOriginVehicle(), rb.getX(), rb.getY(), rb.getCreationInstant(), rb.getRemainingTransmissions());
        this.setDescription(this.getClass().getSimpleName());
        this.type = rb.getType();
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
    public RoadBlock clone(){
        return new RoadBlock(this);
    }
}