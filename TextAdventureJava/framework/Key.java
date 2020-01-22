package framework;

/**
 * De Key class bevat informatie over de status van een key.
 * Een key kan pressed, down, up of repeat zijn. Als een key op repeat staat is die ook down
 */
public class Key {
    public enum KeyState {
        UP,
        PRESSED,
        REPEAT,
        DOWN,
    }

    private KeyState keyState;
    private boolean repeat;

    /**
     * Constructor key
     * Repeat staat standaard op false en keyState staat standaard op UP
     */
    public Key() {
        repeat = false;
        keyState = KeyState.UP;
    }

    // Getter

    /**
     * @return Als een key lang durig wordt ingehoudt dan is repeat true en anders false
     */
    public boolean getRepeat() {
        return repeat;
    }

    /**
     * @return Je krijgt UP, PRESSED of DOWN terug van deze functie
     */
    public KeyState getKeyState() {
        return keyState;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Verandert de keyState
     * @param keyState als de keyState repeat is dan wordt repeat op true gezet en de keystate op DOWN
     */
    public void setKeyState(KeyState keyState) {
        if (keyState == KeyState.REPEAT) {
            repeat = true;
            this.keyState = KeyState.DOWN;
        } else {
            this.keyState = keyState;
        }
    }
}