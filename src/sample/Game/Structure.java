package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Random;


public class Structure extends Pane implements Serializable {

    static final long serialVersionUID = -6537980111273999319L;

    public static final int TYPE_ANTENNA = 0;
    public static final int TYPE_ADVANCED_ANTENNA = 1;
    public static final int TYPE_SATELLITE = 2;

    static Font consolas;

    static {
        try {
            consolas = Font.loadFont(new FileInputStream(new File("assets/cmdFont.ttf")),13);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final int[] RANGES = {5,8,14};

    private int range;
    private int type;
    private double power = 0.8, px, py;
    private transient Circle emitZone, basicZone;
    private transient Label info, firmware;
    private transient ImageView icon, circle;
    private String name;
    private boolean hasFirmware = false;
    private static Random random = new Random();
    private float efficiency;
    private String iPath;

    /**
     * Allocates a new ImageView object using the given image.
     *
     * @param image Image that this ImageView uses
     */
    public Structure(Image image, int type, double x, double y, String imagePath) {
        iPath = imagePath;

        this.type = type;
        px = x;
        py = y;
        setNetworkRange((int)(Main.screenHeight()/100)*RANGES[type]);

        name = (random.nextInt(90)+10)+"-"+(random.nextInt(9)+1)+getRandomChar(random);

        refresh();

        measureEfficiency();











    }

    public void refresh() {

        Color emitColor = Color.ORANGE;
        emitZone = new Circle(px,py,1, new Color(emitColor.getRed(), emitColor.getGreen(),
                emitColor.getBlue(), power));
        basicZone = new Circle(px,py,range, new Color(Color.SKYBLUE.getRed(), Color.SKYBLUE.getGreen(),
                Color.SKYBLUE.getBlue(), 0));
        basicZone.setStroke(new Color(1,1,1,0.3));
        getChildren().addAll(emitZone);

        try {
            icon = new ImageView(new Image(new FileInputStream(new File(iPath))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        icon.setX(px-24);
        icon.setY(py-24);
        getChildren().add(icon);

        if(hasFirmware) {
            addFirmware();
        }
        else {
            firmware = new Label();
            firmware.setTextFill(new Color(1,0.25,0.25,1));
            firmware.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
            firmware.setFont(new Font("Calibri Bold", 14));
            firmware.setText("Нет прошивки");
            firmware.setLayoutX(icon.getX()-21);
            firmware.setLayoutY(icon.getY()+60);

            getChildren().add(firmware);
        }

        info = new Label();
        info.setText(name);
        info.setFont(consolas);
        info.setLayoutX(icon.getX()+4);
        info.setLayoutY(icon.getY()+46);

        getChildren().add(info);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if(hasFirmware) {
                emit();
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private void measureEfficiency(){
        float points = 5000;
        float onLand = 0;
        for (int i = 0; i < points; i++) {
            int rx, ry;
            do{
                int sign = Math.random() >= 0.5 ? 1 : -1;
                rx = (int)(emitZone.getCenterX() + (random.nextDouble()*sign)*emitZone.getRadius());
                sign = Math.random() >= 0.5 ? 1 : -1;
                ry = (int)(emitZone.getCenterY() + (random.nextDouble()*sign)*emitZone.getRadius());
            } while(Math.hypot(Math.abs(rx-emitZone.getCenterX()), Math.abs(ry-emitZone.getCenterY())) > emitZone.getRadius());

            if(DDoSMap.isLand(rx,ry)) {
                onLand ++;
            }
        }
        efficiency = onLand/points;
    }

    public void tick() {
        double r = Math.random();
        double t = type;
        if(r <= 0.11*efficiency*(t+1)) {
            PlayerState.setInternetSpeed(PlayerState.getInternetSpeed() + PlayerState.internetSpeedModifier);
            CountersHandler.
                    speed.
                    updateValue(
                            PlayerState.getInternetSpeed());
        }

    }

    public int getType() {
        return type;
    }

    public void emit(){
        emitZone.setOpacity(power);
        emitZone.setRadius(1);
        final float CYCLE_COUNT = 100;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            double p = power;
            double r = range;

            double opacityModifier = p/CYCLE_COUNT;
            emitZone.setOpacity((emitZone.getOpacity()-opacityModifier));
            emitZone.setRadius(emitZone.getRadius()+r/CYCLE_COUNT);
        }));
        timeline.setCycleCount((int)CYCLE_COUNT);
        timeline.play();

        tick();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetworkRange(){
        return range;
    }
    public void setNetworkRange(int range){
        this.range = range;
    }
    private static String getRandomChar(Random random){
        String chars = "AABCCDEEFFGHIJKLMNOOPQRSTUVWXYZ";
        return chars.charAt(random.nextInt(chars.length()))+"";
    }

    public void addFirmware() {
        hasFirmware = true;

        try {
            circle = new ImageView(new Image(new FileInputStream(new File("assets/circle.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //circle.setX(icon.getX()+24-range);
        //circle.setY(icon.getY()+24-range);
        circle.setFitWidth(1);
        circle.setFitHeight(1);
        circle.setOpacity(0.3);
        getChildren().add(circle);
        getChildren().remove(firmware);

        // Animate circle
        final float targetSize = range*2;

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), event -> {
            circle.setX(icon.getX()+24-circle.getFitWidth()/2);
            circle.setY(icon.getY()+24-circle.getFitWidth()/2);
            circle.setFitWidth(circle.getFitWidth()+1);
            circle.setFitHeight(circle.getFitWidth()+1);
        }));
        timeline.setCycleCount((int)targetSize);
        timeline.play();




    }
}
