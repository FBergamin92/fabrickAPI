package it.project.service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import it.project.pojo.CashAccount;
import it.project.pojo.ResponceCashAccountAPI;
import it.project.dto.Account;
import it.project.dto.Creditor;
import it.project.dto.MoneyTransferRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class FabrickAPIService {
	
	private Logger logger = LoggerFactory.getLogger(FabrickAPIService.class);
	
	private RestClient restClient = RestClient.builder()
											  .baseUrl("https://sandbox.platfr.io")
											  .build();
	
	
	public void getSaldo(Long accountId) {
		
		ResponseEntity<String> result = this.restClient.get()
					  .uri("/api/gbs/banking/v4.0/accounts/{accountId}/balance", accountId)
					  .header("X-Time-Zone","Europe/Rome")
					  .header("Content-Type", "application/json")
					  .header("Auth-Schema", "S2S")
					  .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
					  .retrieve()
					  .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
							logger.info("abbiamo ottenuto il codice {}",response.getStatusCode()); 
						})
					  .toEntity(String.class);
		}
	
	public void getListaTransizioni(Long accountId) {
		
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("fromAccountingDate", "2019-01-01");
		queryParams.put("toAccountingDate", "2019-12-01");

		
		ResponseEntity<String> result = this.restClient.get()
					  .uri(uriBuilder -> uriBuilder
							  .path("/api/gbs/banking/v4.0/accounts/{accountId}/transactions")
							  .queryParam("fromAccountingDate", queryParams.get("fromAccountingDate"))
							  .queryParam("toAccountingDate", queryParams.get("toAccountingDate"))
							  .build(accountId))
					  .header("X-Time-Zone","Europe/Rome")
					  .header("Content-Type", "application/json")
					  .header("Auth-Schema", "S2S")
					  .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
					  .retrieve()
					  .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
							logger.info("abbiamo ottenuto il codice {}",response.getStatusCode()); 
						})
					  .toEntity(String.class);
		
		logger.info(result.getBody());
	}
	
	public void makeBonifico(Long accountId, MoneyTransferRequest moneyTransfer) {
		
		ResponseEntity<String> result = this.restClient.post()
				  .uri("/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers", accountId)
				  .header("X-Time-Zone","Europe/Rome")
				  .header("Content-Type", "application/json")
				  .header("Auth-Schema", "S2S")
				  .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
				  .body(moneyTransfer)
				  .retrieve()
				  .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
						logger.info("abbiamo ottenuto il codice {}",response.getStatusCode()); 
					})
				  .toEntity(String.class);
		
	}

}
