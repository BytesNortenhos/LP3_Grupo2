package Models;

public class Local {
    private int idLocal;
    private String name;
    private String type;
    private String address;
    private String city;
    private int capacity;
    private int constructionYear;
    private int event;
    private Event Event;

    /**
     * Constructor of Local
     * @param idLocal {int} Local ID
     * @param name {String} Name
     * @param address {String} Address
     * @param city {String} City
     * @param capacity {int} Capacity
     * @param constructionYear {int} Year of construction
     */
    public Local(int idLocal, String name, String type, String address, String city, int capacity, int constructionYear) {
        this.idLocal = idLocal;
        this.name = name;
        this.type = type;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.constructionYear = constructionYear;
    }

    /**
     * Constructor of Local
     * @param idLocal {int} Local ID
     * @param name {String} Name
     * @param type {String} Type
     * @param address {String} Address
     * @param city {String} City
     * @param capacity {int} Capacity
     * @param constructionYear {int} Year of construction
     * @param event {int} Event
     */
    public Local(int idLocal, String name, String type, String address, String city, int capacity, int constructionYear, int event) {
        this.idLocal = idLocal;
        this.name = name;
        this.type = type;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.constructionYear = constructionYear;
        this.event = event;
    }

    /**
     * Constructor of Local
     * @param idLocal {int} Local ID
     * @param name {String} Name of the local
     * @param type {String} Type of the local (e.g., "Interior" or "Exterior")
     * @param address {String} Address of the local
     * @param city {String} City where the local is located
     * @param capacity {int} Maximum capacity of the local
     * @param constructionYear {int} Year when the local was constructed
     * @param Event {Event} Event associated with the local
     */
    public Local(int idLocal, String name, String type, String address, String city, int capacity, int constructionYear, Event Event) {
        this.idLocal = idLocal;
        this.name = name;
        this.type = type;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.constructionYear = constructionYear;
        this.Event = Event;
    }


    /**
     * Get Local ID
     * @return int
     */
    public int getIdLocal() {
        return idLocal;
    }

    /**
     * Set Local ID
     * @param idLocal {int} ID
     */
    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
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
     * Get type
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Set type
     * @param type {String} Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get address
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     * @param address {String} Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get city
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city
     * @param city {String} City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get capacity
     * @return int
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set capacity
     * @param capacity {int} Capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get Year of construction
     * @return int
     */
    public int getConstructionYear() {
        return constructionYear;
    }

    /**
     * Set year of construction
     * @param constructionYear {int} Year of construction
     */
    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }

    /**
     * Get event year
     * @return int
     */
    public int getEvent() {
        return event;
    }

    /**
     * Set event year
     * @param event {int} Event
     */
    public void setEvent(int event) {
        this.event = event;
    }
    /**
     * Get the Event associated with this Local
     * @return Event The Event object associated with this Local
     */
    public Event getEventObject() {
        return Event;
    }

    /**
     * Set the Event associated with this Local
     * @param Event {Event} The Event object to associate with this Local
     */
    public void setEventObject(Event Event) {
        this.Event = Event;
    }

}