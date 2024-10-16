package Models;

public class Event {

    private int year;
    private String country;
    private String logo;

    public Event(int year, String country, String logo){
        this.year = year;
        this.country = country;
        this.logo = logo;
    }

    public int getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getLogo() {
        return logo;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
