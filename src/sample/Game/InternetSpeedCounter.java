package sample.Game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class InternetSpeedCounter extends Counter {
    private static int r1 = 128,
            r2 = 256;

    public InternetSpeedCounter(ImageView icon,
                                String title,
                                Color color,
                                Desktop d) {
        super(icon, title, color, d);
    }

    @Override
    public synchronized void updateValue(int newValue) {
        if(value < r1 && newValue >= r1) {
            desktop.map.addAdvancedAntennaSuggestion();
            desktop.getChildren().add(new Popup("Скорость Вашего интернета превысила "+r1+" Мбит/c!\n" +
                    "Теперь для постройки доступна улучшенная антенна.\n" +
                    "Для неё нужна более сложная прошивка, поэтому\n" +
                    "в среде разработки нужно выбирать 2 пункт.",desktop,5));
        }
        if(value < r2 && newValue >= r2) {
            desktop.map.addSatelliteSuggestion();
            desktop.getChildren().add(new Popup("Скорость Вашего интернета превысила "+r2+" Мбит/c!\n" +
                    "Теперь для запуска доступен спутник.\n" +
                    "Для него нужна ещё более сложная прошивка, поэтому\n" +
                    "в среде разработки нужно выбирать 3 пункт.",desktop,1999999999));
        }
        value = newValue;
        valueLabel.setText(value+" Мбит/с");
        /*
        final float DELTA = Math.abs(newValue-value);
        final int sign = newValue > value ? 1 : -1;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2), event -> {
            value += sign;
            valueLabel.setText(value+" Мбит/с");
        }));
        timeline.setCycleCount((int)DELTA);
        timeline.play();
        */
    }
}
