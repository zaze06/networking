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
     * This should be followed by an error json container the error and a reason.
     */
    ERROR('~');
    /**
     * A chat that is resposible for the {@link Headers} indicator
     */
    final char header;

    /**
     * Constructor of the {@link Headers}
     * @param header the cherecter that is the id of this {@link Headers} instance
     */
    Headers(char header){
        this.header = header;
    }

    /**
     * returns the {@link #header} of the {@link Headers} instance
     * @return the {@link #header} of the {@link Headers} instance
     */
    public char getHeader() {
        return header;
    }
}
