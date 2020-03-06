package ru.levap.cms.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
	public CommonException(HttpStatus httpStatus, String message) {
		super(message);
		if(httpStatus != null) {
			this.httpStatus = httpStatus;
		}
	}
}
