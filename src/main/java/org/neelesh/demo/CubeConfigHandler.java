package org.neelesh.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CubeConfigHandler {
    private static final Path PATH = Path.of(System.getProperty("user.home"), ".cube3D", "cube_config.json");
    public static void initializeConfig(String[] settings) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(settings[0], String.valueOf(KeyCode.R));
        jsonObject.addProperty(settings[1], String.valueOf(KeyCode.L));
        jsonObject.addProperty(settings[2], String.valueOf(KeyCode.F));
        jsonObject.addProperty(settings[3], String.valueOf(KeyCode.B));
        jsonObject.addProperty(settings[4], String.valueOf(KeyCode.U));
        jsonObject.addProperty(settings[5], String.valueOf(KeyCode.D));
        jsonObject.addProperty(settings[6], String.valueOf(KeyCode.M));
        jsonObject.addProperty(settings[7], String.valueOf(KeyCode.E));
        jsonObject.addProperty(settings[8], String.valueOf(KeyCode.S));
        jsonObject.addProperty(settings[9], String.valueOf(KeyCode.X));
        jsonObject.addProperty(settings[10], String.valueOf(KeyCode.Y));
        jsonObject.addProperty(settings[11], String.valueOf(KeyCode.Z));
        jsonObject.addProperty(settings[12], String.valueOf(KeyCode.SHIFT));
        jsonObject.addProperty(settings[13], String.valueOf(KeyCode.CONTROL));
        jsonObject.addProperty(settings[14], String.valueOf(KeyCode.T));
        jsonObject.addProperty("Turning Animation Speed", 0.15);
        jsonObject.addProperty("Rotation Animation Speed", 0.2);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try  {
            Files.createDirectories(PATH.getParent());
            Files.write(PATH, gson.toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateConfig(String field, Object value) {
        JsonObject jsonObject;
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(PATH);
            jsonObject = new JsonParser().parse(bufferedReader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.addProperty(field, (double) value);
        } catch (RuntimeException e) {
            jsonObject.addProperty(field, (String) value);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try  {
            Files.write(PATH, gson.toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String loadConfig(String field) {
        JsonObject jsonObject;
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(PATH);
            jsonObject = new JsonParser().parse(bufferedReader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.get(field).getAsString();
    }
}
