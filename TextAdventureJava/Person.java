/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 */
public class Person extends Inspectable {

    private Sprite sprite;

    public Person(String name, String description, Sprite sprite) {
        super(name, description);
        this.sprite = sprite;
    }

    // Getters
    public Sprite getSprite() {
        return sprite;
    }
}
