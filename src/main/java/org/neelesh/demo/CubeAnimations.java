package org.neelesh.demo;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import org.cubesim.Cube;

import static org.neelesh.demo.Cube3D3.*;

public class CubeAnimations {
    public static void rotateFace(Group face, boolean direction, Group[] cubes, String face1, String direction1) {
        double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Turning Animation Speed"));
        turningFaces++;
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    double currentTime = (l - lastUpdate) / 1_000_000_000.0;
                    if (direction) {
                        face.setRotate(face.getRotate() + 90/animationSpeed * currentTime);
                    } else {
                        face.setRotate(face.getRotate() - 90/animationSpeed * currentTime);
                    }
                    elapsedTime += currentTime;
                }
                lastUpdate = l;
                if (elapsedTime > animationSpeed || turningFaces > 1) {
                    stop();
                    turningFaces--;
                    face.setRotate(0);
                    Cube3D3.cube.turn(face1, direction1);
                    Cube3D3.updateCube(cubes);
                    Cube3D3.setUpTimer();
                }
            }
        };
        timer.start();
    }

    public static void rotateMiddleLayer(Group face, boolean direction, Group[] cubes, String face1, String direction1, String face2, String direction2, String face3, String direction3) {
        double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Turning Animation Speed"));
        turningFaces++;
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    double currentTime = (l - lastUpdate) / 1_000_000_000.0;
                    if (direction) {
                        face.setRotate(face.getRotate() + 90/animationSpeed * currentTime);
                    } else {
                        face.setRotate(face.getRotate() - 90/animationSpeed * currentTime);
                    }
                    elapsedTime += currentTime;
                }
                lastUpdate = l;
                if (elapsedTime > animationSpeed || turningFaces > 1) {
                    stop();
                    turningFaces--;
                    face.setRotate(0);
                    Cube3D3.cube.turn(face1, direction1);
                    Cube3D3.cube.turn(face2, direction2);
                    Cube3D3.cube.rotate(face3, direction3);
                    Cube3D3.updateCube(cubes);
                    Cube3D3.setUpTimer();
                }
            }
        };
        timer.start();
    }

    public static void rotateFaceWide(Group face, Group middleLayer, boolean direction, Group[] cubes, String face1, String direction1, String rotate, String direction2) {
        turningFaces++;
        double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Turning Animation Speed"));
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    double currentTime = (l - lastUpdate) / 1_000_000_000.0;
                    if (direction) {
                        face.setRotate(face.getRotate() + 90/animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() + 90/animationSpeed * currentTime);

                    } else {
                        face.setRotate(face.getRotate() - 90/animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() - 90/animationSpeed * currentTime);
                    }
                    elapsedTime += currentTime;
                }
                lastUpdate = l;
                if (elapsedTime > animationSpeed || turningFaces > 1) {
                    stop();
                    turningFaces--;
                    face.setRotate(0);
                    middleLayer.setRotate(0);
                    Cube3D3.cube.turn(face1, direction1);
                    Cube3D3.cube.rotate(rotate, direction2);
                    Cube3D3.updateCube(cubes);
                    Cube3D3.setUpTimer();
                }
            }
        };
        timer.start();
    }

    public static void rotateX(Group[] cubes, Group face1, Group face2, Group middleLayer, boolean clockwise, Cube cube) {
        turningFaces++;
        restructureXAxis(face1, face2, middleLayer, cubes);
        double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Rotation Animation Speed"));
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    double currentTime = (l - lastUpdate) / 1_000_000_000.0;
                    if (clockwise) {
                        face1.setRotate(face1.getRotate() - 90 / animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() - 90 / animationSpeed * currentTime);
                        face2.setRotate(face2.getRotate() - 90 / animationSpeed * currentTime);
                    } else {
                        face1.setRotate(face1.getRotate() + 90 / animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() + 90 / animationSpeed * currentTime);
                        face2.setRotate(face2.getRotate() + 90 / animationSpeed * currentTime);
                    }
                    elapsedTime += currentTime;
                }
                lastUpdate = l;
                if (elapsedTime > animationSpeed || turningFaces > 1) {
                    stop();
                    face1.setRotate(0);
                    middleLayer.setRotate(0);
                    face2.setRotate(0);
                    if (clockwise) {
                        cube.rotate("X", "CW");
                    } else {
                        cube.rotate("X", "CCW");
                    }
                    Cube3D3.updateCube(cubes);
                    turningFaces--;
                }
            }
        };
        timer.start();
    }
    public static void rotateZ(Group[] cubes, Group face1, Group face2, Group middleLayer, boolean clockwise, Cube cube) {
        turningFaces++;
        double animationSpeed = Double.parseDouble(CubeConfigHandler.loadConfig("Rotation Animation Speed"));
        Cube3D3.restructureZAxis(face1, face2, middleLayer, cubes);
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedTime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    double currentTime = (l - lastUpdate) / 1_000_000_000.0;;
                    if (clockwise) {
                        face1.setRotate(face1.getRotate() - 90 / animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() - 90 / animationSpeed * currentTime);
                        face2.setRotate(face2.getRotate() - 90 / animationSpeed * currentTime);
                    } else {
                        face1.setRotate(face1.getRotate() + 90 / animationSpeed * currentTime);
                        middleLayer.setRotate(middleLayer.getRotate() + 90 / animationSpeed * currentTime);
                        face2.setRotate(face2.getRotate() + 90 / animationSpeed * currentTime);
                    }
                    elapsedTime += currentTime;
                }
                lastUpdate = l;
                if (elapsedTime > animationSpeed || turningFaces > 1) {
                    stop();
                    face1.setRotate(0);
                    middleLayer.setRotate(0);
                    face2.setRotate(0);
                    if (clockwise) {
                        cube.rotate("Z", "CW");
                    } else {
                        cube.rotate("Z", "CCW");
                    }
                    Cube3D3.updateCube(cubes);
                    turningFaces--;
                }
            }
        };
        timer.start();
    }
}
