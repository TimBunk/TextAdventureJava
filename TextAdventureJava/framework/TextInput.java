import static org.lwjgl.glfw.GLFW.*;

public class TextInput extends Text {

    private int maxChars;

    TextInput(Font font) {
        super(font, "");
        maxChars = Integer.MAX_VALUE;
    }

    public void update() {
        String s = getString();

        if (s.length() > 0 && (Input.key(GLFW_KEY_BACKSPACE, Key.KeyState.PRESSED) || Input.key(GLFW_KEY_BACKSPACE, Key.KeyState.REPEAT))) {
            s = s.substring(0, s.length() - 1);
        }
        else if (s.length() < maxChars) {
            s += Input.chars();
        }

        setString(s);
    }

    // Getters
    public int getMaxChars() { return maxChars; }
    // Setters
    public void setMaxChars(int maxChars) { this.maxChars = maxChars; }
}