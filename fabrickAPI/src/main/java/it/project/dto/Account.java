package it.project.dto;

public class Account {
	
	private String accountCode;
	
	public Account() {}

	public Account(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	@Override
	public String toString() {
		return "Account [accountCode=" + accountCode + "]";
	}
}
