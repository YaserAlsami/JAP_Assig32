package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GameBoard {
    GridPane gridPane;
    private ProgressBar progressBar;
    private int boardSize;
    boolean designModeEnabled = false;
    String selectedShipDirection = "horizontal";
    private Ship[][] board;
    private 
    Button[][] buttons;
  
    GameBoard opponentBoard;
    private int totalShipSegmentsAllowed;
    private int currentTotalShipSegments = 0;
    private int totalUserShipsPlaced ; 
    private Map<Integer, Integer> userBoatsBySize = new HashMap<>();
    private boolean gameStarted = false;
    private String owner;  
    Button button = new Button();
    int j;
    int i ;
    final int finalI = i;
    final int finalJ = j;
    private int totalShipSegmentsHit = 0;
boolean clicked = false ;
private List<Ship> ships = new ArrayList<>();

    
    
    public GameBoard(int boardSize, String owner ) {
        this.boardSize = boardSize;
        this.gridPane = new GridPane();
        
        gridPane.setAlignment(Pos.CENTER);
        this.progressBar = new ProgressBar();
        this.owner = owner; // Add this line
        this.opponentBoard = null;
        initializeBoard();
     
    }
    
 
 
    /**
     * Initializes the game board with a given size.
     * 
     * This function clears the current GridPane, and initializes the board and buttons 
     * with new Ship and Button instances respectively. Labels are added on the top and left 
     * side of the GridPane for row and column markers. Each button in the GridPane is set 
     * up with an action event that handles ship placement and hit operations based on the 
     * current player. This method also checks if all ships have been sunk and displays 
     * a winner or loser splash screen accordingly. If the owner is 'AI', it also places 
     * the ships randomly for the AI player.
     * 
     */    void initializeBoard() {
        // Clear the gridPane
        gridPane.getChildren().clear();
        this.board = new Ship[boardSize][boardSize];

        this.buttons = new Button[boardSize][boardSize]; // Initialize the buttons array
        // Initialize the buttons and labels
        for (int i = 0; i < boardSize; i++) {
            // Add labels on top
            Label topLabel = new Label(String.valueOf((char) ('A' + i)));
            gridPane.add(topLabel, i + 1, 0);

            // Add labels on the left
            Label leftLabel = new Label(String.valueOf((char) ('a' + i)));
            gridPane.add(leftLabel, 0, i + 1);

            for (int j = 0; j < boardSize; j++) {
                Button button = new Button();
                button.setPrefWidth(25);  // Set preferred width
                button.setPrefHeight(25); // Set preferred height
                gridPane.add(button, i + 1, j + 1);
                buttons[i][j] = button; // Store a reference to the button
                Cell cell = new Cell();
                cell.setStatus(Cell.CellStatus.EMPTY);
                cell.setRow(i);
                cell.setCol(j);
                // Create final copies of the loop variables
                final int finalI = i;
                final int finalJ = j;
                
                button.setOnAction(event -> {
                    if (MiddlePanel.isPlayer) {
                        if (MiddlePanel.dir == true) {
                            System.out.println("Button clicked at coordinates: (" + finalI + ", " + finalJ + MiddlePanel.shipDirectionn + ")");
                            placeShip(finalI, finalJ, MiddlePanel.shipSizee, MiddlePanel.shipDirectionn, "player");
                      
                          
                        	
                        }
                        else {
                        	hitShip(finalI, finalJ, "AI");
                        	System.out.println("ff");
                        	if (allShipsSunk()) {
                        	    System.out.println("winner player");

                        	    // Create a new Stage (i.e., a new window)
                        	    Stage winnerStage = new Stage();

                        	    // Set the title of the window
                        	    winnerStage.setTitle("Winner!");

                        	    // Load an image and create a ImageView object to display it
                        	    Image image = new Image("game_winner.jpg"); // replace with your image path
                        	    ImageView imageView = new ImageView(image);

                        	    // Create a Label to display some text
                        	    Label label = new Label("You Won!");

                        	    // Create a VBox to hold the ImageView and Label
                        	    VBox vbox = new VBox(imageView, label);
                        	    vbox.setAlignment(Pos.CENTER);

                        	    // Set the Scene (i.e., the contents of the window)
                        	    Scene scene = new Scene(vbox, 300, 200); // adjust the window size as needed
                        	    winnerStage.setScene(scene);

                        	    // Display the window
                        	    winnerStage.show();
                        	}
                        	
                        }


                        
                    }        
                });
            }
        }
        if (owner.equals("AI")) {
             placeShipRandomly("AI");
         }  
   
    }
  
   


     /**
      * Sets the opponent's game board.
      *
      * This method allows to set the opponent's game board 
      * to the provided game board instance.
      *
      * @param opponentBoard the GameBoard instance to set as the opponent's board
      */
     public void setOpponentBoard(GameBoard opponentBoard) {
         this.opponentBoard = opponentBoard;
     }




    
     /**
      * Sets the size of the game board.
      *
      * This method sets the size of the game board and reinitializes 
      * the board layout on the JavaFX Application Thread using 
      * Platform.runLater. It ensures that the grid layout is updated 
      * correctly on the JavaFX Application Thread, which is crucial 
      * for GUI updates.
      *
      * @param boardSize the new size to set for the game board
      */
     public void setBoardSize(int boardSize) {
         Platform.runLater(() -> {
             this.boardSize = boardSize;

             // Initialize the new grid
             initializeBoard();

             // Request layout update
             gridPane.requestLayout();
         });
     }


   
     /**
      * Gets the current GridPane object.
      *
      * @return The current GridPane.
      */
     public GridPane getGridPane() {
         return gridPane;
     }
   
     /**
      * Sets the GridPane object.
      *
      * @param gridPane The GridPane to be set.
      */
     public void setGridPane(GridPane gridPane) {
         this.gridPane = gridPane;
     }

     /**
      * Calculates the current game progress.
      *
      * @return The ratio of total ship segments hit to the current total ship segments.
      */
     /**
      * Gets the current game progress.
      *
      * @return The ratio of total ship segments hit to the current total ship segments.
      */
     public double getGameProgress() {
         return (double) totalShipSegmentsHit / currentTotalShipSegments;
     }

     /**
      * Sets the ProgressBar object.
      *
      * @param progressBar The ProgressBar to be set.
      */
     public void setProgressBar(ProgressBar progressBar) {
         this.progressBar = progressBar;
     }

     /**
      * Calculates the number of ships based on the maximum size of the ship.
      *
      * @param maxBoatSize The maximum size of the boat.
      * @return The number of ships.
      */
     private int calculateNumberOfShips(int maxBoatSize) {
         return (maxBoatSize * (maxBoatSize + 1) * (maxBoatSize + 2)) / 6;
     }

     /**
      * Randomly places ships of varying sizes on the game board.
      * 
      * The method calculates the total number of ships based on the board size, and distributes 
      * these ships from the largest size down to the smallest. Each ship is placed at a random 
      * location on the board, with collision checks to ensure ships do not overlap. Once placed, 
      * the ship's cells are updated on the board. If a player owns the ship, the corresponding 
      * button in the GridPane is updated to reflect the presence of the ship.
      *
      * @param owner The owner of the ship, either "player" or "AI"
      */
    public void placeShipRandomly(String owner) {
        // Calculate the total number of ships to be placed
        int totalShips = calculateNumberOfShips(boardSize);
        int totalBoatsPlaced = 0; // Keep track of total boats placed

        // Distribute the total number of ships among different sizes
        for (int size = boardSize/2; size >= 1; size--) { // Start from maximum size and go down to 1
            int numShipsOfSize = boardSize/2 - size + 1; // Calculate number of ships of this size

            // Place each ship of the current size
            for (int i = 0; i < numShipsOfSize; i++) {
                if (totalBoatsPlaced >= totalShips) { // If total boats placed reached the totalShips limit, stop
                    return;
                }

                // Create a new ship of the current size
                Ship ship = new Ship(size, "H");  // Horizontal orientation for simplicity

                // Choose a random location for the ship
                int row = (int) (Math.random() * boardSize);
                int col = (int) (Math.random() * (boardSize - size + 1));  // Ensure the ship fits on the board

                // Check for ship collision
                boolean collision = false;
                for (int j = 0; j < size; j++) {
                    if (board[row][col + j] != null) {
                        collision = true;
                        break;
                    }
                }

                // If a collision was detected, skip this iteration
                if (collision) {
                    i--;
                    continue;
                }

                // Set the ship's location
                List<Cell> location = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    Cell cell = new Cell();
                    cell.setRow(row);
                    cell.setCol(col + j);
                    cell.setStatus(Cell.CellStatus.SHIP);
                    location.add(cell);

                   
                    board[row][col + j] = ship;
                }
                ship.setLocation(location);
                ships.add(ship);

         
                for (Cell cell : location) {
                    Node existingNode = getNodeFromGridPane(gridPane, cell.getCol() + 1, cell.getRow() + 1);
                    if (existingNode instanceof Button) {
                      //  If a button already exists at this location, update its style
                    	if (owner.equals("player")) {
                        Platform.runLater(() -> {
                            ((Button) existingNode).setStyle("-fx-background-color: #00FF00; -fx-background-color:hover: #00FF00; -fx-background-color:pressed: #00FF00;");
                            gridPane.layout();  // Force a layout pass
                        });
                    	}
                   } 
                }
                currentTotalShipSegments++; // Increment total boats placed
            }
        }
        gridPane.requestLayout();
    }
    public List<Ship> getShips() {
        return ships;
    }


    /**
     * Returns the Node in a GridPane at the specified column and row.
     *
     * @param gridPane The GridPane to retrieve the Node from.
     * @param col The column index of the Node.
     * @param row The row index of the Node.
     * @return The Node at the specified column and row, or null if no such Node exists.
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * Sets the design mode of the game board.
     *
     * @param enabled If true, design mode is enabled. If false, design mode is disabled.
     */
    public void setDesignModeEnabled(boolean enabled) {
        this.designModeEnabled = enabled;
    }
    
    /**
     * Sets the direction of the selected ship.
     *
     * @param direction The direction of the selected ship.
     */
    public void setSelectedShipDirection(String direction) {
        this.selectedShipDirection = direction;
    }
    
    /**
     * Places a ship on the board with the given parameters.
     *
     * @param x The x-coordinate where the ship will start.
     * @param y The y-coordinate where the ship will start.
     * @param size The size of the ship to be placed.
     * @param direction The direction of the ship's placement ("horizontal" or "vertical").
     * @param owner The owner of the ship ("player" or "AI").
     * @throws IllegalArgumentException If the placement is not possible (e.g., if the ship doesn't fit on the board, 
     * if there's already a ship at the intended location, or if the direction is invalid).
     */
    public void placeShip(int x, int y, int size, String direction, String owner) {
        int totalShipSegmentsAllowed = calculateNumberOfShips(boardSize);

        // Check if adding the new ship would exceed the total allowed ship segments
        if (currentTotalShipSegments + size > totalShipSegmentsAllowed) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);  // No header
                alert.setContentText("Cannot add ship: total ship size limit exceeded");
                alert.showAndWait();
            });
            return;  // Return here to stop placing the ship 
        }
        
        // Check the number of user placed boats of the current size
        int currentBoatsOfSize = userBoatsBySize.getOrDefault(size, 0);
        int maxBoatsOfSize = boardSize / 2 - size + 1;
        if (currentBoatsOfSize >= maxBoatsOfSize) {
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);  // No header
                alert.setContentText("Cannot add more boats of size");
                alert.showAndWait();
            });
            return;  // Return here to stop placing the ship
        }

        try {
        	Ship ship = new Ship(size, direction, owner);
            List<Cell> shipCells = new ArrayList<>();
            if (direction==null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);  // No header
                    alert.setContentText("please choose direction ");
                    alert.showAndWait();
                });
                return;
            }
            if (direction.equals("horizontal")) {
                if (x + size > boardSize) {
                    throw new IllegalArgumentException("Ship does not fit on board");
                }

                for (int i = 0; i < size; i++) {
                    if (board[x + i][y] != null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);  // No header
                            alert.setContentText("There is already a ship at this location ");
                            alert.showAndWait();
                        });
                        return;
                    }

                    Cell cell = new Cell(x + i, y, Cell.CellStatus.SHIP);
                    shipCells.add(cell);

                    // For horizontal placement
                    Button button = buttons[x + i][y];
                    button.setText("S"); // Indicate the ship's presence
                    button.setStyle("-fx-background-color: #00FF00"); // Set the button background to green
                }
            } else if (direction.equals("vertical")) {
                if (y + size > boardSize) {
                    throw new IllegalArgumentException("Ship does not fit on board");
                }

                for (int i = 0; i < size; i++) {
                    if (board[x][y + i] != null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);  // No header
                            alert.setContentText("There is already a ship at this location ");
                            alert.showAndWait();
                        });
                        return;
                        
                    }

                    Cell cell = new Cell(x, y + i, Cell.CellStatus.SHIP);
                    shipCells.add(cell);

                    // For vertical placement
                    Button button = buttons[x][y + i];
                    button.setText("S"); // Indicate the ship's presence
                    button.setStyle("-fx-background-color: #00FF00"); // Set the button background to green
                }
            } else {
                throw new IllegalArgumentException("Invalid direction");
            }

            ship.setLocation(shipCells);
            ships.add(ship);
            for (Cell cell : shipCells) {
                board[cell.getRow()][cell.getCol()] = ship;
            }
            
            // Update the total number of ship segments after successfully placing the ship
            currentTotalShipSegments += size;
            System.out.println(currentTotalShipSegments);
            // Update the total number of user placed ships
            totalUserShipsPlaced++;
            
            // Update the count of user placed boats of current size
            userBoatsBySize.put(size, currentBoatsOfSize + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


    /**
     * Handles a ship-hit attempt at the specified coordinates on the board.
     *
     * <p>This function verifies that the provided coordinates are within the board bounds.
     * If there's a ship at the given coordinates, it applies a hit to that ship, changes
     * the color of the corresponding button to indicate a hit (red), and removes the ship
     * from the board. If the ship is sunk as a result of the hit, it increments the total
     * number of ship segments hit. If there's no ship at the given coordinates, it simply
     * indicates a miss.
     *
     * @param x the x-coordinate for the hit attempt
     * @param y the y-coordinate for the hit attempt
     * @param owner the owner of the ship being hit
     * @throws IllegalArgumentException if the coordinates are out of bounds
     *
     */
    public void hitShip(int x, int y,String owner ) {
    	Button button;
    	 button = buttons[x][y];
        // Check if the given coordinates are within the board bounds
    	System.out.println(x+" "+ y + " ");
        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize) {
            throw new IllegalArgumentException("Coordinates are out of bounds");
        }

        Ship ship = board[x][y];
        
        // Check if there's a ship at the given coordinates
        if (ship != null) {
            ship.hit();
            System.out.println("Hit!");
            
            button.setStyle("-fx-background-color: #990000");
            board[x][y]=null;

            // Check if the ship is sunk
            if (ship.isSunk()) {
                System.out.println("Ship sunk!");
                totalShipSegmentsHit++;
            }
        } else {
            System.out.println("Miss!");
            button.setStyle("-fx-background-color: #808080");
        }
        Platform.runLater(() -> {
            progressBar.setProgress(getGameProgress());
        });
    }


 
 

    public boolean allShipsSunk() {
        return totalShipSegmentsHit == currentTotalShipSegments;
    }
   

    

    
    
    /**
     * Clears both the logical and visual representation of the board.
     * 
     * The logical board is represented by a 2D array, with each cell initially set to null.
     * The visual board is then reinitialized to reflect these changes.
     * 
     * This function iterates over each cell in the logical board and then calls the initializeBoard function to update the visual board.</p>
     */
    public void clearBoard() {
        // Clear logical board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = null;
            }
        }
        
        // Clear visual board
        initializeBoard();
    }
    
    public void placeShipsFromConfig(List<Ship> shipsConfig, String owner) {
        for (Ship ship : shipsConfig) {
            List<Cell> location = ship.getLocation();
            for (Cell cell : location) {
                int row = cell.getRow();
                int col = cell.getCol();

                if (board[row][col] != null) {
                    // There's already a ship here, this might be an invalid configuration.
                    // Handle as you see fit, maybe throw an exception or skip.
                    continue;
                }

                board[row][col] = ship;

                // If a button already exists at this location, update its style
                Node existingNode = getNodeFromGridPane(gridPane, col + 1, row + 1);
                if (existingNode instanceof Button) {
                    if (owner.equals("player")) {
                        Platform.runLater(() -> {
                            ((Button) existingNode).setStyle("-fx-background-color: #00FF00; -fx-background-color:hover: #00FF00; -fx-background-color:pressed: #00FF00;");
                            gridPane.layout();  // Force a layout pass
                        });
                    }
                }
            }
        }
        gridPane.requestLayout();
    }


    
}



