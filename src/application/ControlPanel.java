package application;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class ControlPanel {
    private Button resetButton;
    private Button randomButton;
    private ChoiceBox dimensionChoiceBox;
    private TextArea historyTextArea;

    public Button getResetButton() {
        return resetButton;
    }

    public void setResetButton(Button resetButton) {
        this.resetButton = resetButton;
    }

    public Button getRandomButton() {
        return randomButton;
    }

    public void setRandomButton(Button randomButton) {
        this.randomButton = randomButton;
    }

    public TextArea getHistoryTextArea() {
        return historyTextArea;
    }

    public void setHistoryTextArea(TextArea historyTextArea) {
        this.historyTextArea = historyTextArea;
    }
}
