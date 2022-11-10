import examples.MessagePackage;
import me.alien.networking.client.Client;
import me.alien.networking.util.Logger;

import java.io.IOException;
import java.io.Serializable;

public class testClient extends Client {
    public testClient() throws IOException {
        super("localhost");
    }

    @Override
    public void messageReceive(Serializable message, boolean fatal, boolean exit) {
        if(message instanceof MessagePackage msg) {
            Logger.info(getClass(), msg.getMessage());
        }
    }

    @Override
    public void connected() {
        try {
            send(new MessagePackage("Hello"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new testClient();
    }
}
