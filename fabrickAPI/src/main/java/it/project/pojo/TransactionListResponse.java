package it.project.pojo;

import java.util.List;

public class TransactionListResponse {
	
    private List<Transaction> list;
    
    public TransactionListResponse() {}

	public TransactionListResponse(List<Transaction> list) {
		this.list = list;
	}

	public List<Transaction> getList() {
		return list;
	}

	public void setList(List<Transaction> list) {
		this.list = list;
	}
}