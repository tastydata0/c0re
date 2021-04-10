package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.Main;


public class Log extends GridPane {
    private int order;

    public Log() {
        Timeline opacityDecrease = new Timeline(new KeyFrame(Duration.millis(200),event -> {
            try {
                for (Node label : getChildren()) {
                    label.setOpacity(label.getOpacity() - 0.0075);
                }
            } catch (Exception e) {

            }
        }));
        opacityDecrease.setCycleCount(Timeline.INDEFINITE);
        opacityDecrease.play();

    }
    public void add(String note,Color color) {
        Label newNote = new Label("<Log> "+note);
        newNote.setTextFill(color);
        newNote.heightProperty().addListener((obs, oldVal, newVal) -> {
            setLayoutY(getLayoutY()-newVal.doubleValue());
        });
        newNote.setFont(new Font("Consolas",20));
        newNote.setMaxWidth(Main.screenWidth()/5);
        newNote.setWrapText(true);

        add(newNote,0,order++);

    }
}
