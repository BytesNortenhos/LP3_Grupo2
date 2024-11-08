package Models;

public class Country {
    private int idCountry;
    private String name;
    private String continent;

    /**
     * Constructor of Country
     * @param idCountry {int} ID
     * @param name {String} Name
     * @param continent {String} Continent
     */
    public Country(int idCountry, String name, String continent) {
        this.idCountry = idCountry;
        this.name = name;
        this.continent = continent;
    }

    /**
     * Constructor of Country (without parameters)
     */
    public Country() {}

    /**
     * Get id
     * @return int
     */
    public int getIdCountry() {
        return idCountry;
    }

    /**
     * Set id
     * @param idCountry {int} ID
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
