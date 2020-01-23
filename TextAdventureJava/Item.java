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
     * Een getter voor sprite.
     * @return sprite
     */
    public Sprite getSprite() { return sprite; }
}
