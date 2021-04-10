package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Game.DDoSMap;
import sample.Game.Desktop;
import sample.Game.Game;
import sample.Graphics.Colors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class MainMenu implements Mode {
    private Scene scene;
    private double scaleX = (Main.screenWidth()/1920);
    private double scaleY = (Main.screenHeight()/1080);
    private Pane mainPane = new Pane();
    private Label gameLogo, play, settings, exit, codeExample;

    public MainMenu() {
        System.out.println(Color.SKYBLUE.toString());
        scene = new Scene(mainPane);
        try {
            ImageView bg = new ImageView(new Image(new FileInputStream("assets/mainMenuBG.jpg")));
            bg.setFitWidth(Main.screenWidth());
            bg.setFitHeight(Main.screenHeight());
            mainPane.getChildren().add(bg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mainPane.setStyle("-fx-background-color: " + Colors.MAIN_COLOR + ";");
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(700), event -> {
            mainPane.getChildren().add(new Cube(mainPane).instance);
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        gameLogo = new Label("c0re");
        try {
            gameLogo.setFont(Font.loadFont(new FileInputStream(new File("assets/12157.ttf")), 200*scaleX));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameLogo.setTextFill(Color.web(Colors.FONT_COLOR));
        gameLogo.setLayoutX(400*scaleX);
        gameLogo.setLayoutY(170*scaleY);
        mainPane.getChildren().add(gameLogo);

        play = new Label(" > Играть");
        try {
            play.setFont(Font.loadFont(new FileInputStream(new File("assets/consola.ttf")), 62*scaleX));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        play.setTextFill(Color.web(Colors.FONT_COLOR));
        play.setLayoutX(925*scaleX);
        play.setLayoutY(410*scaleY);
        play.setOnMouseMoved(event -> {
            play.setTextFill(Color.web(Colors.FONT_COLOR_ACCENT));
            play.setText("=> Играть");
            codeExample.setText("root@"+System.getProperty("user.name")+" > exploit -start");
        });
        play.setOnMouseExited(event -> {
            play.setTextFill(Color.web(Colors.FONT_COLOR));
            play.setText(" > Играть");
            codeExample.setText("root@"+System.getProperty("user.name")+" >");

        });
        play.setOnMouseClicked(event -> {
            new Game().set(Main.getPrimaryStage());
        });
        mainPane.getChildren().add(play);

        settings = new Label(" > Настройки");
        try {
            settings.setFont(Font.loadFont(new FileInputStream(new File("assets/consola.ttf")), 62*scaleX));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        settings.setTextFill(Color.web(Colors.FONT_COLOR));
        settings.setLayoutX(925*scaleX);
        settings.setLayoutY(510*scaleY);
        settings.setOnMouseMoved(event -> {
            settings.setTextFill(Color.web(Colors.FONT_COLOR_ACCENT));
            settings.setText("=> Настройки");
            codeExample.setText("root@"+System.getProperty("user.name")+" > config");
        });
        settings.setOnMouseExited(event -> {
            settings.setTextFill(Color.web(Colors.FONT_COLOR));
            settings.setText(" > Настройки");
            codeExample.setText("root@"+System.getProperty("user.name")+" >");

        });
        settings.setOnMouseClicked(event -> {
            // Click handling
        });
        mainPane.getChildren().add(settings);

        exit = new Label(" > Выход");
        try {
            exit.setFont(Font.loadFont(new FileInputStream(new File("assets/consola.ttf")), 62*scaleX));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        exit.setTextFill(Color.web(Colors.FONT_COLOR));
        exit.setLayoutX(925*scaleX);
        exit.setLayoutY(610*scaleY);
        exit.setOnMouseMoved(event -> {
            exit.setTextFill(Color.web(Colors.FONT_COLOR_ACCENT));
            exit.setText("=> Выход");
            codeExample.setText("root@"+System.getProperty("user.name")+" > shutdown");
        });
        exit.setOnMouseExited(event -> {
            exit.setTextFill(Color.web(Colors.FONT_COLOR));
            exit.setText(" > Выход");
            codeExample.setText("root@"+System.getProperty("user.name")+" >");

        });
        exit.setOnMouseClicked(event -> {
            Platform.exit();
        });
        mainPane.getChildren().add(exit);

        codeExample = new Label("root@"+System.getProperty("user.name")+" >");
        codeExample.setFont(new Font("Consolas", 42*scaleX));
        codeExample.setTextFill(Color.web(Colors.FONT_COLOR_DARK));
        codeExample.setLayoutX(255*scaleX);
        codeExample.setLayoutY(720*scaleY);
        mainPane.getChildren().add(codeExample);


    }

    // Sets main menu mode
    @Override
    public void set(Stage primaryStage) {
        primaryStage.setScene(scene);
    }
}
