package pe.com.stefanini.core.exceptions;

public class BOException extends Exception {
	public BOException(String message){
		super(message);
	}
	public BOException(String message , Exception e){
		super(message, e);
	}
	public BOException(Exception e){
		super(e);
	}
}
