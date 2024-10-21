package Models;

import java.time.Year;

public class Place {
    private String address;
    private String city;
    private int capacity;
    private Year constructionYear;

    public Place(String address, String city, int capacity, Year constructionYear) {
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.constructionYear = constructionYear;
    }
    public Place() {

    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Year getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Year constructionYear) {
        this.constructionYear = constructionYear;
    }
}
