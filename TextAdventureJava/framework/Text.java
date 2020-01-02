import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * @author Tim Bunk
 * Dit is de Text class in deze class staat alle informatie over een visueel stuk text.
 * De tekst is bij default wit en de pivot van de tekst is links boven
 */
public class Text {

    private Font font;
    private String string;
    private Vector2f position;
    private float rotation;
    private Vector4f color;

    /**
     * Bij default wordt de kleur van de tekst op wit gezet
     * @param font de font die gebruikt moet worden voor dit stuk tekst
     * @param string de charaters die dit stuk tekst bevat
     */
    Text(Font font, String string) {
        this.font = font;
        this.string = string;
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    // Getters
    public Font getFont()           { return font; }
    public String getString()       { return string; }
    public Vector2f getPosition()   { return position; }
    public float getRotation()      { return rotation; }
    public Vector4f getColor()      { return color; }

    // Setters
    public void setFont(Font font)              { this.font = font; }
    public void setString(String string)        { this.string = string; }
    public void setPosition(Vector2f position)  { this.position = position; }
    public void setRotation(float rotation)     { this.rotation = rotation; }
    public void setColor(Vector4f color)        { this.color = color; }

}