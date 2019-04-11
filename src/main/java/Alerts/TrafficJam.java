package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TrafficJam extends Alert {

    public TrafficJam(String originVehicle, double x, double y, String timestamp){
        super(originVehicle, x, y, timestamp);
        this.setDescription(this.getClass().getSimpleName());
    }

    public TrafficJam(TrafficJam tj){
        super(tj.getOriginVehicle(), tj.getX(), tj.getY(), tj.getCreationInstant());
        this.setDescription(this.getClass().getSimpleName());
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
                + " " + super.getExpirationInstant());
        return sb.toString();
    }

    @Override
    public TrafficJam clone(){
        return new TrafficJam(this);
    }
}
