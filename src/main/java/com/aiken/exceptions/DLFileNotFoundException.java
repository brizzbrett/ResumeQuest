package com.aiken.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DLFileNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6241420174429880934L;

	public DLFileNotFoundException(String message) {
        super(message);
    }

    public DLFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
