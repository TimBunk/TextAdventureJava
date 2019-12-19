import java.util.ArrayList;

/**
 * @author      Ruben Eekhof rubeneekhof@gmail.com
 * The detective class stores its inventory and the items he has picked up.
 */
public class Detective {

    private String name;
    private String description;
    private ArrayList<Item> inventory;

    public Detective() {
        name = "Bruce Caine";
        description = "Bruce Caine, the best detective in the west.";
        inventory = new ArrayList<Item>();
    }

    /**
     * Adds an item to the detectives inventory.
     * Checks if his inventory is already full or not.
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
     * Removes an item from the detectives inventory.
     * Checks if his inventory is already empty or not.
     * @param item, the item object to be removed to the inventory.
     * @return true, if the item was succesfully dropped, false otherwise.
     */
    public boolean drop(Item item) {
        if (inventory.size() == 0) {
            return false;
        } else {
            inventory.remove(item);
            return true;
        }
    }

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

    public Item getInventoryItem(String itemName) {
        for (Item i : inventory) {
            if (i.getName().equals(itemName)) {
                return i;
            }
        }
        return null;
    }
}
