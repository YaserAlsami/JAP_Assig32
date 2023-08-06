package application;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;

/**
 * This class represents the middle panel of the application's UI, including various control options for the user.
 * It includes controls for setting the board dimensions, language selection, timer display, game reset,
 * and mode switching between design mode and play mode. 
 * Additionally, it contains a ComboBox for the user to set the ship size and direction.
 * The class also provides a MenuBar with a Game menu and a Colors submenu in the Help menu.
 * The "Game" menu allows the user to start a new game, display the solution, or exit the game.
 * The "Colors" menu offers options to select and set three different colors.
 * Note that these menu items are currently empty and can be implemented according to application's requirements.
 */

public class MiddlePanel {
	
    private VBox vBox;
    private GameBoard userBoard;
    private GameBoard aiBoard;
    private ComboBox<Integer> dimensionSelection;
     Timeline timer;
    private int timeSeconds = 0;
    private MenuBar menuBar; // Add this field
    static int shipSizee;
    static String shipDirectionn;
    Label timerLabel = new Label();
    static boolean dir;
    static boolean isPlayer;
    Button randomButton = new Button();
    private GameBoard gameBoard;
    
    /**
     * Constructor for MiddlePanel class.
     * Initializes the VBox and all of its child elements like ComboBox for language and board dimension, timer,
     * reset button, design mode button, play button, and menu bar.
     * Also sets up event listeners for the various UI elements.
     */
    public MiddlePanel() {
    	   userBoard = new GameBoard(6,"player"); 
           aiBoard = new GameBoard(6,"AI"); 
           gameBoard = userBoard;  // or gameBoard = aiBoard;
        this.vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
 
       
        Label languageLabel = new Label("Language");
        ComboBox<String> languageSelection = new ComboBox<>();
        languageSelection.getItems().addAll("English", "Arabic");
        languageSelection.setValue("English"); // Set default value
        HBox languageBox = new HBox(languageLabel, languageSelection);		
        vBox.getChildren().add(languageBox);
        languageBox.setAlignment(Pos.CENTER);
        // Board dimension
        Label dimensionLabel = new Label("Dimension");
        dimensionSelection = new ComboBox<>();
        dimensionSelection.getItems().addAll(3, 4, 5, 6, 7);
        dimensionSelection.setValue(3); // Set default value
        HBox dimensionBox = new HBox(dimensionLabel, dimensionSelection);
        
        vBox.getChildren().add(dimensionBox);
        dimensionBox.setAlignment(Pos.CENTER);
        // Add a listener to the dimension selection
        dimensionSelection.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
              int   boardSize = 2 * newValue;
                userBoard.setBoardSize(boardSize);
                aiBoard.setBoardSize(boardSize);
            }
            
        });
        
        
      

        timerLabel = new Label("Timer :");
        vBox.getChildren().add(timerLabel);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
 

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
    
        });
        vBox.getChildren().add(resetButton);
        
        
        // to create the TextArea
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter text here"); // Set placeholder text
        textArea.setPrefWidth(50); // Set preferred width
        textArea.setPrefHeight(100); // Set preferred height

        // Add the TextArea to the VBox
        vBox.getChildren().add(textArea);
        
     // Create the design mode button
     // Create a ComboBox for the ship size
        ComboBox<Integer> shipSizeComboBox = new ComboBox<>();
        shipSizeComboBox.getItems().addAll(1, 2, 3, 4, 5); // Add ship sizes
        shipSizeComboBox.setValue(0); // Set default value
        shipSizeComboBox.setOnAction(event -> {
            // Update the ship size in the GameBoard instance whenever a new size is selected
        	shipSizeComboBox.setVisible(true); // Hide the ComboBox
            shipSizee=shipSizeComboBox.getValue();
          
            
        });
        vBox.getChildren().add(shipSizeComboBox);

        // Create a ComboBox for the ship direction
        ComboBox<String> shipDirectionComboBox = new ComboBox<>();
        shipDirectionComboBox.getItems().addAll("horizontal", "vertical"); // Add directions
        shipDirectionComboBox.setValue("None"); // Set default value
        shipDirectionComboBox.setVisible(true); // Hide the ComboBox
        shipDirectionComboBox.setOnAction(event -> {
            // Update the selected ship direction when a new direction is selected
            userBoard.selectedShipDirection = shipDirectionComboBox.getValue();
             shipDirectionn = shipDirectionComboBox.getValue();
        });
        vBox.getChildren().add(shipDirectionComboBox);

        Button designModeButton = new Button("Design Mode");
        designModeButton.setOnAction(event -> {
            // Enable design mode
           dir = true;
           isPlayer = true;
            userBoard.initializeBoard	();
            // Show the ComboBoxes
            shipSizeComboBox.setVisible(true);
            shipDirectionComboBox.setVisible(true);
        });
        vBox.getChildren().add(designModeButton);



        Button playButton = new Button("Play");
        playButton.setOnAction(event -> {
        	timer.play();
        	dir=false;
        	
        	 
        });
        vBox.getChildren().add(playButton);
    	//userBoard.hitShip(userBoard.finalI, userBoard.finalJ);
        

     // Create the menu bar
        menuBar = new MenuBar();

        // Create the "Game" menu
        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New");
        newGameItem.setOnAction(event -> {
        });
        MenuItem solutionItem = new MenuItem("Solution");
        solutionItem.setOnAction(event -> {
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> {
        });
        gameMenu.getItems().addAll(newGameItem, solutionItem, exitItem);

        // Create the "Help" menu
        Menu helpMenu = new Menu("Help");

        // Create the Rules menu item
        MenuItem rulesItem = new MenuItem("Rules");
        rulesItem.setOnAction(event -> {
            // Create a dialog to show the rules
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rules");
            alert.setHeaderText("Battleship Game Rules");

            try {
                // Specify your rules text file path
                Path rulesPath = Path.of("C:/CST8221/Assig22/src/application/Rules.txt");
                
                // Read file and join lines with new line character
                String rulesText = Files.readAllLines(rulesPath, StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
                
                // Set the content text from the rules file
                alert.setContentText(rulesText);
            } catch (IOException e) {
                e.printStackTrace();
                alert.setContentText("Error loading rules.");
            }

            alert.showAndWait();
        });
        // Add the rules item to the help menu
        helpMenu.getItems().add(rulesItem);

        // Create the "Colors" menu
        Menu colorsMenu = new Menu("Colors");

        MenuItem color1Item = new MenuItem("Color1");
        ColorPicker color1Picker = new ColorPicker(Color.WHITE);
        color1Picker.setOnAction(event -> {
        });
        color1Item.setGraphic(color1Picker);

        MenuItem color2Item = new MenuItem("Color2");
        ColorPicker color2Picker = new ColorPicker(Color.WHITE);
        color2Picker.setOnAction(event -> {
        });
        color2Item.setGraphic(color2Picker);

        MenuItem color3Item = new MenuItem("Color3");
        ColorPicker color3Picker = new ColorPicker(Color.WHITE);
        color3Picker.setOnAction(event -> {
        });
        color3Item.setGraphic(color3Picker);

        // Add color pickers to the colors menu
        colorsMenu.getItems().addAll(color1Item, color2Item, color3Item);

        // Add colors menu to the help menu
        helpMenu.getItems().add(colorsMenu);

        // Add the menus to the menu bar
        menuBar.getMenus().addAll(gameMenu, helpMenu);

        // Add the menu bar to the layout
        vBox.getChildren().add(menuBar);
     	
    }


    /**
     * Returns the ComboBox object for the board dimension selection.
     * @return ComboBox<Integer> dimensionSelection - the ComboBox for the board dimension selection.
     */
    public ComboBox<Integer> getDimensionSelection() {
        return dimensionSelection;
    }
    
    /**
     * Updates the timer label.
     */
    private void updateTimer() {
        timeSeconds++;
        timerLabel.setText("Time: " + timeSeconds + " seconds");
    }
    
    /**
     * Returns the "Random" button object.
     * @return Button randomButton - the "Random" button.
     */
    public Button getRandomButton() {
        return randomButton;
    }
    
    /**
     * Returns the menu bar of the middle panel.
     * @return MenuBar menuBar - the menu bar.
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }
    
    /**
     * Returns the VBox layout container of the middle panel.
     * @return VBox vBox - the VBox container.
     */
    public VBox getVBox() {
        return vBox;
    }
    
}
