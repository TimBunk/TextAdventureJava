import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room extends Lockable
{
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Inspectable> inspectables;
    private Person npc;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String name, String description)
    {
        super(name, description);
        inspectables = new ArrayList<Inspectable>();
        exits = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String roomDescription = "You are in the " + name + "\n";
        String objectString = getInspectableObjectsString();
        roomDescription += objectString;
        String exitString = getExitString();
        roomDescription += exitString;
        return  roomDescription;
    }

    /**
     * Creates a formatted string with all the inspectables within the room.
     * @return roomDescription, the concatenated String.
     */
    private String getInspectableObjectsString(){
        String roomDescription;
        if (inspectables.size() != 0) {
            roomDescription = "There are some things in this room: ";
            for (Inspectable i : inspectables) {
                roomDescription += i.getName() + " ";
            }
            roomDescription += "\n";
        } else {
            roomDescription = "This room is empty. \n";
        }
        return roomDescription;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "The exits are: ";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += exit + " ";
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Adds an item to the inspectables list.
     * @param item, the item to add.
     */
    public void addItem(Item item) {
        inspectables.add(item);
    }

    /**
     * @return the size of the inspectables list.
     */
    public int getInspectablesSize() {
        return inspectables.size();
    }

    /**
     * Casts an inspectable object into an item object.
     * @param itemName, the name of the item we are looking for.
     * @return Item, if the item was found, null otherwise.
     */
    public Item getItemObject(String itemName) {
        for (Inspectable i : inspectables) {
            if (i.getName().equals(itemName) && i instanceof Item) {
                return (Item) i;
            }
        }
        return null;
    }

    /**
     * Removes an item from a room
     * @param name, the name of the item we want to remove.
     */
    public void removeItem(Item name) {
        inspectables.remove(name);
    }

}