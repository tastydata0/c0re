package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.Graphics.Colors;
import sample.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Desktop extends Pane {

    // App id's
    public static final int APP_CONSOLE = 0;
    public static final int APP_BROWSER = 1;
    public static final int APP_IDE = 2;


    public static final int APP_BAR_HEIGHT = 44;
    public static final int HIDE_WINDOWS_BUTTON_WIDTH = 15;
    private Date date = new Date();
    private transient SimpleDateFormat formatForDateNow;
    DDoSMap map;
    private Log log;
    private CountersHandler countersHandler;
    private AppPanelManager appPanelManager;
    private ArrayList<Window> windows = new ArrayList<>();
    private Window mainWindow; // Window above all other windows


    /**
     * Creates a Pane layout.
     */
    public Desktop() {
        map = new DDoSMap();
        Prefs.load(map);

        formatForDateNow = new SimpleDateFormat("  HH:mm");
        appPanelManager = new AppPanelManager(this);
        Rectangle appPanel = new Rectangle();
        appPanel.setFill(Color.web(Colors.SECONDARY_COLOR));
        appPanel.setOpacity(0.8);
        appPanel.setX(0);
        appPanel.setY(Main.screenHeight()-APP_BAR_HEIGHT);
        appPanel.setWidth(Main.screenWidth());
        appPanel.setHeight(APP_BAR_HEIGHT);
        Rectangle hideWindows = new Rectangle(Main.screenWidth()-HIDE_WINDOWS_BUTTON_WIDTH,appPanel.getY(),HIDE_WINDOWS_BUTTON_WIDTH,APP_BAR_HEIGHT);
        hideWindows.setOpacity(0.7);
        hideWindows.setFill(Color.web(Colors.SECONDARY_COLOR_ACCENT));
        hideWindows.setOnMouseMoved(event -> {
            hideWindows.setOpacity(1);
        });
        hideWindows.setOnMouseExited(event -> {
            hideWindows.setOpacity(0.7);
        });
        hideWindows.setOnMouseClicked(event -> {
            //TODO hide all windows
        });
        Label time = new Label(formatForDateNow.format(date));
        time.setFont(new Font("Consolas", 22));
        time.setTextFill(Color.WHITE);
        time.setLayoutX(Main.screenWidth()-HIDE_WINDOWS_BUTTON_WIDTH-120);
        time.setLayoutY(Main.screenHeight()-(double)APP_BAR_HEIGHT/1.25);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            date = new Date();
            time.setText(formatForDateNow.format(date));

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Wallpaper
        try {
            ImageView wallpaper = new ImageView(new Image(new FileInputStream(new File("assets/bg1.jpg"))));
            wallpaper.setFitWidth(Main.screenWidth());
            wallpaper.setFitHeight(Main.screenHeight());
            getChildren().add(wallpaper);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getChildren().add(map);
        getChildren().add(appPanel);
        getChildren().add(hideWindows);
        getChildren().add(time);
        log = new Log();
        log.setLayoutY(Main.screenHeight()-APP_BAR_HEIGHT-30);
        log.setLayoutX(15);
        log.add("Добро пожаловать в c0re!",Color.GRAY);
        getChildren().add(log);

        appPanelManager = new AppPanelManager(this);
        appPanelManager.addIcon("assets/cmd.png",APP_CONSOLE);
        appPanelManager.addIcon("assets/browser.png",APP_BROWSER);
        appPanelManager.addIcon("assets/ide.png",APP_IDE);
        appPanelManager.setLayoutX(100);
        appPanelManager.setLayoutY(Main.screenHeight()-44);
        getChildren().add(appPanelManager);

        countersHandler = new CountersHandler(this);

        Timeline popupTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            getChildren().add(new Popup("Добро пожаловать в ?????\nВы - администратор одного из лучших провайдеров.\n" +
                    "Назовём его SevStar. Цель - покрыть интернетом весь мир.\n", this, 0));
        }));
        popupTimeline.setCycleCount(1);
        popupTimeline.play();

        popupTimeline = new Timeline(new KeyFrame(Duration.seconds(20), event -> {
            getChildren().add(new Popup("Нажмите ПКМ по суше на карте,\n" +
                    "чтобы построить что-либо. Постройте антенну.", this, 1));
        }));
        popupTimeline.setCycleCount(1);
        popupTimeline.play();


    }

    public void launchApplication(int id) {
        switch (id) {
            case APP_CONSOLE:
                Window cmdWindow = new Window(this,"Console",500,500,700,400);
                cmdWindow.initApp(new Cmd(cmdWindow));
                windows.add(cmdWindow);
                getChildren().add(cmdWindow);
                break;
            case APP_BROWSER:
                Window browserWindow = new Window(this,"Browser",500,500,1060,600);
                browserWindow.initApp(new Browser(browserWindow));
                windows.add(browserWindow);
                getChildren().add(browserWindow);
                break;
            case APP_IDE:
                Window ideWindow = new Window(this,"Development",560,560,700,394);
                ideWindow.initApp(new IDE(ideWindow));
                windows.add(ideWindow);
                getChildren().add(ideWindow);
                break;
        }
    }

    public void setMainWindow(Window window) {
        if(mainWindow != window) {
            window.toFront();
            mainWindow = window;
        }
    }

    public Log getLog() {
        return log;
    }

    public DDoSMap getMap() {
        return map;
    }
}
