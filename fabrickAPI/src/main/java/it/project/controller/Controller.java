package it.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.project.dto.MoneyTransferRequest;
import it.project.pojo.CashAccount;
import it.project.pojo.Transaction;
import it.project.service.FabrickAPIService;

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	private FabrickAPIService fabrickAPIService;
	
	@GetMapping("/lettura_saldo")
	public ResponseEntity<String> bankingAccountCash(@RequestParam Long idAccount) {
		logger.info("Sto cercando le informazioni per {}",idAccount);
		CashAccount result = fabrickAPIService.getSaldo(idAccount);
		logger.info(result.toString());
		return ResponseEntity.ok("Lettura saldo effettuata. il tuo saldo è di: " + result.getAvailableBalance());
	}
	
	@GetMapping("/lista_transizioni")
	public ResponseEntity<String> listTransaction(@RequestParam Long idAccount,
								@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart, 
							    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd
			) {
		List<Transaction> transactions = fabrickAPIService.getListaTransizioni(idAccount, dateStart, dateEnd);
		transactions.forEach(transaction->{
			logger.info(transaction.toString());
		});
		return ResponseEntity.ok("lettura lista transazioni :" + transactions);
	}
	
	@PostMapping("/bonifico")
	public ResponseEntity<String> bankTranser(@RequestParam Long idAccount,
			 								  @RequestBody MoneyTransferRequest moneyTransfer) {
		logger.info("Devo fare un bonifico");
		fabrickAPIService.makeBonifico(idAccount, moneyTransfer);
		return ResponseEntity.ok("bonifico effettuato");
	}

}
