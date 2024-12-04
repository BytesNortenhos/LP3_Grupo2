package AuxilierXML;

public class Person {
    private int id;
    private String password;

    /**
     * Constructor of Person
     * @param id {int} ID
     * @param password {String} Password
     */
    public Person(int id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Constructor of Person (without parameters)
     */
    public Person() {}

    /**
     * Get ID
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID
     * @param id {int} ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get password
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     * @param password {String} Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Override toString method
     * @return String
     */
    @Override
    public String toString() {
        return String.format("ID: %d, Password: %s", id, password);
    }
}

