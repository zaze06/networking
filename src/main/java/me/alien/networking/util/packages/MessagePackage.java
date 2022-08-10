package me.alien.networking.util.packages;

/**
 * A {@link NetworkPackage} that is meant to send a string message
 * @author Zacharias Zellén
 */
public class MessagePackage extends NetworkPackage{
    /**
     * A String that contains the message to send
     */
    public String message;

    /**
     * Constructor to make a {@link MessagePackage} for sending
     * @param message the string message to be contained in the {@link MessagePackage}
     */
    public MessagePackage(String message) {
        this.message = message;
    }

    /**
     * Default toString() method inherited but modified from {@link Object#toString()}
     * @return a string that's describing the {@link MessagePackage} content
     */
    @Override
    public String toString() {
        return "MessagePackage{" +
                "message='" + message + '\'' +
                '}';
    }
}
