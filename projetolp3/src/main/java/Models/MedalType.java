package Models;

public class MedalType {
    private int idMedalType;
    private String descMedalType;

    public MedalType(int idMedalType, String descMedalType) {
        this.idMedalType = idMedalType;
        this.descMedalType = descMedalType;
    }

    public int getId() {
        return idMedalType;
    }

    public void setId(int idMedalType) {
        this.idMedalType = idMedalType;
    }

    public String getDescMedalType() {
        return descMedalType;
    }

    public void setDescMedalType(String descMedalType) {
        this.descMedalType = descMedalType;
    }
}
