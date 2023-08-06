

package application;

import javafx.scene.control.Button;
/**
 * Represents an individual cell in a game of Battleship. 
 * Each cell can be empty, contain a ship, or represent a hit or miss from an attempted attack.
 * The Cell class extends Button, allowing the player to interact with it in the GUI.
 */

public class Cell extends Button {
	
	/**
     * Enum to represent the status of the cell - EMPTY, SHIP, HIT, MISS.
     */
    public enum CellStatus {
        EMPTY,
        SHIP,
        HIT,
        MISS
    }
    // Properties of the cell
    private int row;
    private int col;
    private CellStatus status;
    
    /**
     * Default constructor for Cell.
     */
    public Cell () {
    
    }
    
    
    /**
     * Constructor for Cell with row, column and status.
     *
     * @param row The row in which the cell is located.
     * @param col The column in which the cell is located.
     * @param status The initial status of the cell.
     */
    public Cell(int row, int col, CellStatus status) {
        this.row = row;
        this.col = col;
        this.status = status;

        this.setPrefWidth(25);  // Set preferred width
        this.setPrefHeight(25); // Set preferred height

        updateColor();

        // Add event handler
        this.setOnAction(event -> {
            if (this.status != CellStatus.HIT && this.status != CellStatus.MISS) {
                if (this.status == CellStatus.SHIP) {
                    this.status = CellStatus.HIT;
                } else if (this.status == CellStatus.EMPTY) {
                    this.status = CellStatus.MISS;
                }
                updateColor();
            }
        });
    }
    
    /**
     * Updates the color of the cell based on its current status.
     */
    private void updateColor() {
        switch (status) {
            case EMPTY:
            case MISS:
                this.setStyle("-fx-background-color: darkgray;");
                break;
            case SHIP:
                this.setStyle("-fx-background-color: blue;");
                break;
            case HIT:
                this.setStyle("-fx-background-color: red;");
                break;
        }
    }
    
    /**
     * Returns the row index of this cell.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row index of this cell.
     *
     * @param row The row index to set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Returns the column index of this cell.
     *
     * @return The column index.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the column index of this cell.
     *
     * @param col The column index to set.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Returns the status of this cell.
     *
     * @return The cell's status.
     */
    public CellStatus getStatus() {
        return status;
    }
    

    /**
     * Sets the status of this cell and updates its color to reflect the new status.
     *
     * @param status The status to set.
     */
    public void setStatus(CellStatus status) {
        this.status = status;
        updateColor();
    }
}
