package it.project.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.project.service.FabrickAPIService;

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	private FabrickAPIService fabrickAPIService;
	
	@GetMapping("/lettura_saldo")
	public ResponseEntity<String> bankingAccountCash(@RequestParam Long idAccount) {
		logger.info("Sto cercando le informazioni per {}",idAccount);
		logger.info(fabrickAPIService.getSaldo(idAccount).toString());
		return ResponseEntity.ok("Lettura saldo effettuata");
	}
	
	@GetMapping("/lista_transizioni")
	public ResponseEntity<String> listTransaction(@RequestParam String idAccount, 
								@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart, 
								@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
		logger.info("Dammi la lista transizioni per questo account {} in questo intervallo di tempo {}:{}",idAccount,dateStart,dateEnd);
		//TODO
		return ResponseEntity.ok("lettura lista transazioni");
	}
	
	@PostMapping("/bonifico")
	public ResponseEntity<String> bankTranser() {
		logger.info("Devo fare un bonifico");
		//TODO
		return ResponseEntity.ok("bonifico effettuato");
	}

}
