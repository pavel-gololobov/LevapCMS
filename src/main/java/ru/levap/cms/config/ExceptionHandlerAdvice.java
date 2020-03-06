package ru.levap.cms.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import ru.levap.cms.dto.web.ErrorDTO;
import ru.levap.cms.exceptions.CommonException;
import ru.levap.cms.exceptions.NotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorDTO> handleCommonException(CommonException e) {
    	log.error("EXCEPTION", e);
    	ErrorDTO error = ErrorDTO.builder() //
    			.status(e.getHttpStatus().toString()) //
    			.message(e.getMessage()).build();
    	return ResponseEntity.status(e.getHttpStatus()).body(error);
    }
	
	@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException e) {
    	log.error("EXCEPTION", e);
    	ErrorDTO error = ErrorDTO.builder() //
    			.status(HttpStatus.NOT_FOUND.toString()) //
    			.message(e.getMessage()).build();
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
} 
