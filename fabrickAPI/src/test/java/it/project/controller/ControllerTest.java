package it.project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import it.project.dto.MoneyTransferRequest;
import it.project.exception.FabrickAPIException;
import it.project.exception.GlobalExceptionHandler;
import it.project.pojo.CashAccount;
import it.project.pojo.Transaction;
import it.project.pojo.TransactionType;
import it.project.service.FabrickAPIService;

public class ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FabrickAPIService fabrickAPIService;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetSaldo_Success() throws Exception {
        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(BigDecimal.valueOf(1234.56));

        when(fabrickAPIService.getSaldo(14537780L)).thenReturn(cashAccount);

        mockMvc.perform(get("/lettura_saldo")
                .param("idAccount", "14537780"))
                .andExpect(status().isOk())
                .andExpect(content().string("Lettura saldo effettuata. il tuo saldo è di: 1234.56"));
    }

    @Test
    void testListaTransazioni_Success() throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setTransactionId("1001");
        tx.setAccountingDate("2019-06-15");
        tx.setValueDate("2019-06-16"); 
        tx.setAmount(BigDecimal.valueOf(150.75));
        tx.setCurrency("EUR");
        tx.setDescription("Test Transaction");
        tx.setType(new TransactionType("GBS_TRANSACTION_TYPE", "GBS_ACCOUNT_TRANSACTION_TYPE_0010"));

        transactions.add(tx);

        when(fabrickAPIService.getListaTransizioni(eq(14537780L),
                eq(LocalDate.of(2019, 1, 1)),
                eq(LocalDate.of(2019, 12, 1))))
                .thenReturn(transactions);

        mockMvc.perform(get("/lista_transizioni")
                .param("idAccount", "14537780")
                .param("dateStart", "2019-01-01")
                .param("dateEnd", "2019-12-01"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Transaction")));
    }

    @Test
    void testBonifico_ThrowsFabrickAPIException() throws Exception {
        when(fabrickAPIService.makeBonifico(eq(3L), any(MoneyTransferRequest.class)))
                .thenThrow(new FabrickAPIException("Errore API", 400, "{KO}"));

        mockMvc.perform(post("/bonifico")
                .param("idAccount", "3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Errore API"))
                .andExpect(jsonPath("$.body").value("{KO}"));
    }
}
