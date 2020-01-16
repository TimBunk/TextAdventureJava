import java.util.ArrayList;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * Alle objecten die op slot kunnen.
 */
public class Lockable extends Inspectable {

    private boolean locked;

    public Lockable(String name, String description) {
        super(name, description);
        this.locked = false;
    }

    /**
     * Unlockt het object.
     * @return true, als dit met succes is gedaan, false indien niet.
     */
    public boolean unlock() {
        if (locked) {
            this.locked = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Doet een object op slot.
     * @return true, als het object met succes op slot is gedaan, false indien niet.
     */
    public boolean lock() {
        if (!locked) {
            this.locked = true;
            return true;
        } else {
            return false;
        }
    }
}

