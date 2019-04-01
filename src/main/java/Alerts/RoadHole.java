package Alerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoadHole extends Alert {
    private String depth;

    public RoadHole(double x, double y, String timestamp, String depth){
        super(x, y, timestamp);
        this.depth = depth;
        this.setDescription(this.getClass().getSimpleName());
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

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
                + " " + super.getX()
                + " " + super.getY()
                + " " + super.getCreationInstant()
                + " " + super.getExpirationInstant()
                + " " + getDepth());
        return sb.toString();
    }
}
