import framework.Sprite;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * All items that can be picked up by the detective.
 */
public class Item extends Inspectable {

    Sprite sprite;

    public Item(String name, String description, Sprite sprite) {
        super(name, description);
        this.sprite = sprite;
    }


    /**
     * Uses an item.
     *
     * @param item, the item you want to use.
     */
    /*public void use(Item item) {
        System.out.println("Using item " + name);
    }*/


    public Sprite getSprite() { return sprite; }
}
