package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;

/**
 * The Main class is the starting point for a Battleship game application.
 * It handles the user interface and game initialization, and it maintains a reference
 * to the game boards for both the player and the AI.
 */
public class Main extends Application {

    private int boardSize = 6; // Default board size
    static String player="player";
    static String AI="AI";
    GameBoard playerBoard = new GameBoard(boardSize,player="gg");
    GameBoard computerBoard = new GameBoard(boardSize,AI);
    ProgressBar playerProgressBar= new ProgressBar();
    ProgressBar computerProgressBar= new ProgressBar();
     BorderPane root = new BorderPane();  
     int x,y;
     private Client client; // Class attribute
     Server server = new Server();
    
     /**
      * The start method is the main entry point for all JavaFX applications.
      *
      * @param primaryStage the primary stage for this application, onto which
      * the application scene can be set.
      */
    @Override
    public void start(Stage primaryStage) {
        try {
            playerProgressBar.setPrefHeight(20); // Set preferred height         
            computerProgressBar.setPrefHeight(20); // Set preferred height
            VBox playerBox = new VBox(playerBoard.getGridPane(), playerProgressBar);
            playerBox.setStyle("-fx-background-color: #0000FF"); // Hex color for blue
            root.setLeft(playerBox);
            VBox computerBox = new VBox(computerBoard.getGridPane(), computerProgressBar);
            computerBox.setStyle("-fx-background-color: #0000FF"); // Hex color for blue
            root.setRight(computerBox);

            primaryStage.setTitle("Yaser Alsami"); // Set the window title

            
    
            MiddlePanel middlePanel = new MiddlePanel();

            // Add a listener to the dimension selection
            middlePanel.getDimensionSelection().valueProperty().addListener((observable, oldValue, newValue) -> {
                int newBoardSize = 2 * newValue;
                playerBoard.setBoardSize(newBoardSize);
                computerBoard.setBoardSize(newBoardSize);
                boardSize = newBoardSize;
            });
            
            
            
            


            root.setCenter(middlePanel.getVBox());
            
            Button randomButton = new Button("Random");
            Label rand = new Label("Feeling Lazey? click this button to genrate a random design for you boat");
            randomButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
               	 
                	  playerBoard.placeShipRandomly("player");
                  	
                     
                      
                   
                }
            });
            
            ComboBox<String> multiplayerChoice = new ComboBox<>();
            multiplayerChoice.getItems().addAll("Server", "Client");

            multiplayerChoice.showingProperty().addListener((observable, wasShowing, isNowShowing) -> {
                if (!isNowShowing) {  // When the dropdown is hidden
                    String selection = multiplayerChoice.getValue();
                    if ("Server".equals(selection)) {
                        showDialogForServer();
                    } else if ("Client".equals(selection)) {
                        showDialogForClient();
                    }
                    // Reset selection so that the listener will trigger again for the same value
                    multiplayerChoice.setValue(null);
                }
            });

           Button playButton = new Button ("AI");
           Label ai = new Label("click AI button to have the AI make shot");
           playButton.setOnAction(new EventHandler<ActionEvent>() {
        	   public void handle(ActionEvent event) { 
        		// Assuming `playerBoard` is the player's GameBoard and `computerBoard` is the AI's GameBoard
        		    Random random = new Random ();
        		   
        		   x=  random.nextInt(boardSize);
        	        y=  random.nextInt(boardSize);
        		  
        		  
        		   playerBoard.hitShip(x, y, "player");;
           }
        });
           Label multiplayerLabel = new Label("Multiplayer:");
           HBox randButton = new HBox(10, rand, randomButton, playButton, ai, multiplayerLabel, multiplayerChoice); 
           // I added '10' to space out the components a bit. Adjust the value as needed.
           root.setBottom(randButton);

           
   
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
            
            // Create a new Stage for the splash screen
            Stage splashScreen = new Stage();
            splashScreen.initStyle(StageStyle.UNDECORATED); 

            // Create a VBox to hold the splash screen content
            VBox splashLayout = new VBox();
            splashLayout.setAlignment(Pos.CENTER);

            // Add an ImageView with your splash image
            Image splashImage = new Image("game_about.jpg"); // Replace with the path to your image
            ImageView splashImageView = new ImageView(splashImage);
            splashLayout.getChildren().add(splashImageView);
            splashImageView.setFitWidth(800);
            splashImageView.setFitHeight(600);

            // Add a ProgressBar
            ProgressBar loadProgress = new ProgressBar();
            loadProgress.setProgress(-1.0f); // Indeterminate progress
            splashLayout.getChildren().add(loadProgress);

            // Show the splash screen
            Scene splashScene = new Scene(splashLayout);
            splashScreen.setScene(splashScene);
            splashScreen.show();

            // Use a PauseTransition to delay the main game window
            PauseTransition delay = new PauseTransition(Duration.seconds(0));
            delay.setOnFinished(event -> {
                splashScreen.close(); // Close the splash screen

              
            });
        delay.play();
            
         // Load the image
            Image topImage = new Image("game_about.jpg");
            

            // Create the ImageView
            ImageView topImageView = new ImageView(topImage);
            topImageView.setFitWidth(800);
            topImageView.setFitHeight(200);

            // Add the ImageView to the top of the BorderPane
            root.setTop(topImageView);
         // Create a VBox
            VBox topBox = new VBox();

            // Add the MenuBar and the ImageView to the VBox
            topBox.getChildren().addAll(middlePanel.getMenuBar(), topImageView);

            // Set the VBox as the top node of the BorderPane
            root.setTop(topBox);

        System.out.println(root);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * The resetGame method resets the game state by reinitializing game components
     * and clearing both player and computer game boards.
     */
     void resetGame() {
        if (root != null) {
            // remove old game components
            root.getChildren().removeAll(playerBoard.getGridPane(), computerBoard.getGridPane());

            // re-initialize game components
            playerBoard = new GameBoard(boardSize,"player");
            computerBoard = new GameBoard(boardSize,"AI");
            playerProgressBar.setProgress(0);
            computerProgressBar.setProgress(0);
            if (playerBoard.getGridPane() != null && playerProgressBar != null && computerBoard.getGridPane() != null && computerProgressBar != null) {
                root.setLeft(new VBox(playerBoard.getGridPane(), playerProgressBar));
                root.setRight(new VBox(computerBoard.getGridPane(), computerProgressBar));
            } else {
                System.out.println("One or more elements are null, cannot reset game.");
            }
        } else {
            System.out.println("Root is null, cannot reset game.");
        }
        playerBoard.clearBoard();
        computerBoard.clearBoard();
    }
     
     /**
      * This method creates and shows a dialog for configuring the client for the game.
      * The dialog includes fields for entering the user name, server name, and port number,
      * as well as buttons for managing the game and sending/receiving data from the server.
      */   
     private void showDialogForClient() {
    	    Dialog<Void> clientDialog = new Dialog<>();
    	    clientDialog.initModality(Modality.NONE);
    	    clientDialog.getDialogPane().setPrefSize(600, 500); // Use desired width and height values
    	    clientDialog.setTitle("Client Configuration");
    	    
    	    // Load the image and create an ImageView
    	    Image image = new Image("client.png"); // Replace with your image path
    	    ImageView imageView = new ImageView(image);
    	    imageView.setFitWidth(480);  // Adjust width if necessary
    	    imageView.setFitHeight(250); // Adjust height if necessary

    	    // Create a VBox to hold the ImageView and the FlowPane
    	    VBox mainLayout = new VBox(10); // Vertical spacing of 10
    	    mainLayout.getChildren().add(imageView);
    	    
    	    // Create a FlowPane
    	    FlowPane flow = new FlowPane(10, 10); // horizontal and vertical gaps
    	    flow.setPadding(new Insets(10));

    	    // Create TextFields
    	    TextField userNameField = new TextField();
    	    userNameField.setPromptText("Enter User Name");
    	    
    	    TextField serverNameField = new TextField();
    	    serverNameField.setPromptText("Enter Server Name");
    	    
    	    TextField portNumberField = new TextField();
    	    portNumberField.setPromptText("Enter Port Number");

    	    // Create buttons
    	    Button connectButton = new Button("Connect");
    	    Button endGameButton = new Button("End Game");
    	    Button newGameButton = new Button("New Game");
    	    Button sendGameButton = new Button("Send Game");
    	    Button receiveGameButton = new Button("Receive Game");
    	    Button sendDataButton = new Button("Send Data");
    	    Button playButton = new Button("Play");

    	    // Create a TextArea for messages
    	    TextArea clientMessagesArea = new TextArea();
    	    clientMessagesArea.setPromptText("Client messages will appear here...");
    	    clientMessagesArea.setEditable(false);
    	    clientMessagesArea.setPrefHeight(150);

    	    // Add components to FlowPane
    	    flow.getChildren().addAll(
    	        new Label("User Name:"), userNameField,
    	        new Label("Server Name:"), serverNameField,
    	        new Label("Port Number:"), portNumberField,
    	        connectButton, endGameButton, newGameButton,
    	        sendGameButton, receiveGameButton, sendDataButton,
    	        playButton
    	    );

    	    // Add the FlowPane and TextArea to the VBox
    	    mainLayout.getChildren().addAll(flow, clientMessagesArea);

    	    clientDialog.getDialogPane().setContent(mainLayout);

    	    // Add a 'Cancel' button to your dialog
    	    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	    clientDialog.getDialogPane().getButtonTypes().add(cancelButtonType);

    	    
    	    client = new Client(); // Initialize the client

    	    connectButton.setOnAction(event -> {
    	        try {
    	            String serverName = serverNameField.getText();
    	            int portNumber = Integer.parseInt(portNumberField.getText());
    	            String userName = userNameField.getText();

    	            // Save data in the client object
    	            client.setUserName(userName);
    	            client.setServerName(serverName);
    	            client.setPortNumber(portNumber);

    	            client.startConnection(serverName, portNumber);
    	            clientMessagesArea.appendText("Connected to server " + serverName + " on port " + portNumber + " as " + userName + "\n");

    	            // Optionally, display a confirmation message or move to the game scene
    	        } catch (IOException e) {
    	            clientMessagesArea.appendText("Failed to connect to the server: " + e.getMessage() + "\n");
    	        } catch (NumberFormatException e) {
    	            clientMessagesArea.appendText("Invalid port number format: " + e.getMessage() + "\n");
    	        }
    	    });
    	    
    	    sendGameButton.setOnAction(event -> {
    	        try {
    	            // 1. Serialize all ships' locations
    	            StringBuilder serializedGameState = new StringBuilder();
    	            for (Ship ship : playerBoard.getShips()) {
    	                serializedGameState.append(ship.serializeLocation()).append("|");  // Using '|' to separate ships
    	            }
    	           
    	            // 2. Send to server
    	            String messageToSend = "P1#" + serializedGameState.toString();
    	            String serverResponse = client.sendMessage(messageToSend);
    	            clientMessagesArea.appendText("Sent game state to server: " + messageToSend + "\n");

    	            // Optionally, handle the server's response
    	            if(serverResponse.equals("ACK")) {
    	                clientMessagesArea.appendText("Server acknowledged game state.\n");
    	            }
    	            
    	        } catch (IOException e) {
    	            clientMessagesArea.appendText("Error sending game state: " + e.getMessage() + "\n");
    	        }
    	    });

    	    receiveGameButton.setOnAction(event -> {
    	        try {
    	            // 1. Request game state from server
    	            String requestMessage = "P2";
    	            String receivedGameState = client.sendMessage(requestMessage);

    	            // 2. Deserialize game state and update each ship
    	            String[] shipsData = receivedGameState.split("\\|");  // Split by ships
    	            List<Ship> ships = new ArrayList<>();
    	            for (String shipData : shipsData) {
    	                Ship ship = new Ship(/*... appropriate parameters ...*/);
    	                ship.deserializeLocation(shipData);
    	                ships.add(ship);
    	            }

    	            // Place ships on the computer board
    	            computerBoard.placeShipsFromConfig(ships, "AI");

    	        } catch (IOException e) {
    	            clientMessagesArea.appendText("Error receiving game state: " + e.getMessage() + "\n");
    	        }
    	    });

    	    endGameButton.setOnAction(event -> {
    	        try {
    	            client.stopConnection(); // Disconnect from the server
    	            clientMessagesArea.appendText("Disconnected from server " + serverNameField.getText() + " on port " + portNumberField.getText() + " as " + userNameField.getText() + "\n");
    	        } catch (IOException e) {
    	            clientMessagesArea.appendText("Error disconnecting from the server: " + e.getMessage() + "\n");
    	        }
    	    });
    	    
    	    clientDialog.showAndWait();
    	}

  
     /**
      * This method creates and displays a dialog for configuring the server for the game.
      * The dialog includes a field for entering the port number, a checkbox for server finalization,
      * a text area for displaying server messages, and a start server button.
      */
     private void showDialogForServer() {
    	    Dialog<Void> serverDialog = new Dialog<>();
    	    serverDialog.initModality(Modality.NONE);
    	    serverDialog.setTitle("Server Configuration");

    	    // Create main layout - VBox to contain image and flow pane
    	    VBox mainLayout = new VBox(10);

    	    // Load and add the image at the top
    	    Image image = new Image("server.png");
    	    ImageView imageView = new ImageView(image);
    	    imageView.setFitWidth(480);
    	    imageView.setFitHeight(100);
    	    mainLayout.getChildren().add(imageView);

    	    // Create a FlowPane for the remaining components
    	    FlowPane flow = new FlowPane(10, 10);
    	    flow.setPadding(new Insets(10));

    	    // Port TextField
    	    TextField portField = new TextField();
    	    portField.setPromptText("Port");

    	    // Text Area for messages
    	    TextArea messagesArea = new TextArea();
    	    messagesArea.setPromptText("Server messages will appear here...");
    	    messagesArea.setEditable(false);
    	    messagesArea.setPrefHeight(150);

    	    // Start Button
    	    Button startButton = new Button("Start Server");
    	    startButton.setOnAction(event -> {
    	        try {
    	            int port = Integer.parseInt(portField.getText());

    	            // Set the callback function
    	            server.setOnServerStartCallback(message -> {
    	                Platform.runLater(() -> {
    	                    messagesArea.appendText(message + "\n");
    	                });
    	            });

    	            new Thread(() -> {
    	                try {
    	                    server.start(port);
    	                    Platform.runLater(() -> {
    	                        messagesArea.appendText("Server started successfully on port " + port + "\n");
    	                    });
    	                } catch (IOException e) {
    	                    Platform.runLater(() -> {
    	                        Alert alert = new Alert(Alert.AlertType.ERROR);
    	                        alert.setTitle("Error Dialog");
    	                        alert.setHeaderText("Server Error");
    	                        alert.setContentText("Error starting the server: " + e.getMessage());

    	                        alert.showAndWait();
    	                    });
    	                }
    	            }).start();

    	        } catch (NumberFormatException e) {
    	            messagesArea.appendText("Invalid port number format: " + e.getMessage() + "\n");
    	        }
    	    });

    	    CheckBox finalizeServerCheckBox = new CheckBox("Finalize server when no more clients");
    	    finalizeServerCheckBox.setOnAction(event -> {
    	        Server server = new Server();
    	        server.setFinalizeWhenNoClients(finalizeServerCheckBox.isSelected());
    	    });


    	
    	 flow.getChildren().addAll(
    	     new Label("Port:"), portField, startButton,
    	     new Label("Messages:"), messagesArea,
    	     finalizeServerCheckBox // Add it here
    	 );
    	 server.setOnClientConnectCallback((clientId, clientName) -> {
    		    Platform.runLater(() -> {
    		        messagesArea.appendText("Client " + clientId + " connected with name " + clientName + "\n");
    		    });
    		});
    	 
    	 
    	 server.setOnClientDisconnectCallback((clientId, clientName) -> {
    		    Platform.runLater(() -> {
    		        messagesArea.appendText("Client " + clientId + " disconnected with name " + clientName + "\n");
    		    });
    		});

    	 
    	 

    	    mainLayout.getChildren().add(flow);

    	    // Create Cancel ButtonType
    	    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    	    serverDialog.getDialogPane().getButtonTypes().add(cancelButtonType);

    	    serverDialog.getDialogPane().setContent(mainLayout);

    	    serverDialog.showAndWait();
    	}






     /**
      * The main method is the entry point for all Java applications.
      *
      * @param args the command-line arguments.
      */
    public static void main(String[] args) {
        launch(args);
    }
}
