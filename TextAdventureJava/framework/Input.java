import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.zip.CheckedOutputStream;

import static org.lwjgl.system.dyncall.DynCallback.dcbArgInt;
import static org.lwjgl.system.dyncall.DynCallback.dcbArgPointer;

public class Input implements GLFWKeyCallbackI, GLFWCharCallbackI {

    public enum KeyState {
        UP,
        PRESSED,
        REPEAT,
        DOWN,
    }

    private static Input input = null;
    private final String SIGNATURE = "(pi)v";
    private HashMap<Integer, KeyState> keys;
    private String chars;

    Input() {
        keys = new HashMap<>();
        chars = "";
    }

    public static Input init() {
        if (input == null) {
            input = new Input();
        }
        return input;
    }

    public static boolean key(int key, KeyState state) {
        KeyState keyState = input.keys.get(key);
        if (keyState != null) {
            return (keyState == state || (keyState == KeyState.REPEAT && state == KeyState.DOWN));
        }
        return false;
    }

    public static String chars() {
        return input.chars;
    }

    public void update() {
        for (HashMap.Entry<Integer, KeyState> entry : keys.entrySet()) {
            int key = entry.getKey();
            KeyState state = entry.getValue();
            // Update alle keys die op het moment PRESSED zijn naar DOWN
            if (state== KeyState.PRESSED) {
                keys.replace(key, KeyState.DOWN);
            }
        }
        // Maak de chars string weer leeg
        chars = "";
    }

    private void handleInput(int key, int action) {
        //System.out.println("Key: " + key + ", action: " + action);
        // Als de key wordt ingedrukt maak een key object aan en stop hem in de hashmap
        if (action == 1) {
            keys.put(key, KeyState.PRESSED);
        }
        // Als de key nog steeds wordt ingedrukt zet de state dan op REPEAT
        else if (action == 2) {
            keys.replace(key, KeyState.REPEAT);
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
        }
        else {
            handleChar(key);
        }

        /*char[] chars = Character.toChars(key);
        for (char c : chars) {
            System.out.println("Char: " + c);
        }*/
        //System.out.println("Scancode: " + scancode);
        //if (key == GLFW_KEY_E && action == GLFW_PRESS)

    }

    @Override
    public void invoke(long window, int codepoint) {
        // Zal nooit gebruikt worden maar is verplicht door de interface GLFWCharCallbackI
    }
}