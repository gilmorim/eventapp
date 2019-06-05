package Vehicle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Vehicle {
    private String vin;
    private String brand;
    private String model;
    private int year;

    private String currentRsu;
    private double currentX;
    private double currentY;

    private static Vehicle vehicle = null;

    public Vehicle(){
        this.vin = "samplevin";
        this.brand="samplebrand";
        this.model="samplemodel";
        this.year = 1800;
    }

    public Vehicle(String vin, String brand, String model, int year, double currentX, double currentY){
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public Vehicle(Vehicle v){
        this.vin = v.getVin();
        this.brand = v.getBrand();
        this.model = v.getModel();
        this.year = v.getYear();
        this.currentX = v.getCurrentX();
        this.currentY = v.getCurrentY();
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCurrentRsu() {
        return currentRsu;
    }

    public void setCurrentRsu(String currentRsu) {
        this.currentRsu = currentRsu;
    }

    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    //TODO: proper file reading verification
    public void fromFile(String file) {
        String[] carDetails;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            line = reader.readLine();
            reader.close();
            carDetails = line.split(" ");

            this.setBrand(carDetails[0]);
            this.setModel(carDetails[1]);
            this.setVin(carDetails[2]);
            this.setYear(Integer.parseInt(carDetails[3]));
            this.setCurrentX(Double.parseDouble(carDetails[4]));
            this.setCurrentY(Double.parseDouble(carDetails[5]));

        } catch (FileNotFoundException e) {
            System.out.println("configuration file not found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static Vehicle getInstance(){
        if(vehicle == null)
            vehicle = new Vehicle();

        return vehicle;
    }

    @Override
    public String toString(){
        return vin + " " + brand + " " + model + " " + year + " " + currentX + " " + currentY;
    }

    @Override
    public Vehicle clone(){
        return new Vehicle(this);
    }
}
