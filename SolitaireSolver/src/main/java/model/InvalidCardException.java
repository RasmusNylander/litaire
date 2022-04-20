package model;

public class InvalidCardException extends IllegalArgumentException {
	public InvalidCardException(String message) {
		super(message);
	}
}
