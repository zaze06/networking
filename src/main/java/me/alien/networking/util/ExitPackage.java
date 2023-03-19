package me.alien.networking.util;

import me.alien.networking.client.Client;
import me.alien.networking.server.Server;

import java.io.Serializable;

/**
 * Exit package containing information of closing/exiting the connection
 */
public class ExitPackage implements Serializable {
    /**
     * The reason for closing the connection.
     */
    String reason;

    /**
     * Creates a exit/closing package that will be sent for information to the {@link Server} or {@link Client}
     * @param reason The exiting reason
     */
    public ExitPackage(String reason) {
        this.reason = reason;
    }

    /**
     * Retries the reason for the exit.
     * @return the reason for the exit
     */
    public String getReason() {
        return reason;
    }
}
