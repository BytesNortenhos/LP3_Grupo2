package Models;

import java.util.List;

public class Event {

    private int year;
    private String country;
    private String logo;
    private List<Place> place;

    public Event(int year, String country, String logo, List<Place> place) {
        this.year = year;
        this.country = country;
        this.logo = logo;
        this.place = place;
    }

    public int getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getLogo() {
        return logo;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }
}
