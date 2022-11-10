import examples.MessagePackage;
import me.alien.networking.server.Client;
import me.alien.networking.server.Server;
import me.alien.networking.util.Logger;

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
        if(message instanceof MessagePackage msg) {
            Logger.info(getClass(), msg.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        testserver testserver = new testserver();
        testserver.start();
    }
}
