package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Random;

public class Cube {
    Timeline timeline = null;

    public Box instance;
    public Cube(Pane pane) {
        instance = new Box(20,20,20);
        instance.setOpacity(0.4);
        instance.setLayoutX(new Random().nextInt(1920));
        Rotate rotateX = new Rotate(0,0,0,0,Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0,0,0,0,Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0,0,0,0,Rotate.Z_AXIS);
        instance.getTransforms().addAll(rotateX,rotateY,rotateZ);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.web("#09090d01"));
        instance.setMaterial(material);
        instance.setOnMousePressed(event -> {
            material.setDiffuseColor((Color.color(Math.random(), Math.random(), Math.random())));
            instance.setMaterial(material);
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            instance.setRotate(instance.getRotate()+0.1);
            instance.setTranslateY(instance.getTranslateY()+1);
            rotateX.setAngle(rotateX.getAngle()+Math.random());
            rotateY.setAngle(rotateY.getAngle()+Math.random());
            rotateZ.setAngle(rotateZ.getAngle()+Math.random());
            if (instance.getTranslateY() > Main.screenHeight()) {
                remove();
                pane.getChildren().remove(instance);
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();

    }
    public void remove() {
        timeline.stop();
    }
}
