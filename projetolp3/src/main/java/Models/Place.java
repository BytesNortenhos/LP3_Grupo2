package Models;

import java.time.Year;

public class Place {
    private String address;
    private String city;
    private int capacity;
    private int constructionYear;

    /**
     * Constructor of place
     * @param address {String} Address
     * @param city {String} City
     * @param capacity {int} Capacity
     * @param constructionYear {int} Year of construction
     */
    public Place(String address, String city, int capacity, int constructionYear) {
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.constructionYear = constructionYear;
    }

    public Place() {}

    /**
     * Get address
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     * @param address {String} Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get city
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city
     * @param city {String} City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get capacity
     * @return int
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set capacity
     * @param capacity {int} Capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get Year of construction
     * @return int
     */
    public int getConstructionYear() {
        return constructionYear;
    }

    /**
     * Set year of construction
     * @param constructionYear {int} Year of construction
     */
    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }
}
