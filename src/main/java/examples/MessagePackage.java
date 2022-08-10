package examples;

import java.io.Serializable;

/**
 * An example class that uses the {@link Serializable} interface <br>
 * In this class we have a {@link #message} that is of the type {@link String}
 * @author Zacharias Zellén
 */
public class MessagePackage implements Serializable {
    /**
     * A message contained in a {@link String} format
     */
    private String message;

    /**
     * A base constructor to give {@link #message} a value
     * @param message A {@link String} message
     */
    public MessagePackage(String message) {
        this.message = message;
    }

    /**
     * inherited from {@link Object#toString()}. but hear its modified to give the content of {@link #message}
     * @return an information string about this class
     */
    @Override
    public String toString() {
        return "MessagePackage{" +
                "message='" + message + '\'' +
                '}';
    }

    /**
     * gives the {@link #message} contained in {@link MessagePackage}
     * @return {@link #message}
     */
    public String getMessage() {
        return message;
    }
}
