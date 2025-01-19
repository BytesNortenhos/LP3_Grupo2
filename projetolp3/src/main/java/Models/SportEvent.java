package Models;

public class SportEvent {
    private int idSport;
    private int year;
    private String guidApi;

    /**
     * Constructor of SportEvent
     *
     * @param idSport {int} Sport ID
     * @param year    {int} Year
     */
    public SportEvent(int idSport, int year) {
        this.idSport = idSport;
        this.year = year;
    }

    /**
     * Constructor of SportEvent
     *
     * @param idSport {int} Sport ID
     * @param year    {int} Year
     * @param guidApi {String} GUID API
     */
    public SportEvent(int idSport, int year, String guidApi) {
        this.idSport = idSport;
        this.year = year;
        this.guidApi = guidApi;
    }

    /**
     * Get Sport ID
     *
     * @return int
     */
    public int getIdSport() {
        return idSport;
    }

    /**
     * Set Sport ID
     *
     * @param idSport {int} Sport ID
     */
    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    /**
     * Get Year
     *
     * @return int
     */
    public int getYear() {
        return year;
    }

    /**
     * Set Year
     *
     * @param year {int} Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get GUID API
     * @return String
     */
    public String getGuidApi() {
        return guidApi;
    }

    /**
     * Set GUID API
     * @param guidApi {String} GUID API
     */
    public void setGuidApi(String guidApi) {
        this.guidApi = guidApi;
    }
}

