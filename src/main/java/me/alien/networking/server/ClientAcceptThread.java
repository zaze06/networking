package me.alien.networking.server;

import me.alien.networking.util.Headers;
import me.alien.networking.util.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * The {@link Thread} that's resposible for allowing new {@link me.alien.networking.client.Client}s and doing an initial handshake
 * @author Zacharias Zellén
 */
public class ClientAcceptThread extends Thread{
    /**
     * The {@link Server} instance that the {@link ClientAcceptThread} is working whit
     */
    Server server;

    /**
     * Creates a {@link ClientAcceptThread} instance to work with a {@link Server} instance
     * @param server A Server {@link Server} to listen to work whit
     */
    public ClientAcceptThread(Server server) {
        this.server = server;
    }

    /**
     * A method inharited from {@link Thread#run()} that is started in a sepret thread to not stall the main thread
     */
    @Override
    public void run() {
        while (true){
            try{
                Socket client = server.server.accept();
                Logger.info(getClass(), "processing new client");
                PrintStream out = new PrintStream(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println(Headers.START.getHeader());
                String data = in.readLine();
                if(!data.startsWith(Headers.VERSION.getHeader()+"")){
                    Logger.warn(getClass(), "Invalid packet");
                    JSONObject error = new JSONObject();
                    error.put("error", "invalid packet");
                    error.put("expected", "version header");
                    out.println(Headers.FATAL.getHeader()+error.toString());
                    in.readLine();
                    client.close();
                    continue;
                }
                data = data.substring(1);
                if(Integer.parseInt(data, 16) > server.VERSION){
                    Logger.warn(getClass(), "Client version to new");
                    JSONObject error = new JSONObject();
                    error.put("error", "version too new");
                    error.put("expected", "version "+server.VERSION);
                    out.println(Headers.FATAL.getHeader()+error.toString());
                    in.readLine();
                    client.close();
                    continue;
                }else if(Integer.parseInt(data, 16) < server.VERSION){
                    Logger.warn(getClass(), "Client version to old");
                    JSONObject error = new JSONObject();
                    error.put("error", "version too old");
                    error.put("expected", "version "+server.VERSION);
                    out.println(Headers.FATAL.getHeader()+error.toString());
                    in.readLine();
                    client.close();
                    continue;
                }

                out.println(Headers.SUCCESS.getHeader());

                Client client1 = new Client(client, server);
                server.clients.add(client1);
                server.newClient(client1);
            }catch (IOException ignored){

            }
        }
    }
}
