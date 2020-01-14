package framework;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * @Author Tim Bunk
 * Glyph bevat alle info over een glyph in een font. In deze class staat de uv, scale, offset en xAdvance
 */
public class Glyph {

    private Vector4f uv;
    private final Vector2f scale;
    private Vector2f offset;
    private int xAdvance;

    /**
     * @param uv       uv's van de image
     * @param scale    de grootte in pixels
     * @param offset   offset van de standaard locatie
     * @param xAdvance afstand tussen de huidige glyph en de volgende glyph
     */
    public Glyph(Vector4f uv, Vector2f scale, Vector2f offset, int xAdvance) {
        this.uv = uv;
        this.scale = scale;
        this.offset = offset;
        this.xAdvance = xAdvance;
    }

    // Getters
    // De eerste twee waardes representeren de uv's voor links onder en de laatste twee waardes represeneteren de uv's voor rechts boven
    public Vector4f getUv() {
        return uv;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public int getXAdvance() {
        return xAdvance;
    }


}