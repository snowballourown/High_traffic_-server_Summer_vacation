package Challenge_summer.bigTraffic.dto.event;



public class EventRequest {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public EventRequest(String name) {
        this.name = name;
    }


}
