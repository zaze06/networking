package me.alien.networking.client;

import me.alien.networking.server.Server;
import me.alien.networking.util.ExitPackage;
import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import me.alien.networking.util.FatalPackage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * {@link Client} is the main client class for the networking and will connect to a {@link Server}
 * @author Zacharias Zellén
 */
abstract public class Client {
    /**
     * The version of networking communication used for handshake. <br>
     * If you have your own version or other things needed to be completed use {@link #connected()} and {@link Server#newClient} to confirm other versions before completion the handshake
     */
    public final int VERSION = 0;
    /**
     * The {@link Socket} that is connected to the {@link Server}
     */
    final Socket socket;

    /**
     * The {@link Thread} that make sure that the received messages get handed in {@link #messageReceive(Serializable, boolean, boolean)}
     */
    private final MessageReceiveThread messageReceiveThread;

    /**
     * The {@link OutputStream} that will send message to the {@link me.alien.networking.client.Client}
     */
    private ObjectOutputStream out;

    /**
     * Base constructor of the {@link Client} class.<br> When this is called it will connect to the {@link Server} through the ip and port.
     * @param ip IP to the {@link Server}
     * @param port Port to the {@link Server}
     * @throws IOException If the {@link Socket} failed to be created or unable to create a {@link PrintStream} or {@link BufferedReader}
     */
    public Client(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.messageReceiveThread = new MessageReceiveThread();

        PrintStream out = new PrintStream(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String data = in.readLine();

        if(!data.startsWith(Headers.START.getHeader()+"")){
            Logger.warn(getClass(), "Invalid packet");
            JSONObject error = new JSONObject();
            error.put("error", "invalid packet");
            error.put("expected", "starter header");
            out.println(Headers.FATAL.getHeader()+error.toString());
            in.readLine();
            socket.close();
        }

        out.println(Headers.VERSION.getHeader()+Integer.toHexString(VERSION));

        data = in.readLine();

        if(data.startsWith(Headers.FATAL.getHeader()+"")){
            JSONObject jsonObject = new JSONObject(data.substring(1));
            Logger.warn(getClass(), "Fatal issue. \n error: "+jsonObject.getString("error")+". expected: "+jsonObject.getString("expected"));
            out.println(Headers.SUCCESS.getHeader());
            socket.close();
            System.exit(1);
        } else if (!data.startsWith(Headers.SUCCESS.getHeader() + "")) {
            Logger.warn(getClass(), "Invalid packet");
            JSONObject error = new JSONObject();
            error.put("error", "invalid packet");
            error.put("expected", "SUCCESS header");
            out.println(Headers.FATAL.getHeader()+error.toString());
            in.readLine();
            socket.close();
        }

        this.out = new ObjectOutputStream(out);
        messageReceiveThread.start();

        connected();
    }

    /**
     * Base constructor of the {@link Client} class.<br> When this is called it will connect to the {@link Server} through the ip and default port. <br>
     * this will call {@link #Client(String ip, int port)} with the default port 6550
     * @param ip IP to the {@link Server}
     * @throws IOException If the {@link Socket} failed to be created or unable to create a {@link PrintStream} or {@link BufferedReader}
     */
    public Client(String ip) throws IOException {
        this(ip, 6550);
    }

    /**
     * {@link #messageReceive(Serializable, boolean, boolean)} gets called when {@link #messageReceiveThread} receives a new message
     * @param message A class that needs to implument {@link Serializable} to contain some sort of data. see examples package
     * @param fatal If this is {@code true} then the connection will be closed directly after this method is complete. If its {@code true} then the message will be a {@link FatalPackage} and it will contain a {@link FatalPackage#reason} and {@link FatalPackage#error} that will describe the issue
     * @param exit If this is true then the server is requesting this connection to be closed. This is followed by a region but no mather what the socket will be closed, reconnection can be done.
     */
    public abstract void messageReceive(Serializable message, boolean fatal, boolean exit);

    /**
     * {@link #connected()} will be called when it's done with the initial handshake hear its possible to do some second hand handshake to confirm more information
     */
    public abstract void connected();

    /**
     * This will send the message to {@link me.alien.networking.server.Server} that it's connected too.
     * @param message An object that implements the {@link Serializable} to be sent
     * @throws IOException If the {@link Socket#getOutputStream()}'s {@link OutputStream} throws a IOException
     */
    public void send(Serializable message) throws IOException {
        out.writeObject(message);
    }

    /**
     * Sends an exit package to close the connection to the server
     * @param reason the reason way the connection was ended.
     * @throws IOException If the {@link Socket#getOutputStream()}s {@link OutputStream} throws a IOException
     */
    public void end(String reason) throws IOException {
        out.writeObject(new ExitPackage(reason));
    }

    /**
     * The {@link MessageReceiveThread} is a {@link Thread} that will make sure all receive packages will be sent to {@link #messageReceive(Serializable, boolean, boolean)}
     * @author Zacharias Zellén
     */
    private class MessageReceiveThread extends Thread {

        /**
         * A method inherited from {@link Thread#run()} that will be run in a different thread to not stale up the main thread
         */
        @Override
        public void run() {
            try{
                while(true){
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    try{
                        Serializable message = (Serializable) in.readObject();
                        boolean fatal = false;
                        boolean exit = false;
                        if(message instanceof FatalPackage){
                            fatal = true;
                        }
                        if(message instanceof ExitPackage) {
                            exit = true;
                        }
                        messageReceive(message, fatal, exit);
                    }catch (IOException ioException){
                        if(socket.isClosed()){
                            Logger.warn(getClass(), "IOException accused");
                            Logger.exception(getClass(), ioException.getStackTrace());
                            messageReceive(new FatalPackage("Connected closed, assuming lost of connection to server", "Connection closed"), true, false);
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (IOException ioException){
                Logger.warn(getClass(), "IOException accused");
                Logger.exception(getClass(), ioException.getStackTrace());
                messageReceive(new FatalPackage("Failed to create a ObjectInputStream instance of the sockets InputStream instance", "Socket closed?"), true, false);
            }
        }
    }
}
