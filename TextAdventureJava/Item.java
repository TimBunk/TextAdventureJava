/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * All items that can be picked up by the detective.
 */
public class Item extends Inspectable {

    public Item(String name, String description) {
        super(name, description);
    }

    /**
     * Uses an item.
     *
     * @param item, the item you want to use.
     */
    public void use(Item item) {
        System.out.println("Using item " + name);
    }
}
