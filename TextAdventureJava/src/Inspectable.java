/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * All objects that can be inspected by the detective.
 */
public class Inspectable {

    protected String name;
    protected String description;

    public Inspectable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Inspects an inspectable object.
     * @param item, the object to be inspected.
     * @return description, the objects description field.
     */
    public String inspect(Inspectable item) {
        return this.description;
    }

    public String getName() {
        return name;
    }
}
