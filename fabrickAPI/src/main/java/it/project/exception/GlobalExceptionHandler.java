package it.project.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(FabrickAPIException.class)
	public ResponseEntity<Object> handleApiException(FabrickAPIException ex) {
	    // Log completo
	    logger.error("FabrickAPIException - status: {}, body: {}", ex.getStatusCode(), ex.getResponseBody());

	    // Restituisci un JSON strutturato
	    Map<String, Object> errorBody = new HashMap<>();
	    errorBody.put("status", ex.getStatusCode());
	    errorBody.put("message", ex.getMessage());
	    errorBody.put("body", ex.getResponseBody());

	    return ResponseEntity.status(ex.getStatusCode()).body(errorBody);
	}

}
