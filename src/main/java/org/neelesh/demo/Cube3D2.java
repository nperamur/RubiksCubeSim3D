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

public class Cube3D2 extends Application {
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private static int turningFaces;
    private static Cube cube = new Cube();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Cube3D2.class.getResource("hello-view.fxml"));
        cube.rotate("X", "CW");
        cube.rotate("X", "CW");


        Box box2 = new Box(100, 100, 100);
        box2.setTranslateX(400);
        box2.setTranslateY(200);

        Rotate rotateX = new Rotate(0, 250, 250, 250, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(90, 250, 250, 250, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, 250, 250, 250, Rotate.Z_AXIS);


        Group group = new Group();
        Group[] cubes = new Group[] {
                createCube(0,0, 300, 3, 0, 4, Color.GREEN, Color.RED, Color.YELLOW),
                createCube(0,300, 300, 3, 0, 5, Color.GREEN, Color.RED, Color.WHITE),
                createCube(300,0, 300, 0, 1, 4, Color.RED, Color.DODGERBLUE, Color.YELLOW),
                createCube(300,300, 300, 0, 1, 5, Color.RED, Color.DODGERBLUE, Color.WHITE),
                createCube(0,0, 0, 3, 2, 4, Color.GREEN, Color.ORANGE, Color.YELLOW),
                createCube(0,300, 0, 3, 2, 5, Color.GREEN, Color.ORANGE, Color.WHITE),
                createCube(300,0, 0, 2, 1, 4, Color.ORANGE, Color.DODGERBLUE, Color.YELLOW),
                createCube(300,300, 0, 2, 1, 5, Color.ORANGE, Color.DODGERBLUE, Color.WHITE)
        };
        updateCube(cubes);
        Group face1 = new Group();
        face1.getChildren().addAll(cubes[0], cubes[1], cubes[2], cubes[3]);
        Group face2 = new Group();
        face2.getChildren().addAll(cubes[4], cubes[5], cubes[6], cubes[7]);
        group.getChildren().addAll(face1, face2);



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
            if (rotateY.getAngle() <= 45) {
                cube.rotate("Y", "CCW");
                rotateY.setAngle(rotateY.getAngle()+90);
                updateCube(cubes);
            } else if (rotateY.getAngle() > 135) {
                rotateY.setAngle(rotateY.getAngle()-90);
                cube.rotate("Y", "CW");
                updateCube(cubes);
            } if (Math.abs(rotateX.getAngle()%180)+90 > 180) {
                cube.rotate("X", "CW");
                cube.rotate("X", "CW");
                if (rotateX.getAngle()%180 > 0) {
                    rotateX.setAngle(rotateX.getAngle()-180);
                } else {
                    rotateX.setAngle(rotateX.getAngle()+180);
                }
                rotateY.setAngle(180-rotateY.getAngle());
                updateCube(cubes);
            }
             // Rotate around Y-axis
            rotateX.setAngle(rotateX.getAngle() + deltaY * 0.4); // Rotate around X-axis
            // Update old mouse positions for the next drag
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode().equals(KeyCode.X)) {
                rotateX(cubes, face1, face2, !e.isShiftDown());
            }
            if (e.getCode().equals(KeyCode.Z)) {
                rotateZ(cubes, face1, face2, !e.isShiftDown());
            }
            if (e.getCode().equals(KeyCode.Y)) {
                AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;
                    private double elapsedtime = 0;
                    private double elapsedDegrees = 0;
                    private boolean shiftPressed = e.isShiftDown();
                    @Override
                    public void handle(long l) {
                        if (lastUpdate > 0) {
                            elapsedtime += (l-lastUpdate)/1_000_000_000.0;
                            if (shiftPressed) {
                                rotateY.setAngle(rotateY.getAngle() - 35 * elapsedtime);
                            } else {
                                rotateY.setAngle(rotateY.getAngle() + 35 * elapsedtime);
                            }
                            elapsedDegrees += 35 * elapsedtime;
                        }
                        lastUpdate = l;
                        if (elapsedtime >= 0.2) {
                            stop();
                            if (shiftPressed) {
                                cube.rotate("Y", "CCW");
                                rotateY.setAngle(rotateY.getAngle()+elapsedDegrees);
                            } else {
                                cube.rotate("Y", "CW");
                                rotateY.setAngle(rotateY.getAngle()-elapsedDegrees);
                            }
                            updateCube(cubes);
                        }
                    }
                };
                timer.start();
            }
            if (e.getCode().equals(KeyCode.R) || e.getCode().equals(KeyCode.L)) {
                face1.getChildren().removeAll(face1.getChildren());
                face1.getChildren().addAll(cubes[0], cubes[1], cubes[2], cubes[3]);
                face1.setRotationAxis(Rotate.Z_AXIS);
                face2.getChildren().removeAll(face2.getChildren());
                face2.getChildren().addAll(cubes[4], cubes[5], cubes[6], cubes[7]);
                face2.setRotationAxis(Rotate.Z_AXIS);
                if (e.getCode().equals(KeyCode.R) && e.isShiftDown()) {
                    cube.turn("R", "CCW");
                    RotateFace(face1, true, cubes);
                } else if (e.getCode().equals(KeyCode.R)) {
                    cube.turn("R", "CW");
                    RotateFace(face1, false, cubes);
                } else if (e.getCode().equals(KeyCode.L) && e.isShiftDown()){
                    cube.turn("L", "CCW");
                    RotateFace(face2, false, cubes);
                } else {
                    cube.turn("L", "CW");
                    RotateFace(face2, true, cubes);
                }
            }
            if (e.getCode().equals(KeyCode.U) || e.getCode().equals(KeyCode.D)) {
                face1.getChildren().removeAll(face1.getChildren());
                face1.getChildren().addAll(cubes[0], cubes[2], cubes[4], cubes[6]);
                face1.setRotationAxis(Rotate.Y_AXIS);
                face2.getChildren().removeAll(face2.getChildren());
                face2.getChildren().addAll(cubes[1], cubes[3], cubes[5], cubes[7]);
                face2.setRotationAxis(Rotate.Y_AXIS);
                if (e.getCode().equals(KeyCode.U) && e.isShiftDown()) {
                    cube.turn("U", "CCW");
                    RotateFace(face1, false, cubes);
                } else if (e.getCode().equals(KeyCode.U)) {
                    cube.turn("U", "CW");
                    RotateFace(face1, true, cubes);
                } else if (e.getCode().equals(KeyCode.D) && e.isShiftDown()){
                    RotateFace(face2, true, cubes);
                    cube.turn("D", "CCW");
                } else {
                    RotateFace(face2, false, cubes);
                    cube.turn("D", "CW");
                }
            }
            if (e.getCode().equals(KeyCode.F) || e.getCode().equals(KeyCode.B)) {
                face1.getChildren().removeAll(face1.getChildren());
                face1.getChildren().addAll(cubes[2], cubes[3], cubes[6], cubes[7]);
                face1.setRotationAxis(Rotate.X_AXIS);
                face2.getChildren().removeAll(face2.getChildren());
                face2.getChildren().addAll(cubes[0], cubes[1], cubes[4], cubes[5]);
                face2.setRotationAxis(Rotate.X_AXIS);
                if (e.getCode().equals(KeyCode.F)  && e.isShiftDown()) {
                    RotateFace(face1, true, cubes);
                    cube.turn("F", "CCW");
                } else if (e.getCode().equals(KeyCode.F)) {
                    RotateFace(face1, false, cubes);
                    cube.turn("F", "CW");
                } else if (e.getCode().equals(KeyCode.B) && e.isShiftDown()){
                    RotateFace(face2, false, cubes);
                    cube.turn("B", "CCW");
                } else {
                    RotateFace(face2, true, cubes);
                    cube.turn("B", "CW");
                }
            }
        });

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
    public static Group createCube(int x, int y, int z, int show1, int show2, int show3, Color color1, Color color2, Color color3) {

        // Define the 8 vertices of the cube
        float halfSize = 100;
        float[] points = {
                // Front face
                -halfSize, -halfSize, halfSize,  // 0: Bottom-left
                halfSize, -halfSize, halfSize,  // 1: Bottom-right
                halfSize,  halfSize, halfSize,  // 2: Top-right
                -halfSize,  halfSize, halfSize,  // 3: Top-left

                // Back face
                -halfSize, -halfSize, -halfSize,  // 4: Bottom-left
                halfSize, -halfSize, -halfSize,  // 5: Bottom-right
                halfSize,  halfSize, -halfSize,  // 6: Top-right
                -halfSize,  halfSize, -halfSize,  // 7: Top-left
        };

        // Add texture coordinates (dummy coordinates to avoid errors)
        float[] texCoords = {
                // Front face (maps to the first section of the texture grid)
                0.0f, 0.0f,  // Top-left of texture (front face)
                0.5f, 0.0f,  // Top-right of texture (front face)
                0.5f, 0.5f,  // Bottom-right of texture (front face)
                0.0f, 0.5f,  // Bottom-left of texture (front face)

                // Right face (maps to the second section)
                0.5f, 0.0f,  // Top-left of texture (right face)
                1.0f, 0.0f,  // Top-right of texture (right face)
                1.0f, 0.5f,  // Bottom-right of texture (right face)
                0.5f, 0.5f,  // Bottom-left of texture (right face)

                // Back face (maps to third section)
                0.0f, 0.5f,  // Top-left of texture (back face)
                0.5f, 0.5f,  // Top-right of texture (back face)
                0.5f, 1.0f,  // Bottom-right of texture (back face)
                0.0f, 1.0f,  // Bottom-left of texture (back face)

                // Left face (maps to fourth section)
                0.5f, 0.5f,  // Top-left of texture (left face)
                1.0f, 0.5f,  // Top-right of texture (left face)
                1.0f, 1.0f,  // Bottom-right of texture (left face)
                0.5f, 1.0f,  // Bottom-left of texture (left face)

                // Top face (maps to fifth section)
                0.0f, 1.0f,  // Top-left of texture (top face)
                0.5f, 1.0f,  // Top-right of texture (top face)
                0.5f, 1.5f,  // Bottom-right of texture (top face)
                0.0f, 1.5f,  // Bottom-left of texture (top face)

                // Bottom face (maps to sixth section)
                0.5f, 1.0f,  // Top-left of texture (bottom face)
                1.0f, 1.0f,  // Top-right of texture (bottom face)
                1.0f, 1.5f,  // Bottom-right of texture (bottom face)
                0.5f, 1.5f   // Bottom-left of texture (bottom face)
        };


        // Define the faces using indices from the points and texture coordinates arrays

        TriangleMesh mesh1 = new TriangleMesh();
        mesh1.getTexCoords().addAll(texCoords);
        mesh1.getFaces().addAll(0, 0, 1, 1, 2, 2,
                2, 2, 3, 3, 0, 0);
        mesh1.getPoints().addAll(points);
        TriangleMesh mesh2 = new TriangleMesh();
        mesh2.getTexCoords().addAll(texCoords);
        mesh2.getFaces().addAll(1, 0, 5, 1, 6, 2,
                6, 2, 2, 3, 1, 0);
        mesh2.getPoints().addAll(points);
        TriangleMesh mesh3 = new TriangleMesh();
        mesh3.getTexCoords().addAll(texCoords);
        mesh3.getFaces().addAll(5, 0, 4, 1, 7, 2,
                7, 2, 6, 3, 5, 0);
        mesh3.getPoints().addAll(points);
        TriangleMesh mesh4 = new TriangleMesh();
        mesh4.getTexCoords().addAll(texCoords);
        mesh4.getFaces().addAll(4, 0, 0, 1, 3, 2,
                3, 2, 7, 3, 4, 0);
        mesh4.getPoints().addAll(points);
        TriangleMesh mesh5 = new TriangleMesh();
        mesh5.getTexCoords().addAll(texCoords);
        mesh5.getFaces().addAll(4, 0, 5, 1, 1, 2,
                1, 2, 0, 3, 4, 0);
        mesh5.getPoints().addAll(points);
        TriangleMesh mesh6 = new TriangleMesh();
        mesh6.getTexCoords().addAll(texCoords);
        mesh6.getFaces().addAll(3, 0, 2, 1, 6, 2,
                6, 2, 7, 3, 3, 0);
        mesh6.getPoints().addAll(points);

        MeshView meshView1 = createMeshView(mesh1, x, y, z);
        MeshView meshView2 = createMeshView(mesh2, x, y, z);
        MeshView meshView3 = createMeshView(mesh3, x, y, z);
        MeshView meshView4 = createMeshView(mesh4, x, y, z);
        MeshView meshView5 = createMeshView(mesh5, x, y, z);
        MeshView meshView6 = createMeshView(mesh6, x, y, z);
        MeshView[] meshViews = new MeshView[] {
                meshView1,
                meshView2,
                meshView3,
                meshView4,
                meshView5,
                meshView6
        };
        Group group = new Group();
        PhongMaterial material = new PhongMaterial(color1);
        material.setSpecularColor(color1);
        PhongMaterial material2 = new PhongMaterial(color2);
        material2.setSpecularColor(color3);
        PhongMaterial material3 = new PhongMaterial(color3);
        material3.setSpecularColor(color2);
        meshViews[show1].setMaterial(material);
        meshViews[show2].setMaterial(material2);
        meshViews[show3].setMaterial(material3);


        group.getChildren().addAll(meshViews[show1], meshViews[show2], meshViews[show3]);
        group.setTranslateX(halfSize);
        group.setTranslateY(halfSize);
        group.setTranslateZ(halfSize);
        return group;
    }

    public static MeshView createMeshView(Mesh mesh, int x, int y, int z) {
        MeshView meshView = new MeshView(mesh);
        meshView.setTranslateX(x);
        meshView.setTranslateY(y);
        meshView.setTranslateZ(z);
        return meshView;
    }

    public static void RotateFace(Group face, boolean direction, Group[] cubes) {
        turningFaces++;
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedtime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    elapsedtime += (l - lastUpdate) / 1_000_000_000.0;
                    if (direction) {
                        face.setRotate(face.getRotate() + 60 * elapsedtime);
                    } else {
                        face.setRotate(face.getRotate() - 60 * elapsedtime);
                    }
                }
                lastUpdate = l;
                if (elapsedtime > 0.15 || turningFaces > 1) {
                    stop();
                    //if (turningFaces <= 1) {
                        updateCube(cubes);
                    //}
                    turningFaces--;
                    face.setRotate(0);

                }
            }
        };
        timer.start();
    }

    public static void updateCube(Group[] cubes) {
        HashMap<String, Color> colors = new HashMap();
        colors.put("red", Color.RED);
        colors.put("orange", Color.ORANGE);
        colors.put("yellow", Color.YELLOW);
        colors.put("green", Color.GREEN);
        colors.put("blue", Color.DODGERBLUE);
        colors.put("white", Color.WHITE);
        //((MeshView) cubes[0].getChildren().get(0)).setMaterial(new PhongMaterial(Color.BLUE));
        HashMap stickerFaces;
        for (int i = 0; i < cube.getCorners().length; ++i) {
            stickerFaces = new HashMap();
            stickerFaces.put(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getColor1());
            stickerFaces.put(cube.getCorners()[i].getFace2(), cube.getCorners()[i].getColor2());
            stickerFaces.put(cube.getCorners()[i].getFace3(), cube.getCorners()[i].getColor3());
            if (compareCorners(cube.getCorners()[i].getFace1(), cube.getCorners()[i].getFace2(), cube.getCorners()[i].getFace3(), cube.getLeft(), cube.getFront(), cube.getUp())) {
                ((MeshView) cubes[6].getChildren().get(0)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getLeft()))));
                ((MeshView) cubes[6].getChildren().get(1)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getFront()))));
                ((MeshView) cubes[6].getChildren().get(2)).setMaterial(new PhongMaterial(colors.get(stickerFaces.get(cube.getUp()))));
                //displayedColors[20] = colors.get(stickerFaces.get(cube.getUp()));
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
        for (int i = 0; i < 8; i++) {
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

    public static void rotateX(Group[] cubes, Group face1, Group face2, boolean clockwise) {
        turningFaces++;
        face1.getChildren().removeAll(face1.getChildren());
        face1.getChildren().addAll(cubes[0], cubes[1], cubes[2], cubes[3]);
        face1.setRotationAxis(Rotate.Z_AXIS);
        face2.getChildren().removeAll(face2.getChildren());
        face2.getChildren().addAll(cubes[4], cubes[5], cubes[6], cubes[7]);
        face2.setRotationAxis(Rotate.Z_AXIS);
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedtime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    elapsedtime += (l - lastUpdate) / 1_000_000_000.0;
                    if (clockwise) {
                        face1.setRotate(face1.getRotate() - 35 * elapsedtime);
                        face2.setRotate(face2.getRotate() - 35 * elapsedtime);
                    } else {
                        face1.setRotate(face1.getRotate() + 35 * elapsedtime);
                        face2.setRotate(face2.getRotate() + 35 * elapsedtime);
                    }
                }
                lastUpdate = l;
                if (elapsedtime > 0.2 || turningFaces > 1) {
                    stop();
                    face1.setRotate(0);
                    face2.setRotate(0);
                    if (clockwise) {
                        cube.rotate("X", "CW");
                    } else {
                        cube.rotate("X", "CCW");
                    }
                    updateCube(cubes);
                    turningFaces--;
                }
            }
        };
        timer.start();
    }
    public static void rotateZ(Group[] cubes, Group face1, Group face2, boolean clockwise) {
        turningFaces++;
        face1.getChildren().removeAll(face1.getChildren());
        face1.getChildren().addAll(cubes[2], cubes[3], cubes[6], cubes[7]);
        face1.setRotationAxis(Rotate.X_AXIS);
        face2.getChildren().removeAll(face2.getChildren());
        face2.getChildren().addAll(cubes[0], cubes[1], cubes[4], cubes[5]);
        face2.setRotationAxis(Rotate.X_AXIS);
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private double elapsedtime = 0;

            @Override
            public void handle(long l) {
                if (lastUpdate > 0) {
                    elapsedtime += (l - lastUpdate) / 1_000_000_000.0;
                    if (clockwise) {
                        face1.setRotate(face1.getRotate() - 35 * elapsedtime);
                        face2.setRotate(face2.getRotate() - 35 * elapsedtime);
                    } else {
                        face1.setRotate(face1.getRotate() + 35 * elapsedtime);
                        face2.setRotate(face2.getRotate() + 35 * elapsedtime);
                    }
                }
                lastUpdate = l;
                if (elapsedtime > 0.2 || turningFaces > 1) {
                    stop();
                    face1.setRotate(0);
                    face2.setRotate(0);
                    if (clockwise) {
                        cube.rotate("Z", "CW");
                    } else {
                        cube.rotate("Z", "CCW");
                    }
                    updateCube(cubes);
                    turningFaces--;
                }
            }
        };
        timer.start();

    }
}