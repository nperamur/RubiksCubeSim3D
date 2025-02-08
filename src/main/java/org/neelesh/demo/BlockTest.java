package org.neelesh.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.cubesim.Cube;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BlockTest extends Application {
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private static int turningFaces;
    private static Cube cube = new Cube();
    @Override
    public void start(Stage stage) throws IOException {
        cube.rotate("X", "CW");
        cube.rotate("X", "CW");

        Rotate rotateX = new Rotate(0, 250, 250, 250, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(90, 250, 250, 250, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, 250, 250, 250, Rotate.Z_AXIS);


        Group group = new Group();
        //group.getChildren().add(box);
        Group[][][] cubes = new Group[5][5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int x = 0; x < 5; x++) {
                    cubes[i][j][x] = Block.createCube(i*200,j*200, x*200, Color.SANDYBROWN, Color.SANDYBROWN, Color.SANDYBROWN, Color.SANDYBROWN, Color.GREEN, Color.SANDYBROWN);
                    group.getChildren().add(cubes[i][j][x]);
                }
            }
        }



        //group.getChildren().add(box2);
        group.setTranslateX(100);
        group.setTranslateZ(1000);
        group.getTransforms().addAll(rotateX, rotateY, rotateZ);
        Material material = new PhongMaterial(Color.LIGHTSKYBLUE);
        Scene scene = new Scene(group, 500, 500); //I think the Scene is the window
        stage.setTitle("Cube");
        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        scene.setFill(Color.DIMGREY);
        Popup popup = new Popup();
        scene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                group.setTranslateZ(group.getTranslateZ() - 30);
            } else if (deltaY < 0) {
                group.setTranslateZ(group.getTranslateZ() + 30);
            }
        });
        scene.setOnMousePressed((MouseEvent event) -> {
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();

            // Calculate the change in rotation based on mouse movement
            double deltaX = mousePosX - mouseOldX;
            double deltaY = mousePosY - mouseOldY;


            // Apply rotation relative to the mouse movement
            if (((Math.abs(rotateX.getAngle())+90)/180)%2 <1) {
                rotateY.setAngle(rotateY.getAngle() - deltaX * 0.2);
            } else {
                rotateY.setAngle(rotateY.getAngle() + deltaX * 0.2);
            }

            // Rotate around Y-axis
            rotateX.setAngle(rotateX.getAngle() + deltaY * 0.4); // Rotate around X-axis
            // Update old mouse positions for the next drag
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}