package org.neelesh.demo;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class CubeRenderer {
    public static Group createCornerCube(int x, int y, int z, int show1, int show2, int show3, Color color1, Color color2, Color color3) {
        
        int size = 100;
        float[] points = generateVertices(size);
        
        float[] texCoords = generateTextureCoordinates();

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
        group.setTranslateX(size);
        group.setTranslateY(size);
        group.setTranslateZ(size);
        return group;
    }
    public static Group createEdgeCube(int x, int y, int z, int show1, int show2, Color color1, Color color2) {
        for (int i = 0; i < 6; i++) {
            if (i != show1 && i != show2) {
                return createCornerCube(x, y, z, show1, show2, i, color1, color2, null);
            }
        }
        return null;
    }



    public static MeshView createMeshView(Mesh mesh, int x, int y, int z) {
        MeshView meshView = new MeshView(mesh);
        meshView.setTranslateX(x);
        meshView.setTranslateY(y);
        meshView.setTranslateZ(z);
        return meshView;
    }
    
    private static float[] generateVertices(int size) {
        return new float[] {
                // Front face
                -size, -size, size,  // 0: Bottom-left
                size, -size, size,  // 1: Bottom-right
                size,  size, size,  // 2: Top-right
                -size,  size, size,  // 3: Top-left

                // Back face
                -size, -size, -size,  // 4: Bottom-left
                size, -size, -size,  // 5: Bottom-right
                size,  size, -size,  // 6: Top-right
                -size,  size, -size,  // 7: Top-left
        };
    }

    private static float[] generateTextureCoordinates() {
        return new float[] {
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
    }
}
