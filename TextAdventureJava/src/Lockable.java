/**
 * @author      Ruben Eekhof rubeneekhof@gmail.com
 */
public class Lockable extends Inspectable {

    private boolean locked;

    public Lockable(String name, String description) {
        super(name, description);
        this.locked = false;
    }
    
    private void unlock() {
        locked = false;
    }

    private void lock() {
        locked = true;
    }

    private boolean isLocked() {
        return this.locked;
    }
}

