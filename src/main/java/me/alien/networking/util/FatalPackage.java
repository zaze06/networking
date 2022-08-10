package me.alien.networking.util;

import java.io.Serializable;

/**
 * A {@link Serializable} that contains a fatal information
 * @author Zacharias Zellén
 */
public class FatalPackage implements Serializable {
    /**
     * The reason for the fatal issue
     */
    private final String reason;
    /**
     * the error that created the fatal issue
     */
    private final String error;

    /**
     * a standard constructor to store the reason and error as {@link String}s in the Fatal error class
     * @param reason the reason or exception that cased the fatal issue
     * @param error the description of way this fatal issue happened
     */
    public FatalPackage(String reason, String error) {
        this.reason = reason;
        this.error = error;
    }

    /**
     * Default toString() method inherited but modified from {@link Object#toString()}
     * @return a string that's describing the {@link FatalPackage} content
     */
    @Override
    public String toString() {
        return "FatalPackage{" +
                "reason='" + reason + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    /**
     * gives the error that {@link FatalPackage} contains
     * @return {@link #error}
     */
    public String getError() {
        return error;
    }
    /**
     * gives the reason that {@link FatalPackage} contains
     * @return {@link #reason}
     */
    public String getReason() {
        return reason;
    }
}