package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.*;

public class IDE extends App {
    private Window window;
    private TextField input;
    private Label command;
    private VBox ide = new VBox();
    private ScrollPane scrollPane = new ScrollPane();
    private HBox buttonBox = new HBox();
    private TextArea codeArea = new TextArea();
    private String code[];
    private int codeViewLine = 0;

    public IDE(Window window) {
        this.window = window;

        try {
            ImageView background = new ImageView(new Image(new FileInputStream("assets/ide.jpg")));
            background.setFitWidth(700);
            background.setFitHeight(394);
            getChildren().add(background);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            code = readFile("assets/code.txt").split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Development process



        codeArea.setStyle("-fx-faint-focus-color: black; -fx-focus-color: black; -fx-accent: transparent; -fx-shadow-highlight-color: transparent; -fx-background:transparent; -fx-border-color:transparent; -fx-text-box-border: transparent; -fx-focus-color: black; -fx-border-width: 0; -fx-highlight-fill: black; -fx-control-inner-background: transparent ; -fx-border-color: transparent ; -fx-border-width: 0px ; -fx-text-fill: #00FF00FF ;");
        scrollPane.setContent(codeArea);
        scrollPane.setPrefViewportWidth(700);
        codeArea.setPrefWidth(700);
        codeArea.setPrefRowCount(8);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ide.getChildren().add(scrollPane);

        HBox labels = new HBox();
        Label toWrite = new Label("write > ");
        command = new Label("");
        command.setFont(new Font("Consolas", 16));
        command.setTextFill(new Color(0,1,0,1));
        labels.getChildren().addAll(toWrite, command);
        ide.getChildren().add(labels);

        input = new TextField();
        input.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(getChildren().contains(ide)) {
                    input.requestFocus();
                }
            }
        });
        input.setStyle("-fx-faint-focus-color: black; -fx-focus-color: black; -fx-accent: transparent; -fx-shadow-highlight-color: transparent; -fx-background:transparent; -fx-border-color:transparent; -fx-text-box-border: transparent; -fx-focus-color: black; -fx-border-width: 0; -fx-highlight-fill: black; -fx-control-inner-background: transparent ; -fx-border-color: transparent ; -fx-border-width: 0px ; -fx-text-fill: white ;");
        ide.getChildren().add(input);

        // Buttons
        Button easyButton = new Button("Создать прошивку \nдля антенны");
        Button mediumButton = new Button("Создать прошивку \nдля улучшенной антенны");
        Button hardButton = new Button("Создать прошивку \nдля спутника");


        {
            easyButton.setPrefHeight(200);
            easyButton.setPrefWidth(200);
            try {
                easyButton.setGraphic(new ImageView(new Image(new FileInputStream("assets/antenna.png"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mediumButton.setPrefHeight(200);
            mediumButton.setPrefWidth(200);
            try {
                mediumButton.setGraphic(new ImageView(new Image(new FileInputStream("assets/advancedAntenna.png"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            hardButton.setPrefHeight(200);
            hardButton.setPrefWidth(200);
            try {
                hardButton.setGraphic(new ImageView(new Image(new FileInputStream("assets/satellite.png"))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            buttonBox.getChildren().addAll(easyButton, mediumButton, hardButton);
            buttonBox.setLayoutX(50);
            buttonBox.setLayoutY(50);
            getChildren().add(buttonBox);
        }

        easyButton.setOnMouseClicked(event -> {
            getChildren().remove(buttonBox);
            getChildren().add(ide);
            start(0);
        });
        mediumButton.setOnMouseClicked(event -> {
            getChildren().remove(buttonBox);
            getChildren().add(ide);
            start(1);
        });
        hardButton.setOnMouseClicked(event -> {
            getChildren().remove(buttonBox);
            getChildren().add(ide);
            start(2);
        });


    }

    private String expectedCommand;
    private int commands = -1;

    private void end() {
        codeViewLine = 0;
        getChildren().remove(ide);
        getChildren().add(buttonBox);

        Timeline popupTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            window.desktop.getChildren().add(new Popup("Отлично. Поршивка написана.\n" +
                    "Пора загрузить её в антенну.\n" +
                    "Используйте cmd (1 иконка панели задач), чтобы\n" +
                    "сделать это. Команда help покажет все команды.\n" +
                    "Для удобства можно закрыть IDE.", window.desktop, 3));
        }));
        popupTimeline.setCycleCount(1);
        popupTimeline.play();
    }

    private void addPieceOfCode() {
        codeArea.clear();
        for (int i = codeViewLine; i < codeViewLine+8; i++) {
            codeArea.setText(codeArea.getText()+code[i]+"\n");
        }
        codeViewLine+=8;
    }

    private void start(int difficulty) {
        input.requestFocus();
        nextWord(difficulty);
        input.setOnKeyReleased(event -> {
            if(input.getText().trim().equals(expectedCommand.trim())) {
                input.clear();
                nextWord(difficulty);
            }
        });


    }
    private void nextWord(int difficulty) {
        commands++;
        if(commands>0) {
            addPieceOfCode();
        }
        if(commands >= 6+difficulty*2) {
            commands = -1;
            // Coding completed
            DevelopmentHandler.getInstance().addProgram(difficulty);
            String purpose = "";
            switch (difficulty) {
                case 0:
                    purpose = "антенны";
                    break;
                case 1:
                    purpose = "улучшенной антенны";
                    break;
                case 2:
                    purpose = "спутника";
                    break;
            }
            window.desktop.getLog().add("Прошивка для "+purpose+" создана!", Color.GREENYELLOW);
            end();
            return;
        }
        DevelopmentHandler developmentHandler = DevelopmentHandler.getInstance();
        expectedCommand = developmentHandler.getWord(difficulty);
        command.setText(expectedCommand);
    }

    private String getExpectedCommand() {
        return expectedCommand;
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
