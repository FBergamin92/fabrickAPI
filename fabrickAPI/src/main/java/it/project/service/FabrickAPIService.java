package it.project.service;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.project.dto.MoneyTransferRequest;
import it.project.exception.FabrickAPIException;
import it.project.pojo.CashAccount;
import it.project.pojo.ResponseFabrickAPI;
import it.project.pojo.Transaction;
import it.project.pojo.TransactionListResponse;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class FabrickAPIService {

    private final Logger logger = LoggerFactory.getLogger(FabrickAPIService.class);
    
    
    private RestClient restClient;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public FabrickAPIService (RestClient restClient) {
    	this.restClient = restClient;
    }

    public CashAccount getSaldo(Long accountId) {
        try {
            ResponseEntity<String> result = restClient.get()
                    .uri("/api/gbs/banking/v4.0/accounts/{accountId}/balance", accountId)
                    .header("X-Time-Zone", "Europe/Rome")
                    .header("Content-Type", "application/json")
                    .header("Auth-Schema", "S2S")
                    .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                    .retrieve()
                    .toEntity(String.class);
            
            ResponseFabrickAPI<CashAccount> response = objectMapper.readValue(result.getBody(),new TypeReference<ResponseFabrickAPI<CashAccount>>() {});
            return response.getPayload();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Errore API getSaldo - status: {}, body: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw new FabrickAPIException("Errore API", ex.getStatusCode().value(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            logger.error("Errore generico getSaldo", ex);
            throw new FabrickAPIException("Errore interno", 500, ex.getMessage());
        }
    }

    public List<Transaction> getListaTransizioni(Long accountId, LocalDate dateStart, LocalDate dateEnd) {
        try {
        	
            ResponseEntity<String> result = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/gbs/banking/v4.0/accounts/{accountId}/transactions")
                            .queryParam("fromAccountingDate", dateStart)
                            .queryParam("toAccountingDate", dateEnd)
                            .build(accountId))
                    .header("X-Time-Zone", "Europe/Rome")
                    .header("Content-Type", "application/json")
                    .header("Auth-Schema", "S2S")
                    .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                    .retrieve()
                    .toEntity(String.class);
            logger.info(result.getBody());
            ResponseFabrickAPI<TransactionListResponse> response = objectMapper.readValue(result.getBody(),new TypeReference<ResponseFabrickAPI<TransactionListResponse>>() {});
            return response.getPayload().getList();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Errore API getListaTransizioni - status: {}, body: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw new FabrickAPIException("Errore API", ex.getStatusCode().value(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            logger.error("Errore generico getListaTransizioni", ex);
            throw new FabrickAPIException("Errore interno", 500, ex.getMessage());
        }
    }

    public String makeBonifico(Long accountId, MoneyTransferRequest moneyTransfer) {
        try {
            ResponseEntity<String> result = restClient.post()
                    .uri("/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers", accountId)
                    .header("X-Time-Zone", "Europe/Rome")
                    .header("Content-Type", "application/json")
                    .header("Auth-Schema", "S2S")
                    .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                    .body(moneyTransfer)
                    .retrieve()
                    .toEntity(String.class);

            return result.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Errore API makeBonifico - status: {}, body: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw new FabrickAPIException("Errore API", ex.getStatusCode().value(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            logger.error("Errore generico makeBonifico", ex);
            throw new FabrickAPIException("Errore interno", 500, ex.getMessage());
        }
    }
}
