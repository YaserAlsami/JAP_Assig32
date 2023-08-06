package application;

import java.net.*;
import java.io.*;



/**
 * The Client class represents a client that can connect to a server, send messages, and receive responses.
 * It provides methods for starting and stopping the connection, sending messages, and getting/setting client information.
 */
public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int clientId; // Store the unique client ID
    private String userName;
    private String serverName;
    private int portNumber;
    
    
    
    /**
     * Starts a connection to the server at the specified IP address and port.
     *
     * @param ip The IP address of the server.
     * @param port The port number of the server.
     * @throws IOException If an I/O error occurs when creating the socket or getting its I/O streams.
     */
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // Send the username to the server
        out.println(userName);
        
        // Receive and store the unique client ID from the server
        clientId = Integer.parseInt(in.readLine());
        System.out.println("Received Client ID: " + clientId);
    }

    
    
    /**
     * Sends a message to the server and returns its response.
     *
     * @param msg The message to send to the server.
     * @return The server's response to the message.
     * @throws IOException If an I/O error occurs when sending the message or receiving the response.
     */
    public String sendMessage(String msg) throws IOException {
        out.println(clientId + "#" + msg); // Prepend the client ID to each message
        return in.readLine();
    }
    /**
     * Stops the connection to the server.
     *
     * @throws IOException If an I/O error occurs when closing the connection.
     */
    public void stopConnection() throws IOException {
        // Send a "DISCONNECT" message before closing the connection
        out.println("DISCONNECT");
        String serverResponse = in.readLine();  // Wait for the server's response

        // If the server acknowledged the disconnection, close the connection
        if (serverResponse.equals("ACK")) {
            in.close();
            out.close();
            clientSocket.close();
        }
    }
    
    
    /**
     * Returns the user name of the client.
     *
     * @return The user name of the client.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * Sets the user name of the client.
     *
     * @param userName The new user name of the client.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * Returns the name of the server to which the client is connected.
     *
     * @return The name of the server.
     */
    public String getServerName() {
        return serverName;
    }
    /**
     * Sets the name of the server to which the client will connect.
     *
     * @param serverName The new server name.
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    /**
     * Returns the port number of the server to which the client is connected.
     *
     * @return The port number of the server.
     */
    public int getPortNumber() {
        return portNumber;
    }
    /**
     * Sets the port number of the server to which the client will connect.
     *
     * @param portNumber The new port number.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
