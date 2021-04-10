package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Game.Game;

import java.awt.*;


public class Main extends Application {

    private BorderPane mainPane = new BorderPane();
    private static Stage primaryStage1;
    private static double width, height;

    static {
        Dimension sSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        width = sSize.width;
        height = sSize.height;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage1 = primaryStage;
        primaryStage.setTitle("c0re");
        primaryStage.setWidth(screenWidth());
        primaryStage.setHeight(screenHeight());
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        new MainMenu().set(primaryStage);
        primaryStage.setFullScreenExitHint("");

        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage1;
    }

    public static double screenWidth() {
        return width;
    }
    public static double screenHeight() {
        return height;
    }
}
