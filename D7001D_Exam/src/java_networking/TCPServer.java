package java_networking;

//Requires a single command line arg - the port number
import java.net.*;	// need this for InetAddress, Socket, ServerSocket 

import java.io.*;	// need this for I/O stuff
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {

    static final Logger logger = Logger.getLogger(TCPServer.class.getName());
    // define a constant used as size of buffer 
    static volatile HashMap<String, Integer> clientsTable = new HashMap<>();

    
    static public void main(String args[]) throws Exception {
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9090);
                
            while (true) {
                //information log, just to notify of the readiness of the server to accept connections
                logger.log(Level.INFO, "Ready to receive connections on port 9090");
                new ClientHandler(listener.accept(), clientNumber++).start();
            }
        }
}