package application;

import java.net.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/**
 * The Server class represents a multi-threaded server that handles multiple client connections.
 * It provides methods for starting and stopping the server and for handling client connections.
 */
public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private ConcurrentHashMap<Integer, String> clientConfigurations = new ConcurrentHashMap<>(); // To store client configurations
    private int clientIdCounter = 0; // To generate unique client IDs
    private int currentClientCount = 0;
  private Consumer<String> onServerStartCallback;
  private boolean finalizeWhenNoClients = false;
  private BiConsumer<Integer, String> onClientConnectCallback;
  private BiConsumer<Integer, String> onClientDisconnectCallback;

  
  /**
   * Sets the callback function to be triggered when a client disconnects from the server.
   *
   * @param callback The callback function to set.
   */
  public void setOnClientDisconnectCallback(BiConsumer<Integer, String> callback) {
      this.onClientDisconnectCallback = callback;
  }

  /**
   * Sets the callback function to be triggered when a client connects to the server.
   *
   * @param callback The callback function to set.
   */
  public void setOnClientConnectCallback(BiConsumer<Integer, String> callback) {
      this.onClientConnectCallback = callback;
  }
  

  /**
   * Sets whether the server should be finalized when there are no more connected clients.
   *
   * @param finalizeWhenNoClients The new setting value.
   */
  public void setFinalizeWhenNoClients(boolean finalizeWhenNoClients) {
      this.finalizeWhenNoClients = finalizeWhenNoClients;
  }

  /**
   * Sets the callback function to be triggered when the server starts.
   *
   * @param callback The callback function to set.
   */
    public void setOnServerStartCallback(Consumer<String> callback) {
        this.onServerStartCallback = callback;
    }

    /**
     * Starts the server on the specified port.
     *
     * @param port The port number on which the server should be started.
     * @throws IOException If an I/O error occurs when opening the server socket.
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);  // Set timeout to 1 second for example

        // Trigger the callback when the server starts
        if (onServerStartCallback != null) {
            onServerStartCallback.accept("Server started successfully on port " + port);
        }

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, clientIdCounter++).start();
                clientConnected();
            } catch (SocketTimeoutException e) {
                // If a timeout occurs, check if there are no clients and the server should stop
                if (finalizeWhenNoClients && getCurrentClientCount() == 0) {
                    stop();
                    break;
                }
            }
        }

        serverSocket.close();
    }

    /**
     * Returns the current number of connected clients.
     *
     * @return The current number of connected clients.
     */
    public int getCurrentClientCount() {
        return currentClientCount;
    }

    /**
     * Increases the count of current clients when a new client connects.
     */
    public void clientConnected() {
        currentClientCount++;
    }

    // Decrease currentClientCount when a client disconnects
    public void clientDisconnected() {
        currentClientCount--;
    }

    /**
     * Stops the server.
     *
     * @throws IOException If an I/O error occurs when closing the server socket.
     */
    public void stop() throws IOException {
        isRunning = false;
        serverSocket.close();
    }
    
    
    /**
     * The ClientHandler class handles communication with a connected client.
     * It extends Thread to allow multiple client handlers to run concurrently.
     */
    public class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        public static int clientId; 

        
        /**
         * Constructs a new ClientHandler for the specified socket and client ID.
         *
         * @param socket The socket through which the client is connected.
         * @param clientId The unique ID of the client.
         */
        public ClientHandler(Socket socket, int clientId) {
            this.clientSocket = socket;
            this.clientId = clientId;
        }
        
        
        /**
         * The run method handles communication with the client.
         * It is invoked when the client handler thread is started.
         */
        public void run() {
            System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                // Read the client's name
                String clientName = in.readLine();
                
                // Inform the client of its unique ID
                out.println(clientId);

                String clientMessage;
                if (onClientConnectCallback != null) {
                    onClientConnectCallback.accept(clientId, clientName);
                }

                while ((clientMessage = in.readLine()) != null) {
                    if (clientMessage.equals("DISCONNECT")) {
                        clientDisconnected();
                        out.println("ACK");  // Send acknowledgment
                        if (onClientDisconnectCallback != null) {
                            onClientDisconnectCallback.accept(clientId, clientName);
                        }
                        break;
                    }
                    
                 // Handle P1 protocol: Storing the game configuration
                    if (clientMessage.contains("#P1#")) {
                        String config = clientMessage.split("#P1#")[1];
                        clientConfigurations.put(clientId, config);
                        out.println("ACK");  // Send acknowledgment
                    }


                    // Handle P2 protocol: Sending back a stored game configuration
                 // Handle P2 protocol: Sending back a stored game configuration
                    if (clientMessage.contains("#P2")) {
                        String configToSend = clientConfigurations.getOrDefault(clientId - 1, "");
                        out.println(configToSend);  // Send the actual game configuration, not the protocol message
                        System.out.println (configToSend);
                    }

                }

                in.close();
                out.close();
                clientSocket.close();
                
                

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
