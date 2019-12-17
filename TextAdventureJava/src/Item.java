/**
 * @author      Ruben Eekhof rubeneekhof@gmail.com
 */
public class Item extends Inspectable {

    public Item(String name, String description) {
        super(name, description);
    }

    public void use(String name) {
        System.out.println("Using item " + name);
    }
}
