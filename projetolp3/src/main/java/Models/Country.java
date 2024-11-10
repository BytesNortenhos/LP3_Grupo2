package Models;

public class Country {
    private int idCountry;
    private String name;
    private String continent;

    /**
     * Constructor of Country
     * @param idCountry {String} ID
     * @param name {String} Name
     * @param continent {String} Continent
     */
    public Country(int idCountry, String name, String continent) {
        this.idCountry = idCountry;
        this.name = name;
        this.continent = continent;
    }

    /**
     * Get id
     *
     * @return String
     */
    public int getIdCountry() {
        return idCountry;
    }

    /**
     * Set id
     * @param idCountry {String} ID
     */
    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    /**
     * Get name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name {String} Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get continent
     * @return String
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Set continent
     * @param continent {String} Continent
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }
}
