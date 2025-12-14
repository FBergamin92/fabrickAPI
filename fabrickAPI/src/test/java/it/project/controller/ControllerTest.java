package it.project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    void testGetSaldo_ThrowsFabrickAPIException() throws Exception {
        when(fabrickAPIService.getSaldo(1L))
                .thenThrow(new FabrickAPIException("Errore API", 400, "{KO}"));

        mockMvc.perform(get("/lettura_saldo")
                .param("idAccount", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Errore API"))
                .andExpect(jsonPath("$.body").value("{KO}"));
    }

    @Test
    void testListaTransazioni_ThrowsFabrickAPIException() throws Exception {
        when(fabrickAPIService.getListaTransizioni(2L))
                .thenThrow(new FabrickAPIException("Errore API", 500, "{KO}"));

        mockMvc.perform(get("/lista_transizioni")
                .param("idAccount", "2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Errore API"))
                .andExpect(jsonPath("$.body").value("{KO}"));
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
