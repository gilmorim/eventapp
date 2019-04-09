package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoadHole extends Alert {
    private String depth;

    public RoadHole(String originVehicle, double x, double y, String timestamp, String depth){
        super(originVehicle, x, y, timestamp);
        this.depth = depth;
        this.setDescription(this.getClass().getSimpleName());
    }

    public RoadHole(RoadHole rh){
        super(rh.getOriginVehicle(), rh.getX(), rh.getY(), rh.getCreationInstant());
        depth = rh.getDepth();
        this.setDescription(this.getClass().getSimpleName());
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
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
                + " " + getDepth());
        return sb.toString();
    }

    @Override
    public RoadHole clone(){
        return new RoadHole(this);
    }
}
