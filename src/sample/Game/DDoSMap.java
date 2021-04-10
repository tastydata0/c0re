package sample.Game;

import com.sun.javafx.geom.Vec2d;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Random;

public class DDoSMap extends Pane {

    static Image image,point;
    private double lastClickX, lastClickY;
    static ContextMenu contextMenu = new ContextMenu();
    ImageView mapPoint;
    /**
     * Creates a Pane layout.
     */
    public DDoSMap() {
        try {
            image = new Image(new FileInputStream(new File("assets/DDoSMap.png")));
            ImageView view = new ImageView(image);
            view.setFitWidth(Main.screenWidth());
            view.setFitHeight(Main.screenHeight());


            point = new Image(new FileInputStream(new File("assets/mapPoint.png")));
            mapPoint = new ImageView(point);
            getChildren().add(view);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MenuItem antenna = new MenuItem("Построить антенну");
        antenna.setOnAction(event -> {
            try {
                Structure structure = new Structure(new Image(new FileInputStream(new File("assets/antenna.png"))),
                        Structure.TYPE_ANTENNA, lastClickX, lastClickY,"assets/antenna.png");
                getChildren().add(structure);
                StructureHandler.getInstance().registerStructure(structure);
                Timeline popupTimeline = new Timeline(new KeyFrame(Duration.seconds(4), event1 -> {
                    getChildren().add(new Popup("Антенна построена, но ей нужна прошивка.\n" +
                            "Напишите её, используя IDE (3 иконка панели задач)\n" +
                            "В процессе написания кода нужно вводить запрошенные слова.", this, 2));
                }));
                popupTimeline.setCycleCount(1);
                popupTimeline.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });


        // Add MenuItem to ContextMenu
        contextMenu.getItems().add(antenna);
        contextMenu.setStyle("-fx-faint-focus-color: white; -fx-focus-color: white; -fx-accent: transparent; -fx-shadow-highlight-color: transparent; -fx-background:transparent; -fx-border-color:transparent; -fx-text-box-border: transparent; -fx-focus-color: white; -fx-border-width: 0; -fx-highlight-fill: white; -fx-control-inner-background: transparent ; -fx-border-color: transparent ; -fx-border-width: 0px ; -fx-text-fill: white ;");

        // Context menu call
        setOnMouseClicked(event -> {
            if(isLand(event.getScreenX(),event.getScreenY())){
                if(event.getButton().toString().equals("SECONDARY")){
                    contextMenu.show(this,event.getScreenX(),event.getScreenY());
                    lastClickX = event.getScreenX();
                    lastClickY = event.getScreenY();
                }
            }
        });



    }

    public void addAdvancedAntennaSuggestion() {
        MenuItem advancedAntenna = new MenuItem("Построить улучшенную антенну");
        advancedAntenna.setOnAction(event -> {
            // Create antenna
            try {
                Structure structure = new Structure(new Image(new FileInputStream(new File("assets/advancedAntenna.png"))),
                        Structure.TYPE_ADVANCED_ANTENNA, contextMenu.getX(), contextMenu.getY(),"assets/advancedAntenna.png");
                getChildren().add(structure);
                StructureHandler.getInstance().registerStructure(structure);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        contextMenu.getItems().add(advancedAntenna);

    }

    public void addSatelliteSuggestion() {
        MenuItem satellite = new MenuItem("Запустить спутник");
        satellite.setOnAction(event -> {
            // Create satellite
            try {
                Structure structure = new Structure(new Image(new FileInputStream(new File("assets/satellite.png"))),
                        Structure.TYPE_SATELLITE, contextMenu.getX(), contextMenu.getY(),"assets/satellite.png");
                getChildren().add(structure);
                StructureHandler.getInstance().registerStructure(structure);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        contextMenu.getItems().add(satellite);
    }

    public void addPoint() {
        ImageView newPoint = new ImageView(point);
        Vec2d vec2d = getRandomLandPoint();
        newPoint.setX(vec2d.x-32);
        newPoint.setY(vec2d.y-64);
        getChildren().add(newPoint);
    }

    public static boolean isLand(double x, double y){
        return image.getPixelReader().getColor((int)(x/(Main.screenWidth()/1920)),(int)(y/(Main.screenHeight()/1080))).getBlue() > 0.01;
    }

    public Vec2d getRandomLandPoint() {
        int x,y;
        Random random = new Random();
        do {
            x = random.nextInt(((int) Main.screenWidth()));
            y = random.nextInt(((int) Main.screenHeight()));
        } while (image.getPixelReader().getColor(x,y).getBlue() < 0.01);
        return new Vec2d(x,y);
    }


}
