package me.alien.networking.server;

import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import me.alien.networking.util.packages.FatalPackage;
import me.alien.networking.util.packages.NetworkPackage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * The {@link Server}s client object to offload a bit from the server
 * @author Zacharias Zellén
 */
public class Client {
    /**
     * The {@link Socket} that is connected to {@link me.alien.networking.client.Client}s socket
     */
    private final Socket socket;
    /**
     * A {@link Server} instance that should handle messages
     */
    private final Server server;
    /**
     * {@link MessageReceiveThread} is a {@link Thread} that will send the recived messages to {@link Server#clientMessage(Client, NetworkPackage, boolean)}
     */
    private final MessageReceiveThread messageReciveThread;
    /**
     * The {@link OutputStream} that will send message to the {@link me.alien.networking.client.Client}
     */
    private ObjectOutputStream out;

    /**
     * The constructor of {@link Client} that will be a connection for the {@link me.alien.networking.client.Client} to the {@link Server}
     * @param client A {@link Socket} that is the connection to {@link me.alien.networking.client.Client}
     * @param server A {@link Server} instance that will handle the messages received
     */
    public Client(Socket client, Server server) {
        this.socket = client;
        this.server = server;
        messageReciveThread = new MessageReceiveThread();
        messageReciveThread.start();
        try {
            this.out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This will send the message to {@link me.alien.networking.client.Client} that it's connected too.
     * @param message An object that extends the {@link NetworkPackage} to be sent
     * @throws IOException If the {@link Socket#getOutputStream()}s {@link OutputStream} throws a IOException
     */
    public <T extends NetworkPackage> void send(T message) throws IOException {
        out.writeObject(message);
    }

    /**
     * {@link MessageReceiveThread} is the {@link Thread} that is responsible for sending the received message to {@link Server#clientMessage(Client, NetworkPackage, boolean)}
     * @author Zacharias Zellén
     */
    private class MessageReceiveThread extends Thread {
        /**
         * A method that is inherited from {@link Thread#run()} that will be run in a different frame to not stall the running of the main frame
         */
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
                        server.clientMessage(Client.this, message, fatal);
                    }catch (IOException ignored){
                        if(socket.isClosed()){
                            Logger.warn(getClass(), "IOException accused");
                            Logger.exception(getClass(), ignored.getStackTrace());
                            server.clientMessage(Client.this, new FatalPackage("Connected closed, assuming lost of connection to client", "connection lost"), true);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (IOException ignored){
                Logger.warn(getClass(), "IOException accused");
                Logger.exception(getClass(), ignored.getStackTrace());
                server.clientMessage(Client.this, new FatalPackage("Failed to create a ObjectOutputStream instance of the sockets InputStream instance", "Socket not opened?"), true);
            }
        }
    }
}
