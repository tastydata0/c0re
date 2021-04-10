package sample.Game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.text.Element;

public class AppIcon extends Pane {
    private int applicationId;


    /**
     * Creates a Pane layout.
     */
    public AppIcon(ImageView icon, int applicationId,Desktop desktop) {
        this.applicationId = applicationId;
        setPrefSize(50,44);
        Rectangle accent = new Rectangle(40,44);
        accent.setFill(Color.WHITE);
        accent.setOpacity(0);
        accent.setOnMouseEntered(event -> {
            accent.setOpacity(0.4);
        });
        accent.setOnMouseExited(event -> {
            accent.setOpacity(0);
        });
        accent.setOnMouseClicked(event -> {
            launchApp(desktop,applicationId);
        });

        icon.setLayoutX(2);
        accent.setLayoutX(2);
        getChildren().addAll(icon,accent);
    }

    private void launchApp(Desktop desktop, int applicationId) {
        desktop.launchApplication(applicationId);
    }
}
