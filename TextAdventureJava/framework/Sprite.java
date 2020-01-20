package framework;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * @author Tim Bunk
 * Dit is de sprite class in deze class staat alle informatie over een sprite
 */
public class Sprite {
    private static int counter = 0;
    private Vector2f pivot;
    private Vector2f position;
    private float rotation;
    private int width, height;
    private int textureID;
    private Vector4f color;
    private int layer;
    private int id;

    /**
     * Constructor van de sprite waar alles geinitialiseerd wordt
     *
     * @param width  de breedte van de sprite
     * @param height de hoogte van de sprite
     * @param color  de kleur van de sprite
     */
    public Sprite(int width, int height, Vector4f color) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.textureID = 0;
        pivot = new Vector2f(0.0f, 0.0f);
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        layer = 0;
        id = counter;
        counter++;
    }

    /**
     * Constructor van de sprite waar alles geinitialiseerd wordt
     *
     * @param width     de breedte van de sprite
     * @param height    de hoogte van de sprite
     * @param textureID de texture die deze sprite moet gebruiken
     */
    public Sprite(int width, int height, int textureID) {
        this.width = width;
        this.height = height;
        this.color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        this.textureID = textureID;
        pivot = new Vector2f(0.0f, 0.0f);
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        layer = 0;
        id = counter;
        counter++;
    }

    /**
     * Copy constructor
     * @param copy de sprite waar je een exacte kopie van wilt maken
     */
    public Sprite(Sprite copy) {
        this.width = copy.width;
        this.height = copy.height;
        this.color = new Vector4f(copy.color);
        this.textureID = copy.textureID;
        pivot = new Vector2f(copy.pivot);
        position = new Vector2f(copy.position);
        rotation = copy.rotation;
        layer = copy.layer;
        id = counter;
        counter++;
    }

    // Getters
    public Vector4f getColor() {
        return color;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getRotation() {
        return rotation;
    }

    public int getLayer() {
        return layer;
    }

    public int getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getPivot() {
        return pivot;
    }

    // Setters
    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setPivot(Vector2f pivot) {
        this.pivot = pivot;
    }
}