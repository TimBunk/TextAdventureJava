/**
 * @author      Ruben Eekhof rubeneekhof@gmail.com
 */
public class Inspectable {

    protected String name;
    protected String description;

    public Inspectable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String inspect(String item) {
        return this.description;
    }

}
