package Challenge_summer.bigTraffic.dto;



public class EventCreateRequest {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public EventCreateRequest(String name) {
        this.name = name;
    }
}
