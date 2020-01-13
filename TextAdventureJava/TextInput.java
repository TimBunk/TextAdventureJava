import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class TextInput extends Text {

    private String placeHolderString;
    private Vector4f placeHolderColor;
    private Vector4f backupColor;
    private boolean empty;
    private int maxChars;
    private TextInputCallbackI textInputCallbackI;

    TextInput(Font font) {
        super(font, "");
        placeHolderString = "";
        placeHolderColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        backupColor = getColor();
        empty = true;
        maxChars = Integer.MAX_VALUE;
        textInputCallbackI = null;
    }

    public void update() {
        String c = Input.chars();
        // Empty is true als er nog geen characters zijn getypt
        if (empty) {
            // Als er een char is getypt dan wordt empty false en wordt de placeholder uit gezet
            if (c != "") {
                empty = false;
                activatePlaceholder(false);
            }
            // Anders wordt de placeholder aan gezet
            else {
                activatePlaceholder(true);
            }
        }

        if (empty == false) {
            // Get de string
            String s = getString();
            // Als er op enter wordt geklikt run dan de callback
            if (textInputCallbackI != null && Input.key(GLFW_KEY_ENTER, Key.KeyState.PRESSED)) {
                textInputCallbackI.textInputCallback(s);
                activatePlaceholder(true);
                empty = true;
                return;
            }
            // Als er backspace wordt geklikt verwijder dan de laatse char van de string
            else if (s.length() > 0 && (Input.key(GLFW_KEY_BACKSPACE, Key.KeyState.PRESSED) || Input.key(GLFW_KEY_BACKSPACE, Key.KeyState.REPEAT))) {
                s = s.substring(0, s.length() - 1);
                // Als de string helemaal leeg is wordt de placeholder actief gezet
                if (s.length() == 0) {
                    activatePlaceholder(true);
                    empty = true;
                    return;
                }
            }
            // Voeg chars toe aan de string zolang de lengte van string niet langer is dan max chars
            else if (s.length() < maxChars) {
                s += c;
            }
            // Set de string
            setString(s);
        }
    }

    private void activatePlaceholder(boolean activate) {
        if (activate) {
            setString(placeHolderString);
            super.setColor(placeHolderColor);
        } else {
            setString("");
            super.setColor(backupColor);
        }
    }

    // Getters
    public int getMaxChars() {
        return maxChars;
    }

    // Setters
    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    public void setCallback(TextInputCallbackI textInputCallbackI) {
        this.textInputCallbackI = textInputCallbackI;
    }

    public void setColor(Vector4f color) {
        backupColor = color;
        super.setColor(color);
    }

    public void setPlaceHolder(String placeHolderString, Vector4f placeHolderColor) {
        this.placeHolderString = placeHolderString;
        this.placeHolderColor = placeHolderColor;
    }
}