package Models;

import java.util.List;

public class Event {
    private int year;
    private String country;
    private String logo;
    private List<Place> place;

    /**
     * Constructor of Event
     * @param year {int} Year
     * @param country {String} Country
     * @param logo {String} Logo
     * @param place {List<Place>} List of Places
     */
    public Event(int year, String country, String logo, List<Place> place) {
        this.year = year;
        this.country = country;
        this.logo = logo;
        this.place = place;
    }

    /**
     * Get year
     * @return int
     */
    public int getYear() {
        return year;
    }

    /**
     * Get country
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get logo
     * @return String
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Set year
     * @param year {int} Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get country
     * @param country {String} Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Set logo
     * @param logo {String} Logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * Get places
     * @return List<Place>
     */
    public List<Place> getPlace() {
        return place;
    }

    /**
     * Set places
     * @param place {List<Place>} Places
     */
    public void setPlace(List<Place> place) {
        this.place = place;
    }
}
