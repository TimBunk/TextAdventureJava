package framework;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Tim Bunk
 * Dit is de Text class in deze class staat alle informatie over een visueel stuk text.
 * De tekst is bij default wit en de pivot van de tekst is links boven
 */
public class Text {

    private Font font;
    private Vector2f position;
    private float rotation;
    private float size;
    private Vector4f color;
    private Queue<String> lines;
    private float maxWidth, maxHeight;
    private String string;

    /**
     * Bij default wordt de kleur van de tekst op wit gezet
     *
     * @param font   de font die gebruikt moet worden voor dit stuk tekst
     * @param string de charaters die dit stuk tekst bevat
     */
    public Text(Font font, String string) {
        this.font = font;
        position = new Vector2f(0.0f, 0.0f);
        rotation = 0.0f;
        size = font.getSize();
        color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        lines = new LinkedList<String>();
        maxWidth = -1.0f;
        maxHeight = -1.0f;
        setString(string);
    }

    // Getters
    public Font getFont() {
        return font;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public Vector4f getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public String getString() {
        return string;
    }

    public Queue<String> getLines() {
        return lines;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    // Setters
    public void setFont(Font font) {
        this.font = font;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setSize(float size) {
        this.size = size;
        setString(string);
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        setString(string);
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
        setString(string);
    }

    /**
     * De setString functie zorgt ervoor dat elke lijn text die er moet komen in een queue wordt gezet.
     * Als de maxWidth is bereikt wordt de rest automatisch op de volgende lijn gezet en als de maxHeight is bereikt wordt de eerste lijn verwijdert
     * @param string de string die je wilt zetten voor de text
     */
    public void setString(String string) {
        this.string = string;
        lines.clear();
        float size = this.size / font.getSize();
        float posY = 0.0f;
        String[] linesArray = string.split("\\n");
        for (String l : linesArray) {
            if (maxWidth <= 0.0f) {
                lines.add(l);
                posY += this.size;
                if (maxHeight > 0.0f && posY > maxHeight) {
                    lines.remove();
                    posY -= this.size;
                }
            } else {
                String[] wordsArray = l.split("\\s");
                float posX = 0.0f;
                float spaceXAdvance = font.getGlyph(' ').getXAdvance() * size;
                String newString = "";
                for (String w : wordsArray) {

                    float wordXAdvance = 0.0f;
                    for (int i = 0; i < w.length(); i++) {
                        char c = string.charAt(i);
                        Glyph g = font.getGlyph(c);
                        wordXAdvance += (g.getXAdvance() * size);
                    }
                    posX += wordXAdvance;
                    if (newString.equals("") || posX <= maxWidth) {
                        newString += w;
                    } else {
                        if (newString.charAt(newString.length() - 1) == ' ') {
                            newString = newString.substring(0, newString.length() - 1);
                        }
                        lines.add(newString);
                        posY += this.size;
                        if (maxHeight > 0.0f && posY > maxHeight) {
                            lines.remove();
                            posY -= this.size;
                        }
                        newString = w;
                        posX = wordXAdvance;
                    }
                    newString += " ";
                    posX += spaceXAdvance;

                }

                if (newString.equals("") == false) {
                    lines.add(newString);
                    posY += this.size;
                    if (maxHeight > 0.0f && posY > maxHeight) {
                        lines.remove();
                        posY -= this.size;
                    }
                }
            }

        }
    }

}