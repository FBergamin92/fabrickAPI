package it.project.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import it.project.dto.MoneyTransferRequest;
import it.project.exception.FabrickAPIException;

@Service
public class FabrickAPIService {

    private final Logger logger = LoggerFactory.getLogger(FabrickAPIService.class);

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://sandbox.platfr.io")
            .build();

    public String getSaldo(Long accountId) {
        try {
            ResponseEntity<String> result = restClient.get()
                    .uri("/api/gbs/banking/v4.0/accounts/{accountId}/balance", accountId)
                    .header("X-Time-Zone", "Europe/Rome")
                    .header("Content-Type", "application/json")
                    .header("Auth-Schema", "S2S")
                    .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                    .retrieve()
                    .toEntity(String.class);

            return result.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Errore API getSaldo - status: {}, body: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw new FabrickAPIException("Errore API", ex.getStatusCode().value(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            logger.error("Errore generico getSaldo", ex);
            throw new FabrickAPIException("Errore interno", 500, ex.getMessage());
        }
    }

    public String getListaTransizioni(Long accountId) {
        try {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("fromAccountingDate", "2019-01-01");
            queryParams.put("toAccountingDate", "2019-12-01");

            ResponseEntity<String> result = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/gbs/banking/v4.0/accounts/{accountId}/transactions")
                            .queryParam("fromAccountingDate", queryParams.get("fromAccountingDate"))
                            .queryParam("toAccountingDate", queryParams.get("toAccountingDate"))
                            .build(accountId))
                    .header("X-Time-Zone", "Europe/Rome")
                    .header("Content-Type", "application/json")
                    .header("Auth-Schema", "S2S")
                    .header("Api-Key", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP")
                    .retrieve()
                    .toEntity(String.class);

            return result.getBody();

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
