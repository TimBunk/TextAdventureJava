import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * @author Tim Bunk
 * Dit is de sprite class in deze class staat alle informatie over een sprite
 */
public class Sprite {
    private Vector2f pivot;
    private Vector2f position;
    private float rotation;
    private int width, height;
    private Texture texture;
    private Vector3f color;
    private float[] vertices;

    /**
     * Constructor van de sprite waar alles geinitialiseerd wordt
     * @param width de breedte van de sprite
     * @param height de hoogte van de sprite
     * @param color de kleur van de sprite
     */
    Sprite(int width, int height, Vector3f color) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.texture = null;
        pivot = new Vector2f(0.0f, 0.0f);
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        vertices = new float[16];
        updateVertices();
    }

    /**
     * Constructor van de sprite waar alles geinitialiseerd wordt
     * @param width de breedte van de sprite
     * @param height de hoogte van de sprite
     * @param texture de texture die deze sprite moet gebruiken
     */
    Sprite(int width, int height, Texture texture) {
        this.width = width;
        this.height = height;
        this.color = new Vector3f(0.0f, 0.0f, 0.0f);
        this.texture = texture;
        pivot = new Vector2f(0.0f, 0.0f);
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        vertices = new float[16];
        updateVertices();
    }

    // Getters
    public Vector3f getColor()      { return color; }
    public Texture getTexture()     { return texture; }
    public float getRotation()      { return rotation; }
    public int getWidth()           { return width; }
    public int getHeight()          { return height; }
    public Vector2f getPosition()   { return position; }
    public Vector2f getPivot()      { return pivot; }
    public float[] getVertices()    { return vertices; }

    // Setters
    public void setColor(Vector3f color)        { this.color = color; }
    public void setTexture(Texture texture)   { this.texture = texture; }
    public void setRotation(float rotation)     { this.rotation = rotation; }
    public void setWidth(int width) {
        this.width = width;
        updateVertices();
    }
    public void setHeight(int height) {
        this.height = height;
        updateVertices();
    }
    public void setPosition(Vector2f position) {
        this.position = position;
        updateVertices();
    }
    public void setPivot(Vector2f pivot) {
        this.pivot = pivot;
        updateVertices();
    }


    private void updateVertices() {
        // Positions                                                                                                            // texture coords
        vertices[0] = (width*(0.5f-(pivot.x*0.5f)) + position.x); vertices[1] = (height*(0.5f-(pivot.y*0.5f)) + position.y);    vertices[2] = 1.0f; vertices[3] = 1.0f;  // top right
        vertices[4] = (width*(0.5f-(pivot.x*0.5f)) + position.x); vertices[5] = (-height*(0.5f-(pivot.y*0.5f)) + position.y);   vertices[6] =1.0f; vertices[7] = 0.0f;  // bottom right
        vertices[8] =(-width*(0.5f-(pivot.x*0.5f)) + position.x); vertices[9] = (-height*(0.5f-(pivot.y*0.5f)) + position.y);   vertices[10] =0.0f; vertices[11] =0.0f;  // bottom left
        vertices[12] =(-width*(0.5f-(pivot.x*0.5f)) + position.x); vertices[13] =(height*(0.5f-(pivot.y*0.5f)) + position.y);   vertices[14] =0.0f; vertices[15] =1.0f; // top left
    }
}