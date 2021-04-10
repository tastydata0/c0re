package sample.Game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Mode;


public class Game implements Mode {
    private Scene scene;
    private Desktop desktop;

    public Game() {

        desktop = new Desktop();
        scene = new Scene(desktop);
        PlayerState.setIp(IpGenerator.generateRandomIp());
        PlayerState.setMac(MacGenerator.getRandomMac());
        PlayerState.setLocalIp(IpGenerator.generateRandomLocalIp());
    }

    @Override
    public void set(Stage primaryStage) {
        scene.getStylesheets().add((getClass().getResource("dark.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
    }
}
