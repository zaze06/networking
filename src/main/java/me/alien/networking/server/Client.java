package me.alien.networking.server;

import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final Server server;
    private Client client;
    private final MessageReceiveThread messageReciveThread;

    private PrintStream out;
    public Client(Socket client, Server server) {
        this.socket = client;
        this.server = server;
        messageReciveThread = new MessageReceiveThread();
        messageReciveThread.start();
        this.client = this;
        try {
            this.out = new PrintStream(client.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message){
        out.println(message);
    }

    private class MessageReceiveThread extends Thread {
        @Override
        public void run() {
            try{
                while(true){
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    try{
                        String message = in.readLine();
                        boolean fatal = false;
                        if(message.startsWith(Headers.FATAL.getHeader()+"")){
                            fatal = true;
                        }
                        server.clientMessage(client, message, fatal);
                    }catch (IOException ignored){
                        if(socket.isClosed()){
                            Logger.warn(getClass(), "IOException accused");
                            Logger.exception(getClass(), ignored.getStackTrace());
                            server.clientMessage(client, Headers.FATAL.getHeader()+(new JSONObject().put("reason", "Connected closed, assuming lost of connection to client").toString()), true);
                        }
                    }
                }
            }catch (IOException ignored){
                Logger.warn(getClass(), "IOException accused");
                Logger.exception(getClass(), ignored.getStackTrace());
                server.clientMessage(client, Headers.FATAL.getHeader()+(new JSONObject().put("reason", "Failed to create a BufferedReader instance of the sockets InputStream instance").toString()), true);
            }
        }
    }
}
