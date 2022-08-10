package me.alien.networking.client;

import me.alien.networking.server.Server;
import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import me.alien.networking.util.packages.FatalPackage;
import me.alien.networking.util.packages.MessagePackage;
import me.alien.networking.util.packages.NetworkPackage;
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
     * The {@link Thread} that make sure that the received messages get handed in {@link #messageReceive(NetworkPackage, boolean)}
     */
    private final MessageReceiveThread messageReceiveThread;

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
     * {@link #messageReceive(NetworkPackage, boolean)} gets called when {@link #messageReceiveThread} receives a new message
     * @param message A class that will contain the information of the information <br> example can be {@link MessagePackage} which will contain {@link MessagePackage#message} that is a raw {@link String} message.
     * @param fatal If this is {@code true} then the connection will be closed directly after this method is complete. If its {@code true} then the message will be a {@link FatalPackage} and it will contain a {@link FatalPackage#reason} and {@link FatalPackage#error} that will describe the issue
     */
    public abstract void messageReceive(NetworkPackage message, boolean fatal);

    /**
     * {@link #connected()} will be called when it's done with the initial handshake hear its possible to do some second hand handshake to confirm more information
     */
    public abstract void connected();

    /**
     * The {@link MessageReceiveThread} is a {@link Thread} that will make sure all receive packages will be sent to {@link #messageReceive(NetworkPackage, boolean)}
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
                        NetworkPackage message = (NetworkPackage) in.readObject();
                        boolean fatal = false;
                        if(message instanceof FatalPackage){
                            fatal = true;
                        }
                        messageReceive(message, fatal);
                    }catch (IOException ignored){
                        if(socket.isClosed()){
                            Logger.warn(getClass(), "IOException accused");
                            Logger.exception(getClass(), ignored.getStackTrace());
                            messageReceive(new FatalPackage("Connected closed, assuming lost of connection to client", "Connection closed"), true);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch (IOException ignored){
                Logger.warn(getClass(), "IOException accused");
                Logger.exception(getClass(), ignored.getStackTrace());
                messageReceive(new FatalPackage("Failed to create a ObjectInputStream instance of the sockets InputStream instance", "Socket closed?"), true);
            }
        }
    }
}
