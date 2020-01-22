package framework;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

/**
 * TextInput is een extensie van Text en maakt gebruik van de input class dus die moet geinitialiseerd zijn voor het gebruik van deze class
 */
public class TextInput extends Text {

    private String placeHolderString;
    private Vector4f placeHolderColor;
    private Vector4f backupColor;
    private boolean empty;
    private int maxChars;
    private TextInputCallbackI textInputCallbackI;

    /**
     * De constructor van de textInput
     * @param font de font die gebruikt moet worden voor de text
     */
    public TextInput(Font font) {
        super(font, "");
        placeHolderString = "";
        placeHolderColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        backupColor = getColor();
        empty = true;
        maxChars = Integer.MAX_VALUE;
        textInputCallbackI = null;
    }

    /**
     * Update zorgt ervoor dat alle text die getypt is wordt opgehaald en als er geen text is getypt wordt er eventueel een placeholder neergezet
     */
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

    /**
     * Deze functie zet het maximaal aantal chars dat er mag worden ingetypt
     * @param maxChars het maximaal aantal chars dat er mag worden ingetypt
     */
    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    /**
     * Deze functie neemt een textInputCallbackI aan en roept deze aan wanneer de gebruiker iets heeft ingetypt en op enter heeft geklikt
     * @param textInputCallbackI
     */
    public void setCallback(TextInputCallbackI textInputCallbackI) {
        this.textInputCallbackI = textInputCallbackI;
    }

    public void setColor(Vector4f color) {
        backupColor = color;
        super.setColor(color);
    }

    /**
     * Deze functie zorgt ervoor dat als de textInput leeg is(Nog niks ingetypt) dan wordt er een tijdelijk stuk tekst voor in de plaats gezet
     * @param placeHolderString een stuk tekst voor de placeholder
     * @param placeHolderColor de kleur van de tekst
     */
    public void setPlaceHolder(String placeHolderString, Vector4f placeHolderColor) {
        this.placeHolderString = placeHolderString;
        this.placeHolderColor = placeHolderColor;
    }
}