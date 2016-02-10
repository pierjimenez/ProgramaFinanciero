package pe.com.stefanini.core.exceptions;

public class WSException extends Exception {

	public WSException(String message){
		super(message);
	}
	public WSException(String message , Exception e){
		super(message, e);
	}
	public WSException(Exception e){
		super(e);
	}
}
