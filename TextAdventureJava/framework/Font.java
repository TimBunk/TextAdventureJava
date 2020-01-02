import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Font {

    Texture texture;
    private HashMap<Character, Glyph> glyphes;

    Font(String filePath) throws IOException {
        glyphes = new HashMap<Character, Glyph>();
        texture = new Texture(String.format("%s%s", filePath, ".png"));
        loadFromFile(filePath);
    }

    private void loadFromFile(String filePath) throws IOException {
        filePath = getClass().getResource(String.format("%s%s", filePath, ".fnt")).getPath();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String currentLine = "";
        float scaleW = 0, scaleH = 0;
        while((currentLine = br.readLine()) != null) {
            String[] array = currentLine.split("=|\\s+");
            if (array.length != 0) {
                if (array[0].equals("char")) {
                    loadGlyphFromString(array, scaleW, scaleH);
                }
                else if (array[0].equals("common")) {
                    scaleW = Float.parseFloat(array[6]);
                    scaleH = Float.parseFloat(array[8]);
                }
            }
        }
    }

    private void loadGlyphFromString(String[] array, float scaleW, float scaleH) {
        char id = (char)Integer.parseInt(array[2]);
        Vector2f uv = new Vector2f(Float.parseFloat(array[4])/scaleW, (scaleH - Float.parseFloat(array[6]))/scaleH);
        Vector2f scale = new Vector2f(Integer.parseInt(array[8]), Integer.parseInt(array[10]));
        Vector2f offset = new Vector2f(Integer.parseInt(array[12]), Integer.parseInt(array[14]));
        int xAdvance = Integer.parseInt(array[16]);
        Glyph g = new Glyph(uv, scale, offset, xAdvance);
        glyphes.put(id, g);
    }

    public void destroy() {
        texture.destroy();
    }
}