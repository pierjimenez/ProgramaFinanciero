package pe.com.stefanini.core.exceptions;

public class UtilException extends Exception{

	public UtilException(String message){
		super(message);
	}
	public UtilException(String message , Exception e){
		super(message, e);
	}
	public UtilException(Exception e){
		super(e);
	}
}
