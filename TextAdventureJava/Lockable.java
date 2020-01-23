import java.util.ArrayList;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * Alle objecten die op slot kunnen.
 */
public class Lockable extends Inspectable {

    private Item key;

    public Lockable(String name, String description) {
        super(name, description);
        this.key = null;
    }

    /**
     * Unlockt het object.
     * @return true, als dit met succes is gedaan, false indien niet.
     */
    public boolean unlock(Item key) {
        if (key == this.key) {
            this.key = null;
            return true;
        }
        return false;
    }

    /**
     * Doet een object op slot.
     */
    public void lock(Item key) {
        this.key = key;
    }

    public Item getKey() {
        return this.key;
    }

    /**
     * Kijkt of een object op slot zit.
     * @return true, als het object op slot zit false indien niet.
     */
    public boolean isLocked() {
        if (this.key != null) {
            return true;
        }
        return false;
    }
}

