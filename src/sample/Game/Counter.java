package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class Counter extends Pane {
    ImageView icon;
    Desktop desktop;
    Label title, valueLabel;
    int value = 0;

    /**
     * Creates a Pane layout.
     */
    public Counter(ImageView icon, String title, Color color, Desktop d) {
        this.icon = icon;
        this.desktop = d;
        this.title = new Label(title);
        this.title.setTextFill(color);
        this.title.setLayoutX(64);
        this.title.setFont(new Font("Calibri bold", 24));

        valueLabel = new Label(value+"");
        valueLabel.setTextFill(color);
        valueLabel.setFont(new Font("Consolas", 16));
        valueLabel.setLayoutX(64);
        valueLabel.setLayoutY(30);

        getChildren().addAll(icon, this.title, valueLabel);
    }
    public synchronized void updateValue(int newValue) {
        final float DELTA = Math.abs(newValue-value);
        final int sign = newValue > value ? 1 : -1;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2), event -> {
            value += sign;
            valueLabel.setText(value+"");
        }));
        timeline.setCycleCount((int)DELTA);
        timeline.play();
    }
}
