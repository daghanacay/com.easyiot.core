package com.easyiot.base.api.exception;

/**
 * Base framework exception. It extends runtime exception to avoid cluttering of
 * unnecessary exception handling in the code. All framework exceptions should
 * extend this exception.
 * 
 * @author daghan
 *
 */
public class FrameworkException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public FrameworkException(){
		super();
	}
	
	public FrameworkException(String message){
		super(message);
	}
}
