package sample.Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CountersHandler {
    public static Counter energy, speed;

    public CountersHandler(Desktop desktop) {
        try {
            speed = new InternetSpeedCounter(new ImageView(new Image(new FileInputStream(new File("assets/speed.png")))),
                    "Интернет:", Color.WHITE, desktop);
            speed.updateValue(PlayerState.getInternetSpeed());
            speed.setLayoutX(20);
            speed.setLayoutX(20);

            desktop.getChildren().add(speed);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
