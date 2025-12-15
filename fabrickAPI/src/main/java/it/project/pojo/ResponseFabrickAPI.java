package it.project.pojo;

import java.util.List;

public class ResponseFabrickAPI<T> {
	
	private String status;
    private List<String> error;
    private T payload;
    
    public ResponseFabrickAPI() {}
    
	public ResponseFabrickAPI(String status, List<String> error, T payload) {
		this.status = status;
		this.error = error;
		this.payload = payload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getError() {
		return error;
	}

	public void setError(List<String> error) {
		this.error = error;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}
    
	
    
}
