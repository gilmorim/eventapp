package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoadHole extends Alert {
    private String type;

    public RoadHole(String origin, double x, double y, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, timestamp, remainingTransmissions, lastRetransmitter);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public RoadHole(String origin, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, creationInstant, timestamp, remainingTransmissions, lastRetransmitter);
        this.type = type;
        this.setDescription(this.getClass().getSimpleName());
    }

    public RoadHole(RoadHole rh){
        super(rh.getOrigin(), rh.getX(), rh.getY(), rh.getCreationInstant(), rh.getRemainingTransmissions(), rh.getLastRetransmitter());
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
                + " " + super.getOrigin()
                + " " + super.getX()
                + " " + super.getY()
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
