package sample.Game;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import sample.Graphics.Colors;
import sample.Main;

import java.util.ArrayList;

public class Popup extends Pane {
    private static ArrayList<Integer> alreadyShownIDs = new ArrayList<Integer>();
    /**
     * Creates a Pane layout.
     */
    public Popup(String message, Pane parent, int id) {
        if(!alreadyShownIDs.contains(id) && !PlayerState.POPUPS_DISABLED) {
            alreadyShownIDs.add(id);
            int sx = 500, sy = 300;
            Rectangle r = createShadowedBox(sx, sy, 6, 6, 6, 6, 6);
            r.setFill(new Color(0.12, 0.12, 0.15, 0.7));
            getChildren().add(r);
            setLayoutX(Main.screenWidth() / 2 - sx / 2);
            setLayoutY(Main.screenHeight() / 2 - sy / 2);
            Label messageLabel = new Label(message);
            messageLabel.setFont(new Font("Consolas", 14));
            BorderPane bp = new BorderPane();
            messageLabel.setTextFill(Color.WHITE);
            bp.setCenter(messageLabel);
            bp.setPrefWidth(sx);
            bp.setPrefHeight(sy);
            getChildren().add(bp);

            Button ok = new Button("Ок");
            ok.setPrefWidth(60);
            ok.setLayoutX(sx - 100);
            ok.setLayoutY(sy - 50);
            getChildren().add(ok);

            Label idLabel = new Label("ID: "+id);
            idLabel.setTextFill(new Color(0.5,0.5,0.5,0.7));
            bp.setBottom(idLabel);

            ok.setOnMouseClicked(event -> parent.getChildren().remove(this));
        }
    }

    Rectangle createShadowedBox(double sizeX, double sizeY,
                                double shadowWidth, double shadowHeight,
                                double offsetX, double offsetY,
                                double radius)
    {
        Rectangle r = new Rectangle(sizeX, sizeY);
        r.setFill(Color.LIGHTGRAY);
        r.setStroke(Color.BLACK);
        r.setStrokeWidth(2);
        DropShadow e = new DropShadow();
        e.setWidth(shadowWidth);
        e.setHeight(shadowHeight);
        e.setOffsetX(offsetX);
        e.setOffsetY(offsetY);
        e.setRadius(radius);
        r.setEffect(e);
        return r;
    }
}
