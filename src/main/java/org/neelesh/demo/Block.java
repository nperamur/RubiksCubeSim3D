package org.neelesh.demo;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Block {
    public static Group createCube(int x, int y, int z, Color color1, Color color2, Color color3, Color color4, Color color5, Color color6) {

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
        material2.setSpecularColor(color2);
        PhongMaterial material3 = new PhongMaterial(color3);
        material3.setSpecularColor(color3);
        PhongMaterial material4 = new PhongMaterial(color4);
        material4.setSpecularColor(color4);
        PhongMaterial material5 = new PhongMaterial(color5);
        material5.setSpecularColor(color4);
        PhongMaterial material6 = new PhongMaterial(color6);
        material6.setSpecularColor(color4);
        meshViews[0].setMaterial(material);
        meshViews[1].setMaterial(material2);
        meshViews[2].setMaterial(material3);
        meshViews[3].setMaterial(material4);
        meshViews[4].setMaterial(material5);
        meshViews[5].setMaterial(material6);

        group.getChildren().addAll(meshViews);
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
}
