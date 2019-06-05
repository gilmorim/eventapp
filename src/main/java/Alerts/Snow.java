package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Snow extends Alert {

    String type;

    public Snow(String origin, double x, double y, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, timestamp, remainingTransmissions, lastRetransmitter);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Snow(String origin, double x, double y, String creationInstant, String timestamp, String type, int remainingTransmissions, String lastRetransmitter){
        super(origin, x, y, creationInstant, timestamp, remainingTransmissions, lastRetransmitter);
        this.setDescription(this.getClass().getSimpleName());
        this.type = type;
    }

    public Snow(Snow s){
        super(s.getOrigin(), s.getX(), s.getY(), s.getCreationInstant(), s.getRemainingTransmissions(), s.getLastRetransmitter());
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
                + " " + super.getOrigin()
                + " " + super.getX()
                + " " + super.getY()
                + " " + super.getExpirationInstant()
                + " " + super.getRemainingTransmissions()
                + " " + getType());
        return sb.toString();
    }

    @Override
    public Snow clone(){
        return new Snow(this);
    }
}