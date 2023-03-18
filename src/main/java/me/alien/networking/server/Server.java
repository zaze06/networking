package me.alien.networking.server;

import me.alien.networking.util.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * The main class for the Server
 * @author Zacharias Zellén
 */
abstract public class Server {
    /**
     * The version of networking communication used for handshake. </rb>
     * If you have your own version or other things needed to be completed use {@link me.alien.networking.client.Client#connected()} and {@link #newClient(Client)} to confirm other versions before completion the handshake
     */
    final int VERSION = 0;
    /**
     * A {@link ServerSocket} that will be the server to connect the {@link me.alien.networking.client.Client} to
     */
    final ServerSocket server;
    /**
     * The acceptation thread for clients. This will handle the initial handshake
     */
    private final ClientAcceptThread clientAcceptThread;
    /**
     * A list of all the connected {@link Client}s
     */
    protected final ArrayList<Client> clients;
    /**
     * The port that the server is listening too
     */
    public final int port;

    /**
     * Creates a server instance with a specified port and client limit.
     * @param port defines what port for the server to listen too.
     * @param clientLimit defines the max clients that's allowed on the server.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public Server(int port, int clientLimit) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
        clientAcceptThread = new ClientAcceptThread(this);
        clients = new ArrayList<>(clientLimit);
        Logger.info(getClass(), "Server started on port: "+port);
    }

    /**
     * Creates a server instance whit only a specified port.
     * @param port defines wat port for the server to listen too;
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public Server(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
        clientAcceptThread = new ClientAcceptThread(this);
        clients = new ArrayList<>();
        Logger.info(getClass(), "Server started on port: "+port);
    }

    /**
     * Creates a server instance without any specifications
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public Server() throws IOException{
        this.port = 6550;
        server = new ServerSocket(port);
        clientAcceptThread = new ClientAcceptThread(this);
        clients = new ArrayList<>();
        Logger.info(getClass(), "Server started on port: "+port);
    }

    /**
     * Starts the clientAcceptThread to handle new clients
     */
    public void start(){
        clientAcceptThread.start();
        Logger.info(getClass(), "Server is now listening to port: "+port);
    }

    /**
     * newClient gets called when a new client have completed a successful handshake
     * @param client the client that connected to the server
     */
    public abstract void newClient(Client client);

    /**
     * clientMessage gets called when the server receives a message from a client.
     * @param client The client that send the message.
     * @param message The message that was received.
     * @param fatal if this is a fatal message, meaning that the client experienced a fatal issue and needs to have the connection closed.
     *              If fatal is true then the connection will be closed afterwords.
     * @param exit if this is an exit message, meaning that the client will exit after and the connection will be closed, this is the last message.
     *             This message will contain a reason of way the connection should be closed.
     */
    public abstract void clientMessage(Client client, Serializable message, boolean fatal, boolean exit);

    /**
     * Removes a {@link Client} from the servers {@link Server#clients} list.
     * @param client the {@link Client} that should be removed.
     */
    public void close(Client client) {
        clients.remove(client);
    }
}
