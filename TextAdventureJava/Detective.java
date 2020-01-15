import java.util.ArrayList;
import framework.Sprite;
import framework.Text;

/**
 * @author Ruben Eekhof rubeneekhof@gmail.com
 * The detective class stores its inventory and the items he has picked up.
 */
public class Detective {

    private String name;
    private String description;
    private ArrayList<Item> inventory;
    private Sprite sprite;
    private Text text;

    public Detective(String name, String description, Sprite sprite) {
        this.sprite = sprite;
        this.name = name;
        this.description = description;
        inventory = new ArrayList<Item>();
    }

    /**
     * Adds an item to the detectives inventory.
     * Checks if his inventory is already full or not.
     *
     * @param item, the item object to be added to the inventory.
     * @return true, if the item was succesfully added, false otherwise.
     */
    public boolean pickup(Item item) {
        if (inventory.size() == 6) {
            return false;
        } else {
            inventory.add(item);
            return true;
        }
    }

    /**
     * Verwijdert de item uit de inventory list en returnt hem
     *
     * @param itemName, het item object dat verwijdert moet worden
     * @return de item wordt gererturned of als de item niet gevonden wordt, wordt er null gereturned
     */
    public Item drop(String itemName) {
        int i = findItem(itemName);
        if (inventory.size() == 0 || i == -1) {
            return null;
        } else {
            return inventory.remove(i);
        }
    }

    /**
     * Maakt een string display van de players inventory.
     *
     * @return inventoryString
     */
    public String getInventoryString() {
        String inventoryString = "";
        if (inventory.size() == 0) {
            inventoryString += "Your inventory is empty.";
        } else {
            for (Item i : inventory) {
                inventoryString += i.name + " ";
            }
        }
        return inventoryString;
    }

    /**
     * @param itemName de naam vand de item die je zoekt
     * @return returns een index of -1 als de item niet gevonden kon worden
     */
    private int findItem(String itemName) {
        for (int i=0;i<inventory.size();i++) {
            if (inventory.get(i).getName().equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
