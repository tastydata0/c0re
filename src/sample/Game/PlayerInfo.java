package sample.Game;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class PlayerInfo extends Pane {
    static Font consolas;
    static Font consolasSmall;
    private static Rectangle xpFill = new Rectangle();
    private static Label xp = new Label("Level "+PlayerState.getLevel()+" | "+PlayerState.getCurrentXp()+" / "+PlayerState.getXpToNextLevel());

    static {
        try {
            consolas = Font.loadFont(new FileInputStream(new File("assets/cmdFont.ttf")),19);
            consolasSmall = Font.loadFont(new FileInputStream(new File("assets/cmdFont.ttf")),13);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creates a Pane layout.
     */
    public PlayerInfo() {
        Rectangle background = new Rectangle(256,68);
        background.setFill(Color.WHITE);
        background.setOpacity(0.2);
        background.setLayoutX(10);
        background.setLayoutY(10);
        getChildren().add(background);
        try {
            ImageView avatar = new ImageView(new Image(new FileInputStream("assets/player0.jpg")));
            avatar.setFitWidth(64);
            avatar.setFitHeight(64);
            avatar.setLayoutX(12);
            avatar.setLayoutY(12);
            getChildren().add(avatar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Label username = new Label(PlayerState.getUsername());
        username.setFont(consolas);
        username.setLayoutX(64 + 20);
        username.setLayoutY(15);
        getChildren().add(username);

        xp.setFont(consolasSmall);
        xp.setLayoutX(64 + 17);
        xp.setLayoutY(46);
        getChildren().add(xp);

        Rectangle xpBar = new Rectangle(180,5);
        xpBar.setFill(Color.GRAY);
        xpBar.setLayoutX(64 + 16);
        xpBar.setLayoutY(68);
        getChildren().add(xpBar);

        updateXpBar();
        xpFill.setHeight(5);
        xpFill.setFill(Color.SKYBLUE);
        xpFill.setLayoutX(64 + 16);
        xpFill.setLayoutY(68);
        getChildren().add(xpFill);

    }

    public static void updateXpBar() {
        xpFill.setWidth((double)PlayerState.getCurrentXp()%1000 / ((double)PlayerState.getXpToNextLevel()/PlayerState.getLevel()) * 180);
        xp.setText("Level "+PlayerState.getLevel()+" | "+PlayerState.getCurrentXp()+" / "+PlayerState.getXpToNextLevel());
    }
}
