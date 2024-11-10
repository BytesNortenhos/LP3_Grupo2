package Models;

public class MedalType {
    private int idMedalType;
    private String descMedalType;

    public MedalType(int idMedalType, String descMedalType) {
        this.idMedalType = idMedalType;
        this.descMedalType = descMedalType;
    }

    /**
     * Get ID
     * @return int
     */
    public int getId() {
        return idMedalType;
    }

    /**
     * Set ID
     * @param idMedalType {int} ID
     */
    public void setId(int idMedalType) {
        this.idMedalType = idMedalType;
    }

    /**
     * Get Desc Medal Type
     * @return String
     */
    public String getDescMedalType() {
        return descMedalType;
    }

    /**
     * Set Desc Medal Type
     * @param descMedalType {String} Desc
     */
    public void setDescMedalType(String descMedalType) {
        this.descMedalType = descMedalType;
    }
}
