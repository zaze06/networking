package me.alien.networking.util;

public enum Headers {
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
     * This should be followed by an object following by a {@link ObjectPacker}s packaging format
     */
    DATA('%'),
    /**
     * This specific an end of a Field declaration in a {@link ObjectPacker}s packing format
     */
    END_OF_FIELD('&'),
    START_OF_OBJECT_CLASS('`'),
    END_OF_OBJECT_CLASS('('),
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
