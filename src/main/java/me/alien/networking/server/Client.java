package me.alien.networking.server;

import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import me.alien.networking.util.packages.FatalPackage;
import me.alien.networking.util.packages.NetworkPackage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final Server server;
    private Client client;
    private final MessageReceiveThread messageReciveThread;

    private ObjectOutputStream out;
    public Client(Socket client, Server server) {
        this.socket = client;
        this.server = server;
        messageReciveThread = new MessageReceiveThread();
        messageReciveThread.start();
        this.client = this;
        try {
            this.out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends NetworkPackage> void send(T message) throws IOException {
        out.writeObject(message);
    }

    private class MessageReceiveThread extends Thread {
        @Override
        public void run() {
            try{
                while(true){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    try{
                        NetworkPackage message = (NetworkPackage) in.readObject();
                        boolean fatal = false;
                        if(message instanceof FatalPackage){
                            fatal = true;
                        }
                        server.clientMessage(client, message, fatal);
                    }catch (IOException ignored){
                        if(socket.isClosed()){
                            Logger.warn(getClass(), "IOException accused");
                            Logger.exception(getClass(), ignored.getStackTrace());
                            server.clientMessage(client, new FatalPackage("Connected closed, assuming lost of connection to client", "connection lost"), true);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (IOException ignored){
                Logger.warn(getClass(), "IOException accused");
                Logger.exception(getClass(), ignored.getStackTrace());
                server.clientMessage(client, new FatalPackage("Failed to create a ObjectOutputStream instance of the sockets InputStream instance", "Socket not opened?"), true);
            }
        }
    }
}
