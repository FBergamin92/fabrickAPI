package it.project.pojo;

import java.util.List;

public class ResponceCashAccountAPI {
	
	private String status;
    private List<String> error;
    private CashAccount payload;
    
    public ResponceCashAccountAPI() {}
    
	public ResponceCashAccountAPI(String status, List<String> error, CashAccount payload) {
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

	public CashAccount getPayload() {
		return payload;
	}

	public void setPayload(CashAccount payload) {
		this.payload = payload;
	}
    
	
    
}
