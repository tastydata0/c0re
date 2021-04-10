package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Cmd extends App {
    VBox lines = new VBox();
    private ScrollPane scrollPane;
    ArrayList<CmdLine> linesContent = new ArrayList<>();
    private boolean busy = false;
    GridPane status;
    Rectangle statusBackground;
    Font raw = null;
    Font bold = null;
    Window window;
    Process currentProcess;
    String currentDirectory = "/root/";

    public Cmd(Window window) {
        getStylesheets().add((getClass().getResource("dark.css")).toExternalForm());
        this.window = window;

        window.socketStage.setOpacity(0);
        window.headerAndBorders.setEffect(null);
        scrollPane  = new ScrollPane(lines);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefSize(700,380);
        scrollPane.setOnMousePressed(event -> {
            if(linesContent.get(linesContent.size()-1).line.getChildren().contains(linesContent.get(linesContent.size()-1).input)) {
                linesContent.get(linesContent.size()-1).input.requestFocus();
            }
            else {
                linesContent.get(linesContent.size() - 1).folder.requestFocus();
            }
        });
        scrollPane.setOpacity(0.8);
        scrollPane.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-accent: transparent; -fx-shadow-highlight-color: transparent; -fx-background:transparent; -fx-border-color:transparent; -fx-text-box-border: transparent; -fx-focus-color: transparent; -fx-border-width: 0; -fx-highlight-fill: transparent; -fx-control-inner-background: transparent ; -fx-border-color: transparent ; -fx-border-width: 0px ; -fx-text-fill: white ;");

        lines.setMaxWidth(700);
        lines.setMaxHeight(380);

        getChildren().add(scrollPane);

        try {
            raw = Font.loadFont(new FileInputStream(new File("assets/cmdFont.ttf")),17);
            bold = Font.font(raw.getFamily(), FontWeight.BOLD, raw.getSize());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        lines.setLayoutY(5);
        lines.setSpacing(7);
        linesContent.add(new CmdLine(this,null,Color.WHITE));
        if(linesContent.size() > 5) {
            linesContent.remove(0);
            linesContent.trimToSize();
        }
        statusBackground = new Rectangle(window.getPrefWidth(),20);
        statusBackground.setFill(Color.BLACK);
        statusBackground.setOpacity(0.8);
        statusBackground.setTranslateY(380);
        getChildren().add(statusBackground);

    }

    public void setStatus(Text[] texts) {
        getChildren().remove(status);

        status = new GridPane();
        for (int i = 0; i < texts.length; i++) {
            status.add(texts[i],i,0);
        }
        status.setTranslateY(382);
        getChildren().add(status);
    }

    private void createProcess(String input) {
        currentProcess = new Process(this,input);
    }
    public void processFinish(Process process) {
        while (process.isAlive()) {

        }
        busy = false;
        linesContent.add(new CmdLine(this,null,Color.WHITE));
    }

    public void append(String text) {
        linesContent.get(
                linesContent.size()-2).
                msg.setText(
                        linesContent.get(linesContent.size()-2).
                                msg.getText()+text);
    }

    public void output(String message, Color color) {
        getChildren().remove(status);
        linesContent.add(new CmdLine(this,message,color));
        if(linesContent.size()>5) {
            linesContent.remove(0);
            linesContent.trimToSize();
        }
    }
    public void output(String message, Color color, int index) {
        linesContent.add(index, new CmdLine(this,message,color));
        if(linesContent.size()>5) {
            linesContent.remove(0);
            linesContent.trimToSize();
        }
    }

    class CmdLine {
        private Cmd cmd;
        private HBox line = new HBox();
        TextField input = new TextField();
        Text folder = new Text(":~"+currentDirectory.replaceFirst("/root",""));
        Text hash = new Text("#");
        Text msg;
        String message;
        public CmdLine(Cmd cmd, String message,Color color) {
            line.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
            this.message = message;
            lines.getChildren().add(line);
            this.cmd = cmd;
            if (message == null) {
                Text root = new Text("root@"+System.getProperty("user.name").toLowerCase());
                root.setFont(bold);
                root.setFill(Color.web("#FF0000"));

                folder.setFont(bold);
                folder.setFill(Color.SKYBLUE);
                hash.setFont(bold);
                hash.setFill(Color.WHITE);
                input.setTranslateY(-9);
                input.setTranslateX(-2);
                input.setFont(bold);
                input.setMaxHeight(10);
                input.setPrefWidth(485);
                input.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: transparent; -fx-accent: transparent; -fx-shadow-highlight-color: transparent; -fx-background:transparent; -fx-border-color:transparent; -fx-text-box-border: transparent; -fx-focus-color: transparent; -fx-border-width: 0; -fx-highlight-fill: transparent; -fx-control-inner-background: transparent ; -fx-border-color: transparent ; -fx-border-width: 0px ; -fx-text-fill: white ;");

                //("-fx-text-box-border: transparent; -fx-focus-color: transparent; -fx-border-color: transparent; -fx-border-width: 0; -fx-highlight-fill: transparent; -fx-control-inner-background: transparent ; -fx-border-color: black ; -fx-border-width: 0px ; -fx-text-fill: white ;");
                input.setOnKeyPressed(event -> {
                    if(!event.getText().equals("") && (int)event.getText().charAt(0) == 13) {
                        createProcess(input.getText());
                        accept();
                        input.requestFocus();
                        input.setText("");
                        line.getChildren().remove(input);
                        busy = true;
                    }
                });
                input.setFocusTraversable(true);
                line.getChildren().addAll(root,folder,hash,input);
                input.setFocusTraversable(true);
            }
            else {
                msg = new Text(message);
                msg.setFont(bold);
                msg.setFill(color);
                WebView browser = new WebView();
                WebEngine engine = browser.getEngine();
                engine.loadContent("<html> " +
                        "<body bgcolor = black> " +
                        "<style>\n" +
                        "  code {\n" +
                        "    font-family: Consolas;\n" +

                        "    color: white;\n" +
                        "   }"+
                        "</style>\n" +
                        " <code style=\"color:#FFFFFF\"; \"font-size: 200px; ><strong>"+message+"</strong></code> " +
                        "</body> " +
                        "</html>");
                browser.setTranslateY(-5);
                browser.setMaxHeight(35);
                line.getChildren().add(msg);
                input.setFocusTraversable(true);
            }

        }
        public void accept() {
            if(message == null) {
                Text lastInput = new Text(" "+input.getText());
                lastInput.setFont(bold);
                lastInput.setFill(Color.WHITE);
                line.getChildren().remove(input);
                line.getChildren().add(lastInput);
            }
        }
    }

    @Override
    public void close() {
        super.close();
        currentProcess = null;
    }
}
