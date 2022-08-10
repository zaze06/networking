package me.alien.networking.server;

import me.alien.networking.util.Logger;
import me.alien.networking.util.packages.NetworkPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

abstract public class Server {
    final int VERSION = 0;
    final ServerSocket server;
    private final ClientAcceptThread clientAcceptThread;

    final ArrayList<Client> clients;

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
     * @param fatal if this was a fatal message, meaning that the client experienced a fatal issue and needs to have the connection closed.
     *              If fatal is true then the connection will be closed afterwords.
     */
    public abstract void clientMessage(Client client, NetworkPackage message, boolean fatal);
}
