package framework;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author Tim Bunk
 * De font class bevat een texture waar alle glyps op afgedrukt staan
 * De font class heeft ook nog een hashmap met glyphs en die glyps hebben info over de uv's
 * De font class verwacht dat je met hierro een font maakt en dat je die inlaadt
 */
public class Font {

    private Texture texture;
    private HashMap<Character, Glyph> glyphes;
    private float size;

    /**
     * De texture en de font bestand worden ingeladen
     *
     * @param filePath path naar de locatie van de font bestand en plaatje. Het kan er als volgt uitzien: "Fonts/OCR_A_Extended"
     * @throws IOException
     */
    Font(String filePath) throws IOException {
        glyphes = new HashMap<Character, Glyph>();
        texture = new Texture(String.format("%s%s", filePath, ".png"));
        size = 1.0f;
        loadFromFile(filePath);
    }

    private void loadFromFile(String filePath) throws IOException {
        filePath = getClass().getResource(String.format("%s%s", filePath, ".fnt")).getPath();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String currentLine = "";
        float scaleW = 0, scaleH = 0;
        while ((currentLine = br.readLine()) != null) {
            String[] array = currentLine.split("=|\\s+");
            if (array.length != 0) {
                if (array[0].equals("char")) {
                    loadGlyphFromString(array, scaleW, scaleH);
                } else if (array[0].equals("common")) {
                    scaleW = Float.parseFloat(array[6]);
                    scaleH = Float.parseFloat(array[8]);
                } else if (array[0].equals("info")) {
                    size = Float.parseFloat(array[4]);
                }
            }
        }
    }

    private void loadGlyphFromString(String[] array, float scaleW, float scaleH) {
        char id = (char) Integer.parseInt(array[2]);
        Vector2f scale = new Vector2f(Integer.parseInt(array[8]), Integer.parseInt(array[10]));
        Vector2f offset = new Vector2f(Integer.parseInt(array[12]), Integer.parseInt(array[14]));
        int xAdvance = Integer.parseInt(array[16]);

        float uvX = Float.parseFloat(array[4]);
        float uvY = (scaleH - Float.parseFloat(array[6]) - scale.y);
        Vector4f uv = new Vector4f(uvX / scaleW, uvY / scaleH, (uvX + scale.x) / scaleW, (uvY + scale.y) / scaleH);

        Glyph g = new Glyph(uv, scale, offset, xAdvance);
        glyphes.put(id, g);
    }

    public void destroy() {
        texture.destroy();
    }

    // Getters
    public Texture getTexture() {
        return texture;
    }

    public Glyph getGlyph(Character c) {
        return glyphes.get(c);
    }

    public float getSize() {
        return size;
    }
}