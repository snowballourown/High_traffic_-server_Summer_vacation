package Challenge_summer.bigTraffic.dto.seat;




public class SeatCreateRequest {
    String name;

    public SeatCreateRequest(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
