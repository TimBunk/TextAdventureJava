import java.util.ArrayList;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * Lockables die unieke items bevatten.
 */
public class Locker extends Lockable {

    private ArrayList<Item> contents = new ArrayList<>();

    public Locker(String name, String description) {
        super(name, description);
    }

    /**
     * Voegt een item toe aan de contens
     * @param item, het item om toe te voegen.
     */
    public void addContents(Item item) {
        contents.add(item);
    }

    /**
     * Verwijdert een item uit de contents.
     * @param item, het item te verwijderen.
     */
    public void removeContents(Item item) {
        contents.remove(item);
    }

    /**
     * Haalt de beschrijving op van de inhoud van het object.
     * @return, de naam van de content.
     */
    public String getContents() {
        return this.contents.get(0).getName();
    }
}
