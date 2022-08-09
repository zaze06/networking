package me.alien.networking.util;

public enum Headers {
    /**
     * @deprecated only use this if you can't find another Header working.
     */
    @Deprecated()
    EMPTY((char) 0),
    /**
     * A starter header for first contact to the client.
     */
    START(' '),
    /**
     * Fatal issue have a cured and the socket need to be closed this should be followed by a json containing the reason.
     */
    FATAL('!'),
    /**
     * This should be followed by the version integer as a hexadecimal number.
     */
    VERSION('"'),
    /**
     * Indication of a successful action between server and client.
     */
    SUCCESS('#'),
    /**
     * Sending of a string message.
     */
    MESSAGE_RAW('$'),
    /**
     * This should be followed by an object following by a char format
     */
    DATA('%'),
    /**
     * This should be followed by an error json container the error and a reason.
     */
    ERROR('~');
    final char header;
    Headers(char header){
        this.header = header;
    }

    public char getHeader() {
        return header;
    }
}
