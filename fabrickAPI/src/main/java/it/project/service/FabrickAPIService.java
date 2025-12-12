package it.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import it.project.pojo.CashAccount;

@Service
public class FabrickAPIService {
	
	private RestClient restClient = RestClient.builder()
											  .baseUrl("https://sandbox.platfr.io")
											  .build();
	
	
	public CashAccount getSaldo(Long accountId) {
		try {
			return this.restClient.get()
					  .uri("/api/gbs/banking/v4.0/accounts/{accountId}/balance", accountId)
					  .header("X-Time-Zone","Europe/Rome")
					  .header("Content-Type", "application/json")
					  .header("Auth-Schema", "S2S")
					  .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
					  .retrieve()
					  .body(CashAccount.class);
		} catch (RestClientResponseException e) {
            System.out.println("Errore nella chiamata API ❌");
            System.out.println("Status code: " + e.getStatusCode());
            System.out.println("Messaggio: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("Errore generico nella chiamata API ❌");
            e.printStackTrace();
        }
		return null;
		
	}

}
