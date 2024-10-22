package Models;

import java.util.List;

public class Event {
    private int id;
    private int year;
    private Country country;
    private String logo;
    private List<Place> places;

    /**
     * Constructor of Event
     * @param id {int} ID
     * @param year {int} Year
     * @param country {Country} Country
     * @param logo {String} Logo
     * @param places {List<Place>} List of Places
     */
    public Event(int id, int year, Country country, String logo, List<Place> places) {
        this.id = id;
        this.year = year;
        this.country = country;
        this.logo = logo;
        this.places = places;
    }

    /**
     * Get ID
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID
     * @param id {int} ID
     */
    public void setId(int id) {
        this.id = id;
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
     * @return Country
     */
    public Country getCountry() {
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
     * @param country {Country} Country
     */
    public void setCountry(Country country) {
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
    public List<Place> getPlaces() {
        return places;
    }

    /**
     * Set places
     * @param places {List<Place>} Places
     */
    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
