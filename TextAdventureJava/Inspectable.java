/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * All objects that can be inspected by the detective.
 */
public class Inspectable {

    private String name;
    private String description;

    public Inspectable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Inspecteer deze inspectable
     *
     * @return je krijgt de beschrijving terug van deze inspectable
     */
    public String inspect() {
        return Localization.getString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return Localization.getString(name);
    }
}
