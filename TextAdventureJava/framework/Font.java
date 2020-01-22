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
        filePath = String.format("%s%s", filePath, ".fnt");
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

    /**
     * Verwijdert de texture uit het geheugen voor deze functie altijd uit voor dat je de applicatie afsluit of wanneer je de font niet meer gaat gebruiken
     */
    public void destroy() {
        texture.destroy();
    }

    // Getters
    /**
     * Deze functie geeft de texture terug voor deze font
     * @return de texture die deze font gebruikts
     */
    public Texture getTexture() {
        return texture;
    }
    /**
     * Geeft een glyph terug gebaseerd op de Character die je meegeeft
     * @param c de character waar je de glyph van wil hebben
     * @return je krijgt de glyph terug die bij de character hoort als er geen glyph is voor die charachter wordt er null gereturned
     */
    public Glyph getGlyph(Character c) {
        return glyphes.get(c);
    }
    /**
     * Je krijgt de standaard grootte van een character in pixels
     * @return returns de standaard grootte van een character in de font in pixels
     */
    public float getSize() {
        return size;
    }
}