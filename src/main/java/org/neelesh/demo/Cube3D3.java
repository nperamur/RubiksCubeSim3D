package org.neelesh.demo;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.cubesim.Cube;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubePuzzle;
import org.worldcubeassociation.tnoodle.scrambles.Puzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;

import static org.neelesh.demo.CubeAnimations.rotateX;
import static org.neelesh.demo.CubeAnimations.rotateZ;

public class Cube3D3 extends Application {
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    static int turningFaces;
    static Cube cube = new Cube();
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static Group[] cubes = null;
    private static boolean scrambled = false;
    private static double time = 0;
    private static double lastUpdate = 0;
    private static Label label;
    private static Button button;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static int selectedButton = 0;
    private boolean counterclockwise = false;
    private boolean wideMoves = false;
    private static final AnimationTimer TIMER = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (lastUpdate > 0) {
                time += ((l-lastUpdate) / 1_000_000_000.0);
                if (time < 60) {
                    label.setText(DECIMAL_FORMAT.format(time % 60));
                } else if (time < 600) {
                    label.setText((int) time / 60 + ":" + DECIMAL_FORMAT.format(time % 60));
                } else {
                    label.setText("TimeOut");
                }
            }
            lastUpdate = l;
        }
    };
    @Override
    public void start(Stage stage) throws IOException {
        cube.rotate("X", "CW");
        cube.rotate("X", "CW");
        int size = 500;
        int centerDistance = 350;
        Rotate rotateX = new Rotate(0, centerDistance, centerDistance, centerDistance, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(90, centerDistance, centerDistance, centerDistance, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, centerDistance, centerDistance, centerDistance, Rotate.Z_AXIS);
        Group group = new Group();
        Group group2 = new Group();
        group2.getChildren().add(group);
        cubes = new Group[] {
                // corners
                CubeRenderer.createCornerCube(0, 0, size, 3, 0, 4, Color.GREEN, Color.RED, Color.YELLOW),
                CubeRenderer.createCornerCube(0, size, size, 3, 0, 5, Color.GREEN, Color.RED, Color.WHITE),
                CubeRenderer.createCornerCube(size, 0, size, 0, 1, 4, Color.RED, Color.DODGERBLUE, Color.YELLOW),
                CubeRenderer.createCornerCube(size, size, size, 0, 1, 5, Color.RED, Color.DODGERBLUE, Color.WHITE),
                CubeRenderer.createCornerCube(0, 0, 0, 3, 2, 4, Color.GREEN, Color.ORANGE, Color.YELLOW),
                CubeRenderer.createCornerCube(0, size, 0, 3, 2, 5, Color.GREEN, Color.ORANGE, Color.WHITE),
                CubeRenderer.createCornerCube(size, 0, 0, 2, 1, 4, Color.ORANGE, Color.DODGERBLUE, Color.YELLOW),
                CubeRenderer.createCornerCube(size, size, 0, 2, 1, 5, Color.ORANGE, Color.DODGERBLUE, Color.WHITE),
                //edges
                CubeRenderer.createEdgeCube(size, size / 2, 0, 2, 1, Color.ORANGE, Color.DODGERBLUE),
                CubeRenderer.createEdgeCube(0, size / 2, 0, 2, 3, Color.ORANGE, null),
                CubeRenderer.createEdgeCube(size, 0, size / 2, 1, 4, Color.DODGERBLUE, Color.YELLOW),
                CubeRenderer.createEdgeCube(size, size / 2, size, 1, 0, Color.DODGERBLUE, Color.RED),
                CubeRenderer.createEdgeCube(size, size, size / 2, 1, 5, Color.DODGERBLUE, Color.WHITE),
                CubeRenderer.createEdgeCube(size / 2, size, size, 0, 5, Color.RED, Color.WHITE),
                CubeRenderer.createEdgeCube(size / 2, 0, 0, 4, 2, Color.YELLOW, Color.ORANGE),
                CubeRenderer.createEdgeCube(0, 0, size / 2, 4, 3, Color.YELLOW, null),
                CubeRenderer.createEdgeCube(size / 2, 0, size, 4, 0, Color.YELLOW, Color.RED),
                CubeRenderer.createEdgeCube(size / 2, size, 0, 5, 2, Color.WHITE, Color.ORANGE),
                CubeRenderer.createEdgeCube(0, size, size / 2, 5, 3, Color.WHITE, null),
                CubeRenderer.createEdgeCube(0, size / 2, size, 0, 3, Color.RED, null),
                //centers
                CubeRenderer.createEdgeCube(size, size / 2, size / 2, 1, 5, Color.DODGERBLUE, null),
                CubeRenderer.createEdgeCube(size / 2, size / 2, size, 0, 5, Color.RED, null),
                CubeRenderer.createEdgeCube(size / 2, size, size / 2, 5, 1, Color.WHITE, null),
                CubeRenderer.createEdgeCube(size / 2, 0, size / 2, 4, 1, Color.YELLOW, null),
                CubeRenderer.createEdgeCube(size / 2, size / 2, 0, 2, 1, Color.ORANGE, null),
                CubeRenderer.createEdgeCube(0, size / 2, size/2, 3, 1, Color.GREEN, null),

        };
        updateCube(cubes);
        Group face1 = new Group();

        face1.getChildren().addAll(cubes[0], cubes[1], cubes[2], cubes[3]);
        Group face2 = new Group();
        face2.getChildren().addAll(cubes[4], cubes[5], cubes[6], cubes[7]);
        Group middleLayer = new Group();
        middleLayer.getChildren().addAll(cubes[8], cubes[9], cubes[10], cubes[11], cubes[12], cubes[13], cubes[14], cubes[15], cubes[16], cubes[17], cubes[18], cubes[19], cubes[20], cubes[21], cubes[22], cubes[23], cubes[24], cubes[25]);
        group.getChildren().addAll(face1, face2, middleLayer);
        button = setUpButton(140, 25, 500, 500);
        label = new Label();
        label.setText("0.00");
        label.setFont(new Font(33));
        label.setStyle("-fx-text-fill: #FFFFFF");
        label.setTranslateX(220);
        group2.getChildren().addAll(button, label);


        group.setTranslateX(size/2 - centerDistance);
        group.setTranslateY(-100);
        group.setTranslateZ(2000);
        group.getTransforms().addAll(rotateX, rotateY, rotateZ);
        createLighting(group2);
        Scene scene = new Scene(group2, 500, 500, true, SceneAntialiasing.BALANCED);
        Group settingsGroup = new Group();
        Scene settingsScene = new Scene(settingsGroup, 500, 500);
        settingsScene.setFill(BACKGROUND_COLOR);
        createOptionsScreen(settingsGroup, settingsScene, scene, stage);

        stage.setTitle("Rubik's Cube");
        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        scene.setFill(BACKGROUND_COLOR);

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

            double deltaX = mousePosX - mouseOldX;
            double deltaY = mousePosY - mouseOldY;


            if (((Math.abs(rotateX.getAngle()) + 90) / 180) % 2 < 1) {
                rotateY.setAngle(rotateY.getAngle() - deltaX * 0.2);
            } else {
                rotateY.setAngle(rotateY.getAngle() + deltaX * 0.2);
            }
            if (rotateY.getAngle() <= 45) {
                cube.rotate("Y", "CCW");
                rotateY.setAngle(rotateY.getAngle() + 90);
                updateCube(cubes);
            } else if (rotateY.getAngle() > 135) {
                rotateY.setAngle(rotateY.getAngle() - 90);
                cube.rotate("Y", "CW");
                updateCube(cubes);
            }
            if (Math.abs(rotateX.getAngle() % 180) + 90 > 180) {
                cube.rotate("X", "CW");
                cube.rotate("X", "CW");
                if (rotateX.getAngle() % 180 > 0) {
                    rotateX.setAngle(rotateX.getAngle() - 180);
                } else {
                    rotateX.setAngle(rotateX.getAngle() + 180);
                }
                rotateY.setAngle(180 - rotateY.getAngle());
                updateCube(cubes);
            }
            rotateX.setAngle(rotateX.getAngle() + deltaY * 0.4);
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            String rightCode = CubeConfigHandler.loadConfig("Turn Right Face");
            String leftCode = CubeConfigHandler.loadConfig("Turn Left Face");
            String frontCode = CubeConfigHandler.loadConfig("Turn Front Face");
            String backCode = CubeConfigHandler.loadConfig("Turn Back Face");
            String upCode = CubeConfigHandler.loadConfig("Turn Up Face");
            String downCode = CubeConfigHandler.loadConfig("Turn Down Face");
            String xCode = CubeConfigHandler.loadConfig("X Rotations");
            String yCode = CubeConfigHandler.loadConfig("Y Rotations");
            String zCode = CubeConfigHandler.loadConfig("Z Rotations");
            String mSliceCode = CubeConfigHandler.loadConfig("M Slices");
            String sSliceCode = CubeConfigHandler.loadConfig("S Slices");
            String eSliceCode = CubeConfigHandler.loadConfig("E Slices");
            String counterclockwiseCode = CubeConfigHandler.loadConfig("Counterclockwise Moves");
            String wideMovesCode = CubeConfigHandler.loadConfig("Wide Moves");
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                stage.setScene(settingsScene);
            }
            if (e.getCode().name().equals(CubeConfigHandler.loadConfig("Show/Hide Timer"))) {
                if (label.isVisible()) {
                    label.setVisible(false);
                } else {
                    label.setVisible(true);
                }
            }
            if (e.getCode().name().equals(counterclockwiseCode)) {
                counterclockwise = true;
            }
            if (e.getCode().name().equals(wideMovesCode)) {
                wideMoves = true;
            }
            if (e.getCode().name().equals(xCode)) {
                rotateX(cubes, face1, face2, middleLayer, !counterclockwise, cube);
            }
            if (e.getCode().name().equals(zCode)) {
                rotateZ(cubes, face1, face2, middleLayer, !counterclockwise, cube);
            }
            if (e.getCode().name().equals(yCode)) {
                double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Rotation Animation Speed"));
                boolean isCounterclockwise = counterclockwise;
                AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;
                    private double elapsedTime = 0;
                    @Override
                    public void handle(long l) {
                        if (lastUpdate > 0) {
                            double currentTime = (l - lastUpdate) / 1_000_000_000.0;
                            if (isCounterclockwise) {
                                rotateY.setAngle(rotateY.getAngle() - 90 / animationSpeed * currentTime);
                            } else {
                                rotateY.setAngle(rotateY.getAngle() + 90 / animationSpeed * currentTime);
                            }
                            elapsedTime += currentTime;
                        }
                        lastUpdate = l;
                        if (elapsedTime > animationSpeed || animationSpeed == 0) {
                            stop();
                            if (isCounterclockwise) {
                                cube.rotate("Y", "CCW");
                                if (animationSpeed != 0) {
                                    rotateY.setAngle(rotateY.getAngle() + elapsedTime * 90 / animationSpeed);
                                }
                            } else {
                                cube.rotate("Y", "CW");
                                if (animationSpeed != 0) {
                                    rotateY.setAngle(rotateY.getAngle() - elapsedTime * 90 / animationSpeed);
                                }
                            }
                            updateCube(cubes);
                        }
                    }
                };
                timer.start();
            }
            if (e.getCode().name().equals(rightCode) || e.getCode().name().equals(leftCode)) {
                restructureXAxis(face1, face2, middleLayer, cubes);
                if (e.getCode().name().equals(rightCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, true, cubes, "L", "CCW", "X", "CCW");
                } else if (e.getCode().name().equals(rightCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, false, cubes, "L", "CW", "X", "CW");
                } else if (e.getCode().name().equals(rightCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face1, true, cubes, "R", "CCW");
                } else if (e.getCode().name().equals(rightCode)) {
                    CubeAnimations.rotateFace(face1, false, cubes, "R", "CW");
                } else if (e.getCode().name().equals(leftCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, false, cubes, "R", "CCW", "X", "CW");
                } else if (e.getCode().name().equals(leftCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, true, cubes, "R", "CW", "X", "CCW");
                } else if (e.getCode().name().equals(leftCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face2, false, cubes, "L", "CCW");
                } else {
                    CubeAnimations.rotateFace(face2, true, cubes, "L", "CW");
                }
            }
            if (e.getCode().name().equals(mSliceCode)) {
                restructureXAxis(face1, face2, middleLayer, cubes);
                if (!counterclockwise) {
                    CubeAnimations.rotateMiddleLayer(middleLayer, true, cubes, "R", "CW", "L", "CCW", "X", "CCW");
                } else {
                    CubeAnimations.rotateMiddleLayer(middleLayer, false, cubes, "R", "CCW", "L", "CW", "X", "CW");
                }
            }
            if (e.getCode().name().equals(eSliceCode)) {
                restructureYAxis(face1, face2, middleLayer, cubes);
                if (!counterclockwise) {
                    CubeAnimations.rotateMiddleLayer(middleLayer, false, cubes, "U", "CW", "D", "CCW", "Y", "CCW");
                } else {
                    CubeAnimations.rotateMiddleLayer(middleLayer, true, cubes, "U", "CCW", "D", "CW", "Y", "CW");
                }
            }
            if (e.getCode().name().equals(sSliceCode)) {
                restructureZAxis(face1, face2, middleLayer, cubes);
                if (counterclockwise) {
                    CubeAnimations.rotateMiddleLayer(middleLayer, true, cubes, "F", "CW", "B", "CCW", "Z", "CCW");
                } else {
                    CubeAnimations.rotateMiddleLayer(middleLayer, false, cubes, "F", "CCW", "B", "CW", "Z", "CW");
                }
            }
            if (e.getCode().name().equals(upCode) || e.getCode().name().equals(downCode)) {
                restructureYAxis(face1, face2, middleLayer, cubes);
                if (e.getCode().name().equals(upCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, false, cubes, "D", "CCW", "Y", "CCW");
                } else if (e.getCode().name().equals(upCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, true, cubes, "D", "CW", "Y", "CW");
                } else if (e.getCode().name().equals(upCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face1, false, cubes, "U", "CCW");
                } else if (e.getCode().name().equals(upCode)) {
                    CubeAnimations.rotateFace(face1, true, cubes, "U", "CW");
                } else if (e.getCode().name().equals(downCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, true, cubes, "U", "CCW", "Y", "CW");
                } else if (e.getCode().name().equals(downCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, false, cubes, "U", "CW", "Y", "CCW");
                } else if (e.getCode().name().equals(downCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face2, true, cubes, "D", "CCW");
                } else {
                    CubeAnimations.rotateFace(face2, false, cubes, "D", "CW");
                }
            }
            if (e.getCode().name().equals(frontCode) || e.getCode().name().equals(backCode)) {
                restructureZAxis(face1, face2, middleLayer, cubes);
                if (e.getCode().name().equals(frontCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, true, cubes, "B", "CCW", "Z", "CCW");
                } else if (e.getCode().name().equals(frontCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face1, middleLayer, false, cubes, "B", "CW", "Z", "CW");
                } else if (e.getCode().name().equals(frontCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face1, true, cubes, "F", "CCW");
                } else if (e.getCode().name().equals(frontCode)) {
                    CubeAnimations.rotateFace(face1, false, cubes, "F", "CW");
                } else if (e.getCode().name().equals(backCode) && wideMoves && counterclockwise) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, false, cubes, "F", "CCW", "Z", "CW");
                } else if (e.getCode().name().equals(backCode) && wideMoves) {
                    CubeAnimations.rotateFaceWide(face2, middleLayer, true, cubes, "F", "CW", "Z", "CCW");
                } else if (e.getCode().name().equals(backCode) && counterclockwise) {
                    CubeAnimations.rotateFace(face2, false, cubes, "B", "CCW");
                } else {
                    CubeAnimations.rotateFace(face2, true, cubes, "B", "CW");
                }

            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e) ->  {
            String counterclockwiseCode = CubeConfigHandler.loadConfig("Counterclockwise Moves");
            String wideMovesCode = CubeConfigHandler.loadConfig("Wide Moves");
            if (e.getCode().name().equals(counterclockwiseCode)) {
                counterclockwise = false;
            }
            if (e.getCode().name().equals(wideMovesCode)) {
                wideMoves = false;
            }
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
        
    }
    public static void main(String[] args) {
        launch();
    }


    public static void updateCube(Group[] cubes) {
        HashMap<String, Color> colors = new HashMap<>();
        colors.put("red", Color.RED);
        colors.put("orange", Color.rgb(255, 175, 0));
        colors.put("yellow", Color.YELLOW);
        colors.put("green", Color.rgb(0, 255, 0));
        colors.put("blue", Color.DODGERBLUE);
        colors.put("white", Color.WHITE);
        HashMap<String, String> stickerFaces;
        for (int i = 0; i < cube.getCorners().length; ++i) {
            stickerFaces = new HashMap<>();
            stickerFaces.put(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getColor1());
            stickerFaces.put(cube.getCorners()[i].getFace2(), cube.getCorners()[i].getColor2());
            stickerFaces.put(cube.getCorners()[i].getFace3(), cube.getCorners()[i].getColor3());
            if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getLeft(), cube.getFront(), cube.getUp())) {
                ((MeshView) cubes[6].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[6].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[6].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getUp(), cube.getBack(), cube.getLeft())) {
                ((MeshView) cubes[4].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
                ((MeshView) cubes[4].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[4].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getLeft(), cube.getFront(), cube.getDown())) {
                ((MeshView) cubes[7].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[7].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[7].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getFront(), cube.getUp(), cube.getRight())) {
                ((MeshView) cubes[2].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[2].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[2].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getRight(), cube.getBack(), cube.getUp())) {
                ((MeshView) cubes[0].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
                ((MeshView) cubes[0].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[0].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getRight(), cube.getBack(), cube.getDown())) {
                ((MeshView) cubes[1].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
                ((MeshView) cubes[1].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[1].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getRight(), cube.getFront(), cube.getDown())) {
                ((MeshView) cubes[3].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[3].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[3].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            } else if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getLeft(), cube.getBack(), cube.getDown())) {
                ((MeshView) cubes[5].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
                ((MeshView) cubes[5].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[5].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            }
        }

        for (int i = 0; i < cube.getEdges().length; i++) {
            stickerFaces = new HashMap<>();
            stickerFaces.put(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getColor1());
            stickerFaces.put(cube.getEdges()[i].getFace2(), cube.getEdges()[i].getColor2());
            if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getLeft(), cube.getFront())) {
                ((MeshView) cubes[8].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[8].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getLeft(), cube.getBack())) {
                ((MeshView) cubes[9].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[9].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getFront(), cube.getUp())) {
                ((MeshView) cubes[10].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[10].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getFront(), cube.getRight())) {
                ((MeshView) cubes[11].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[11].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getFront(), cube.getDown())) {
                ((MeshView) cubes[12].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[12].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getRight(), cube.getDown())) {
                ((MeshView) cubes[13].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[13].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getLeft(), cube.getUp())) {
                ((MeshView) cubes[14].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[14].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getUp(), cube.getBack())) {
                ((MeshView) cubes[15].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
                ((MeshView) cubes[15].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getRight(), cube.getUp())) {
                ((MeshView) cubes[16].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[16].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getDown(), cube.getLeft())) {
                ((MeshView) cubes[17].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
                ((MeshView) cubes[17].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getDown(), cube.getBack())) {
                ((MeshView) cubes[18].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getDown()))));
                ((MeshView) cubes[18].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
            } else if (compareEdges(cube.getEdges()[i].getFace1(), cube.getEdges()[i].getFace2(), cube.getRight(), cube.getBack())) {
                ((MeshView) cubes[19].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getRight()))));
                ((MeshView) cubes[19].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getBack()))));
            }
        }
        HashMap<String, String> stickerColors = new HashMap();
        stickerColors.put("G", "green");
        stickerColors.put("B", "blue");
        stickerColors.put("R", "red");
        stickerColors.put("W", "white");
        stickerColors.put("O", "orange");
        stickerColors.put("Y", "yellow");
        ((MeshView) cubes[24].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getLeft()))));
        ((MeshView) cubes[23].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getUp()))));
        ((MeshView) cubes[22].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getDown()))));
        ((MeshView) cubes[21].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getRight()))));
        ((MeshView) cubes[20].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getFront()))));
        ((MeshView) cubes[25].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerColors.get(cube.getBack()))));
        for (int i = 0; i < 25; i++) {
            ((PhongMaterial) ((MeshView) cubes[i].getChildren().get(0)).getMaterial()).setSpecularColor(((PhongMaterial) ((MeshView) cubes[i].getChildren().get(0)).getMaterial()).getDiffuseColor());
            ((PhongMaterial) ((MeshView) cubes[i].getChildren().get(1)).getMaterial()).setSpecularColor(((PhongMaterial) ((MeshView) cubes[i].getChildren().get(1)).getMaterial()).getDiffuseColor());
            ((PhongMaterial) ((MeshView) cubes[i].getChildren().get(2)).getMaterial()).setSpecularColor(((PhongMaterial) ((MeshView) cubes[i].getChildren().get(2)).getMaterial()).getDiffuseColor());
        }
    }

    public static boolean compareCorners(String corner1Sticker1, String corner1Sticker2, String corner1Sticker3, String corner2Sticker1, String corner2Sticker2, String corner2Sticker3) {
        Set<String> corner1Stickers = new HashSet();
        corner1Stickers.add(corner1Sticker1);
        corner1Stickers.add(corner1Sticker2);
        corner1Stickers.add(corner1Sticker3);
        Set<String> corner2Stickers = new HashSet();
        corner2Stickers.add(corner2Sticker1);
        corner2Stickers.add(corner2Sticker2);
        corner2Stickers.add(corner2Sticker3);
        return corner1Stickers.equals(corner2Stickers);
    }

    public static boolean compareEdges(String edge1Sticker1, String edge1Sticker2, String edge2Sticker1, String edge2Sticker2) {
        Set<String> edge1Stickers = new HashSet();
        edge1Stickers.add(edge1Sticker1);
        edge1Stickers.add(edge1Sticker2);
        Set<String> edge2Stickers = new HashSet();
        edge2Stickers.add(edge2Sticker1);
        edge2Stickers.add(edge2Sticker2);
        return edge1Stickers.equals(edge2Stickers);
    }


    private static void restructureYAxis(Group face1, Group face2, Group middleLayer, Group[] cubes) {
        face1.getChildren().clear();
        face1.getChildren().addAll(cubes[0], cubes[2], cubes[4], cubes[6], cubes[10], cubes[14], cubes[15], cubes[16], cubes[23]);
        face1.setRotationAxis(Rotate.Y_AXIS);
        middleLayer.getChildren().clear();
        middleLayer.getChildren().addAll(cubes[8], cubes[9], cubes[11], cubes[19], cubes[20], cubes[21], cubes[24], cubes[25]);
        middleLayer.setRotationAxis(Rotate.Y_AXIS);
        face2.getChildren().clear();
        face2.getChildren().addAll(cubes[1], cubes[3], cubes[5], cubes[7], cubes[12], cubes[13], cubes[17], cubes[18], cubes[22]);
        face2.setRotationAxis(Rotate.Y_AXIS);
    }

    static void restructureXAxis(Group face1, Group face2, Group middleLayer, Group[] cubes) {
        face1.getChildren().clear();
        face1.getChildren().addAll(cubes[0], cubes[1], cubes[2], cubes[3], cubes[11], cubes[13], cubes[16], cubes[19], cubes[21]);
        face1.setRotationAxis(Rotate.Z_AXIS);
        middleLayer.getChildren().clear();
        middleLayer.getChildren().addAll(cubes[10], cubes[12], cubes[15], cubes[18], cubes[20], cubes[22], cubes[23], cubes[25]);
        middleLayer.setRotationAxis(Rotate.Z_AXIS);
        face2.getChildren().clear();
        face2.getChildren().addAll(cubes[4], cubes[5], cubes[6], cubes[7], cubes[8], cubes[9], cubes[14], cubes[17], cubes[24]);
        face2.setRotationAxis(Rotate.Z_AXIS);
    }


    static void restructureZAxis(Group face1, Group face2, Group middleLayer, Group[] cubes) {
        face1.getChildren().clear();
        face1.getChildren().addAll(cubes[2], cubes[3], cubes[6], cubes[7], cubes[8], cubes[10], cubes[11], cubes[12], cubes[20]);
        face1.setRotationAxis(Rotate.X_AXIS);
        middleLayer.getChildren().clear();
        middleLayer.getChildren().addAll(cubes[13], cubes[14], cubes[16], cubes[17], cubes[21], cubes[22], cubes[23], cubes[24]);
        middleLayer.setRotationAxis(Rotate.X_AXIS);
        face2.getChildren().clear();
        face2.getChildren().addAll(cubes[0], cubes[1], cubes[4], cubes[5], cubes[9], cubes[15], cubes[18], cubes[19], cubes[25]);
        face2.setRotationAxis(Rotate.X_AXIS);
    }

    private static void createLighting(Group group) {
        PointLight pointLight = new PointLight();
        pointLight.setTranslateX(1600);
        pointLight.setTranslateY(-1400);
        pointLight.setTranslateZ(1600);
        pointLight.setColor(Color.rgb(180, 180, 180));
        PointLight pointLight2 = new PointLight();
        pointLight2.setTranslateX(-900);
        pointLight2.setTranslateY(1800);
        pointLight2.setTranslateZ(-1600);
        pointLight2.setColor(Color.rgb(180, 180, 180));

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(Color.rgb(45, 45, 45));
        group.getChildren().addAll(pointLight, pointLight2, ambientLight);
    }

    private static Button setUpButton(int width, int height, int screenWidth, int screenHeight) {
        Button button = new Button();
        button.setTranslateX((double) screenWidth / 2 - (double) width / 2);
        button.setTranslateY((double) screenHeight - (double) height * 2);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cube.resetCube();
                Puzzle puzzle = new ThreeByThreeCubePuzzle();
                Random r = new Random();
                String scramble = puzzle.generateWcaScramble(r);
                parseScramble(scramble);
                updateCube(cubes);
                TIMER.stop();
                scrambled = true;
                button.setVisible(false);
                label.setText("0.00");
            }
        });
        button.setText("Scramble Cube");
        button.getOnAction();
        button.setFont(new Font(15));
        return button;
    }

    private static void parseScramble(String scramble) {
        String[] scrambleArr = scramble.split(" ");

        for (int i = 0; i < scrambleArr.length; ++i) {
            String direction = "CCW";
            String side = Character.toString(scrambleArr[i].charAt(0));
            if (scrambleArr[i].length() == 1) {
                direction = "CW";
            } else if (scrambleArr[i].charAt(1) == '2') {
                cube.turn(side, direction);
            }

            cube.turn(side, direction);
        }
    }
    static void setUpTimer() {
        if (scrambled) {
            time = 0;
            lastUpdate = 0;
            TIMER.start();
            scrambled = false;
        }
        if (cube.isSolved()) {
            TIMER.stop();
            button.setVisible(true);
        }
    }

    private static void createOptionsScreen(Group settingsGroup, Scene settingsScene, Scene primaryScene, Stage stage) {
        Button[] settingsButtons = new Button[15];
        Label settingsLabel = new Label();
        settingsLabel.setText("Cube Settings");
        settingsLabel.setFont(new Font(33));
        settingsLabel.setStyle("-fx-text-fill: #FFFFFF");
        String[] settings = new String[] {
                "Turn Right Face",
                "Turn Left Face",
                "Turn Front Face",
                "Turn Back Face",
                "Turn Up Face",
                "Turn Down Face",
                "M Slices",
                "E Slices",
                "S Slices",
                "X Rotations",
                "Y Rotations",
                "Z Rotations",
                "Counterclockwise Moves",
                "Wide Moves",
                "Show/Hide Timer"
        };
        if (!Files.exists(Path.of(System.getProperty("user.home"), ".Cube3D", "cube_config.json"))) {
            CubeConfigHandler.initializeConfig(settings);
        }
        for (int i = 0; i < settingsButtons.length; i++) {
            settingsButtons[i] = new Button();
            settingsButtons[i].setPrefWidth(100);
            settingsButtons[i].setPrefHeight(10);
            settingsButtons[i].setTranslateY(i * 30 + 55);
            settingsButtons[i].setTranslateX(200);
            settingsButtons[i].setText(CubeConfigHandler.loadConfig(settings[i]));
            int finalI = i;
            settingsButtons[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    selectedButton = finalI;
                }
            });
        }
        Slider slider = new Slider(0, 0.5, 0.1); // min, max, initial value
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.05);
        slider.setMinorTickCount(0);
        slider.setTranslateX(350);
        slider.setTranslateY(90);
        slider.adjustValue(Double.parseDouble(CubeConfigHandler.loadConfig("Turning Animation Speed")));
        slider.setSnapToTicks(true);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            CubeConfigHandler.updateConfig("Turning Animation Speed", Double.parseDouble(newValue.toString()));
        });
        Slider slider2 = new Slider(0, 0.5, 0.1); // min, max, initial value
        slider2.setShowTickLabels(true);
        slider2.setShowTickMarks(true);
        slider2.setMajorTickUnit(0.05);
        slider2.setMinorTickCount(0);
        slider2.setTranslateX(350);
        slider2.setTranslateY(185);
        slider2.adjustValue(Double.parseDouble(CubeConfigHandler.loadConfig("Rotation Animation Speed")));
        slider2.setSnapToTicks(true);
        slider2.valueProperty().addListener((observable, oldValue, newValue) -> {
            CubeConfigHandler.updateConfig("Rotation Animation Speed", Double.parseDouble(newValue.toString()));
        });
        Button resetButton = new Button();
        resetButton.setPrefHeight(10);
        resetButton.setPrefWidth(100);
        resetButton.setTranslateX(350);
        resetButton.setTranslateY(20);
        resetButton.setText("Reset All");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CubeConfigHandler.initializeConfig(settings);
                for (int i = 0; i < settingsButtons.length; i++) {
                    settingsButtons[i].setText(CubeConfigHandler.loadConfig(settings[i]));
                }
                slider.adjustValue(0.15);
                slider2.adjustValue(0.2);
            }
        });
        for (int i = 0; i < settings.length; i++) {
            Label moveLabel = new Label();
            moveLabel.setTranslateY(i * 30 + 55);
            moveLabel.setTranslateX(10);
            moveLabel.setText(settings[i]);
            moveLabel.setFont(new Font(15));
            moveLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: grey; -fx-border-width: 1;");
            moveLabel.setPrefWidth(175-moveLabel.getWidth());
            settingsGroup.getChildren().add(moveLabel);
        }
        Label turningAnimationLabel = new Label();
        turningAnimationLabel.setTranslateY(55);
        turningAnimationLabel.setTranslateX(315);
        turningAnimationLabel.setText("Turning Animation Duration");
        turningAnimationLabel.setFont(new Font(14));
        turningAnimationLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: grey; -fx-border-width: 1; ");
        turningAnimationLabel.setPrefWidth(185);

        Label rotationAnimationLabel = new Label();
        rotationAnimationLabel.setTranslateY(150);
        rotationAnimationLabel.setTranslateX(315);
        rotationAnimationLabel.setText("Rotation Animation Duration");
        rotationAnimationLabel.setFont(new Font(14));
        rotationAnimationLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-border-color: grey; -fx-border-width: 1; ");
        rotationAnimationLabel.setPrefWidth(185);

        settingsGroup.getChildren().addAll(settingsButtons);
        settingsGroup.getChildren().addAll(settingsLabel, resetButton, slider, turningAnimationLabel, rotationAnimationLabel, slider2);
        settingsScene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                stage.setScene(primaryScene);
                for (int i = 0; i < 3; i++) {
                    ((Group) primaryScene.getRoot()).getChildren().remove(3);
                }
                createLighting(((Group) primaryScene.getRoot()));
            } else {
                settingsButtons[selectedButton].setText(e.getCode().name());
                CubeConfigHandler.updateConfig(settings[selectedButton], e.getCode().name());
            }
        });
    }
}