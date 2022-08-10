package me.alien.networking.util.packages;

/**
 * A {@link NetworkPackage} that contains a fatal information
 * @author Zacharias Zellén
 */
public class FatalPackage extends NetworkPackage{
    /**
     * The reason for the fatal issue
     */
    public final String reason;
    /**
     * the error that created the fatal issue
     */
    public final String error;

    /**
     * a standard constructor to store the reason and error as {@link String}s in the Fatal error class
     * @param reason
     * @param error
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
}
