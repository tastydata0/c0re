package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.Graphics.Colors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Window extends Pane {
    private App socket;
    private Label title;
    public static final int HEADER_HEIGHT = 36;
    public static final int BORDERS_WIDTH = 4;
    public static int zIndex = 1;
    double xOffset,yOffset;
    Desktop desktop;
    Rectangle closeAccent;
    ImageView closeWindow;
    Rectangle headerAndBorders, socketStage;

    public Window(Desktop desktop,String title, double spawnX, double spawnY, double width, double height) {
        this.desktop = desktop;

        setPrefWidth(width);
        setHeight(height);

        // Appearance of window
        headerAndBorders = createShadowedBox(width, HEADER_HEIGHT,6, 6,6,6);
        headerAndBorders.setWidth(width);
        headerAndBorders.setHeight(HEADER_HEIGHT);
        headerAndBorders.setFill(Color.LIGHTGRAY);
        headerAndBorders.setOnMousePressed(event -> {
            xOffset = event.getScreenX()-getLayoutX();
            yOffset = event.getScreenY()-getLayoutY();
            desktop.setMainWindow(this);
        });
        headerAndBorders.setOnMouseDragged(event -> {
            relocate(event.getScreenX()-xOffset,event.getScreenY()-yOffset);
            desktop.setMainWindow(this);
        });

        headerAndBorders.setLayoutY((HEADER_HEIGHT)*-1+BORDERS_WIDTH/2-2);

        socketStage = createShadowedBox(width,height,6,6,6,6);
        socketStage.setWidth(width);
        socketStage.setHeight(height);
        socketStage.setFill(Color.BLACK);

        // Close button

        try {
            closeAccent = new Rectangle(36,36);
            closeWindow = new ImageView(new Image(new FileInputStream("assets/close.png")));
            closeAccent.setOpacity(0);
            closeAccent.setFill(Color.BLACK);
            closeAccent.setOnMouseClicked(event -> {
                close();
            });
            closeAccent.setOnMouseEntered(event -> {
                closeAccent.setOpacity(0.3);
            });
            closeAccent.setOnMouseExited(event -> {
                closeAccent.setOpacity(0);
            });
            closeAccent.setLayoutX(width-8-28);
            closeAccent.setTranslateY(-36);
            closeWindow.setLayoutX(width-8-20);
            closeWindow.setTranslateY(-28);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        this.title = new Label();
        this.title.setTextFill(Color.BLACK);
        this.title.heightProperty().addListener((obs, oldVal, newVal) -> {
            this.title.setLayoutY(-28);
        });
        this.title.setLayoutX(20);
        this.title.setFont(new Font("Consolas",22));
        this.title.setText(title);
        this.title.setOnMouseDragged(event -> {
            relocate(event.getScreenX()-xOffset,event.getScreenY()-yOffset);
            desktop.setMainWindow(this);
        });
        this.title.setOnMousePressed(event -> {
            xOffset = event.getScreenX()-getLayoutX();
            yOffset = event.getScreenY()-getLayoutY();
            desktop.setMainWindow(this);
        });

        relocate(spawnX,spawnY);
        getChildren().add(headerAndBorders);
        getChildren().add(socketStage);
        getChildren().add(this.title);
        getChildren().addAll(closeWindow,closeAccent);
    }

    public void initApp(App socket) {
        this.socket = socket;
        socket.setPrefWidth(socketStage.getWidth()-BORDERS_WIDTH);
        socket.setPrefHeight(socketStage.getHeight());
        getChildren().add(socket);
    }

    public void close() {
        socket.close();
        desktop.getChildren().remove(this);
    }

    Rectangle createShadowedBox(double size,
                                double shadowWidth, double shadowHeight,
                                double offsetX, double offsetY,
                                double radius)
    {
        Rectangle r = new Rectangle(size, size);
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

    /**
     * Sets the node's layoutX and layoutY translation properties in order to
     * relocate this node to the x,y location in the parent.
     * <p>
     * This method does not alter translateX or translateY, which if also set
     * will be added to layoutX and layoutY, adjusting the final location by
     * corresponding amounts.
     *
     * @param x the target x coordinate location
     * @param y the target y coordinate location
     */
    @Override
    public void relocate(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
