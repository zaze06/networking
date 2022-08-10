package me.alien.networking.util.packages;

public class MessagePackage extends NetworkPackage{
    public String message;

    public MessagePackage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessagePackage{" +
                "message='" + message + '\'' +
                '}';
    }
}
