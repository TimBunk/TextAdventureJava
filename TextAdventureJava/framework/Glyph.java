import org.joml.Vector2f;

public class Glyph {

    private Vector2f uv;
    private Vector2f scale;
    private Vector2f offset;
    private int xAdvance;

    Glyph(Vector2f uv, Vector2f scale, Vector2f offset, int xAdvance) {
        this.uv = uv;
        this.scale = scale;
        this.offset = offset;
        this.xAdvance = xAdvance;
    }

    // Getters
    public Vector2f getUv() { return uv; }
    public Vector2f getScale() { return scale; }
    public Vector2f getOffset() { return offset; }
    public int getXAdvance()    { return  xAdvance; }


}