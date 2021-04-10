package sample.Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sun.security.krb5.internal.crypto.Des;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AppPanelManager extends GridPane {
    private int order = 0;
    public static final int ICON_WIDTH = 44;
    public static final int GLOBAL_OFFSET = 80;
    private Desktop desktop;

    public AppPanelManager(Desktop desktop) {
        this.desktop = desktop;
    }

    public void addIcon(String path, int applicationId) {
        try {
            AppIcon icon = new AppIcon(new ImageView(new Image(new FileInputStream(path))),applicationId,desktop);
            add(icon,order++,0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public double newIconRequestX() {
        return GLOBAL_OFFSET+(order++)*ICON_WIDTH;
    }

}
