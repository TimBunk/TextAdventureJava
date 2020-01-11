 

public class Key {
    public enum KeyState {
        UP,
        PRESSED,
        REPEAT,
        DOWN,
    }

    private KeyState keyState;
    private boolean repeat;

    Key() {
        repeat = false;
        keyState = KeyState.UP;
    }

    // Getter
    public boolean getRepeat() { return repeat; }
    public KeyState getKeyState() { return keyState; }
    // Setters
    public void setRepeat(boolean repeat) { this.repeat = repeat; }

    public void setKeyState(KeyState keyState) {
        if (keyState == KeyState.REPEAT) {
            repeat = true;
            this.keyState = KeyState.DOWN;
        }
        else {
            this.keyState = keyState;
        }
    }
}