import examples.MessagePackage;
import me.alien.networking.server.Client;
import me.alien.networking.server.Server;

import java.io.IOException;
import java.io.Serializable;

public class testserver extends Server {

    public testserver() throws IOException {
        super();
    }

    @Override
    public void newClient(Client client) {
        try {
            client.send(new MessagePackage("test"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientMessage(Client client, Serializable message, boolean fatal, boolean exit) {

    }

    public static void main(String[] args) throws IOException {
        testserver testserver = new testserver();
        testserver.start();
    }
}
