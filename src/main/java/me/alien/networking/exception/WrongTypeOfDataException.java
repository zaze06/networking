package me.alien.networking.exception;

public class WrongTypeOfDataException extends RuntimeException{
    public WrongTypeOfDataException(String message) {
        super(message);
    }
}
