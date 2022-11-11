package examples;

import me.alien.networking.server.Client;
import me.alien.networking.util.Logger;

import java.io.IOException;
import java.io.Serializable;

/**
 * A standard example server setup
 */
public class Server extends me.alien.networking.server.Server {
    /**
     * Creates a default {@link me.alien.networking.server.Server} instance from {@link me.alien.networking.server.Server#Server()} constructor
     * @throws IOException if the {@link me.alien.networking.server.Server#Server()} constructor fails.
     */
    public Server() throws IOException {
        super();
        start();
    }

    @Override
    public void newClient(Client client) {
        try {
            client.send(new MessagePackage("Message"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientMessage(Client client, Serializable message, boolean fatal, boolean exit) {
        if(message instanceof MessagePackage){
            MessagePackage msg = (MessagePackage) message;
            Logger.info("example server", msg.getMessage());
        }
    }
}
