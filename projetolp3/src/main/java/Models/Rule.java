package Models;

public class Rule {
    private int idRule;
    private int idSport;
    private String desc;

    /**
     * Constructor of Rule
     * @param idRule {int} Rule ID
     * @param idSport {int} Sport ID
     * @param desc {String} Description
     */
    public Rule(int idRule, int idSport, String desc) {
        this.idRule = idRule;
        this.idSport = idSport;
        this.desc = desc;
    }

    /**
     * Get Rule ID
     * @return int
     */
    public int getIdRule() {
        return idRule;
    }

    /**
     * Set Rule ID
     * @param idRule {int} ID
     */
    public void setId(int idRule) {
        this.idRule = idRule;
    }

    /**
     * Get Sport ID
     * @return int
     */
    public int getIdSport() {
        return idSport;
    }

    /**
     * Set Sport ID
     * @param idSport {int} Sport ID
     */
    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    /**
     * Get Desc
     * @return String
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set Desc
     * @param desc {String} Desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
