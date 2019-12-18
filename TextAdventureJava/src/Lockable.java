/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * All objects that can be locked.
 */
public class Lockable extends Inspectable {

    private boolean locked;

    public Lockable(String name, String description) {
        super(name, description);
        this.locked = false;
    }

    /**
     * Unlocks an object.
     * @param lockableObject, the object to be unlocked
     * @return true, if unlocked succesfully, false otherwise.
     */
    private boolean unlock(Lockable lockableObject) {
        if (locked == true) {
            locked = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Locks an object.
     * @param lockableObject, the object to be locked.
     * @return true, if locked succesfully, false otherwise.
     */
    private boolean lock(Lockable lockableObject) {
        if (locked == false) {
            locked = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if an object is locked or not.
     * @return true, if the objects is locked, false otherwise.
     */
    private boolean isLocked() {
        return this.locked;
    }
}

