package Models;

public class Gender {
    private int idGender;
    private String desc;

    /**
     * Constructor of Gender
     * @param idGender {int} ID
     * @param desc {String} Desc
     */
    public Gender(int idGender, String desc) {
        this.idGender = idGender;
        this.desc = desc;
    }

    /**
     * Get Gender ID
     * @return int
     */
    public int getIdGender() {
        return idGender;
    }

    /**
     * Set Gender ID
     * @param idGender {int} Gender ID
     */
    public void setIdGender(int idGender) {
        this.idGender = idGender;
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
