package me.alien.networking.util.packages;

public class FatalPackage extends NetworkPackage{
    public final String reason;
    public final String error;

    public FatalPackage(String ression, String error) {
        this.reason = ression;
        this.error = error;
    }

    @Override
    public String toString() {
        return "FatalPackage{" +
                "reason='" + reason + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
