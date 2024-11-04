package Models;

import java.util.List;

public class Event {
    private int year;
    private Country country;
    private String logo;

    /**
     * Constructor of Event
     * @param year {int} Year
     * @param country {Country} Country
     * @param logo {String} Logo
     */
    public Event(int year, Country country, String logo) {
        this.year = year;
        this.country = country;
        this.logo = logo;
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
}