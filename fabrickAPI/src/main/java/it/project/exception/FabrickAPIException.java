package it.project.exception;

public class FabrickAPIException extends RuntimeException {
	
	private int statusCode;
    private String responseBody;

    public FabrickAPIException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
