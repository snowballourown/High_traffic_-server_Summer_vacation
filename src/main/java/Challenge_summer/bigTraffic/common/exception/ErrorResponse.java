package challenge_summer.bigtraffic.common.exception;


public class ErrorResponse {

    public String getMessage() {
        return message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    private String message;

}
