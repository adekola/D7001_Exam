/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.FileHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekola
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private int clientNumber;

    public ClientHandler(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    public void run() {
            TCPServer.clientsTable.put(socket.getInetAddress().getHostAddress(), socket.getPort());
            
            // Decorate the streams so we can send characters
            // and not just bytes.  Ensure output is flushed
            // after every newline.
    }
}
