package com.aiken.exceptions;

public class WrongFileTypeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2555132404460877375L;

	public WrongFileTypeException(String message) {
		super(message);
	}

	public WrongFileTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}