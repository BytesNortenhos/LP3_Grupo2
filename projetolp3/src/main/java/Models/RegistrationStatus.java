package Models;

public class RegistrationStatus {
    private int idStatus;
    private String desc;

    /**
     * Constructor of RegistrationStatus
     * @param idStatus {int} Status ID
     * @param desc {String} Desc
     */
    public RegistrationStatus(int idStatus, String desc) {
        this.idStatus = idStatus;
        this.desc = desc;
    }

    /**
     * Get Status ID
     * @return int
     */
    public int getIdStatus() {
        return idStatus;
    }

    /**
     * Set Status ID
     * @param idStatus {int} Status ID
     */
    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
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