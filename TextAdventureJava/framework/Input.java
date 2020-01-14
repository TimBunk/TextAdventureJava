package framework;

import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;

import static org.lwjgl.system.dyncall.DynCallback.dcbArgInt;
import static org.lwjgl.system.dyncall.DynCallback.dcbArgPointer;

/**
 * De input class implements GLFWKeyCallbackI en GLFWCharCallbackI
 * De input class krijgt dus info over welke keys er zijn gedrukt doormiddel van GLFW
 */
public class Input implements GLFWKeyCallbackI, GLFWCharCallbackI {

    private static Input input = null;
    private final String SIGNATURE = "(pi)v";
    private HashMap<Integer, Key> keys;
    private String chars;

    /**
     * Constructor input
     */
    public Input() {
        keys = new HashMap<>();
        chars = "";
    }

    /**
     * Initialiseer een static versie van input omdat je maar een input nodig bent in een applicatie
     *
     * @return de static input
     */
    public static Input init() {
        if (input == null) {
            input = new Input();
        }
        return input;
    }

    /**
     * Met deze functie kan je checken of een key daadwerkelijk is ingedrukt
     *
     * @param key   Voorbeeld: GLFW_KEY_SPACE
     * @param state State kan zijn up, down, pressed of repeat
     * @return
     */
    public static boolean key(int key, Key.KeyState state) {
        Key k = input.keys.get(key);
        if (k != null) {
            Key.KeyState keyState = k.getKeyState();
            return (keyState == state || (state == Key.KeyState.REPEAT && k.getRepeat()));
        }
        return false;
    }

    /**
     * @return welke chars zijn ingedrukt op de huidige frame het kan dus zijn dat er een lege string wordt gereturned
     */
    public static String chars() {
        return input.chars;
    }

    /**
     * Update de keys die pressed zijn naar down en setRepeat naar false ook worden de chars leeggemaakt
     */
    public void update() {
        for (HashMap.Entry<Integer, Key> entry : keys.entrySet()) {
            int keyCode = entry.getKey();
            Key k = entry.getValue();
            // Update alle keys die op het moment PRESSED zijn naar DOWN
            if (k.getKeyState() == Key.KeyState.PRESSED) {
                k.setKeyState(Key.KeyState.DOWN);
            }
            k.setRepeat(false);
        }
        // Maak de chars string weer leeg
        chars = "";
    }

    private void handleInput(int key, int action) {
        //System.out.println("Key: " + key + ", action: " + action);
        // Als de key wordt ingedrukt maak een key object aan en stop hem in de hashmap
        if (action == 1) {
            Key k = new Key();
            k.setKeyState(Key.KeyState.PRESSED);
            keys.put(key, k);
        }
        // Als de key nog steeds wordt ingedrukt zet de state dan op REPEAT
        else if (action == 2) {
            keys.get(key).setRepeat(true);
        }
        // Als de key niet meer wordt ingedrukt wordt het uit de hashmap verwijdert
        else {
            keys.remove(key);
        }
    }

    private void handleChar(int key) {
        chars = String.valueOf(Character.toChars(key));
    }

    @Override
    public String getSignature() {
        return SIGNATURE;
    }

    @Override
    public void callback(long args) {
        invoke(
                dcbArgPointer(args),
                dcbArgInt(args),
                dcbArgInt(args),
                dcbArgInt(args),
                dcbArgInt(args)
        );
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (scancode != 0) {
            handleInput(key, action);
        } else {
            handleChar(key);
        }
    }

    @Override
    public void invoke(long window, int codepoint) {
        // Zal nooit gebruikt worden maar is verplicht door de interface GLFWCharCallbackI
    }
}