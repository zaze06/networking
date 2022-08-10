package me.alien.networking.client;

import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import me.alien.networking.util.packages.FatalPackage;
import me.alien.networking.util.packages.NetworkPackage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

abstract public class Client {
    public final int VERSION = 0;
    Socket socket;

    private final MessageReceiveThread messageReceiveThread;

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

    public Client(String ip) throws IOException {
        this(ip, 6550);
    }

    public abstract void messageReceive(NetworkPackage message, boolean fatal);
    public abstract void connected();

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
