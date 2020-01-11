 

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class Game extends Scene implements TextInputCallbackI
{
    private boolean shouldClose;
    private Parser parser;
    private Detective bruce;
    private Room currentRoom;
    private Random rand;
    private Person murderer;
    private String weapon;

    // Text
    private Font font;
    private Text textNameDetective;
    private Text textNameSuspect;
    private Text textNameInventory;
    private Text textLog;

    // Sprites
    private Sprite spriteInventoryBackground;
    private Sprite spriteTextInputBackground;

    // TextInput
    private TextInput textInput;


    /**
     * Create the game and initialise its internal map.
     */
    public Game() throws IOException {
        // Sprites
        Sprite spriteBruce = new Sprite(128, 128, textureManager.load("Images/Bruce_Cain.png"));
        spriteBruce.setPosition(new Vector2f(64, 476));
        spriteInventoryBackground = new Sprite(128, 390, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteInventoryBackground.setPosition(new Vector2f(64, 195));
        spriteTextInputBackground = new Sprite(680, 64, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteTextInputBackground.setPosition(new Vector2f(480, 32));
        // Text
        font = fontManager.load("Fonts/OCR_A_Extended");
        textNameDetective = new Text(font, "Bruce Caine");
        textNameDetective.setSize(14);
        textNameDetective.setPosition(new Vector2f(0, 412));
        textNameSuspect = new Text(font, "");
        textNameSuspect.setSize(14);
        textNameSuspect.setPosition(new Vector2f(832, 412));
        textNameInventory = new Text(font, "Inventory");
        textNameInventory.setSize(14);
        textNameInventory.setPosition(new Vector2f(21, 390));
        textLog = new Text(font, "");
        textLog.setSize(14);
        textLog.setPosition(new Vector2f(145, 530));
        textLog.setMaxWidth(670);
        textLog.setMaxHeight(470);
        // TextInput
        textInput = new TextInput(font);
        textInput.setSize(14);
        textInput.setPosition(new Vector2f(145, 39));
        textInput.setPlaceHolder("TYPE SOMETHING...", new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
        textInput.setCallback(this);
        textInput.setMaxChars(70);


        shouldClose = false;
        bruce = new Detective("Bruce Caine", "Bruce Caine, the best detective in the west.", spriteBruce);
        parser = new Parser();
        rand = new Random();
        ArrayList<Room> rooms = createRooms();
        ArrayList<Item> items = createItems();
        ArrayList<Person> npcs = createNpcs();
        addItemsToRooms(items, rooms);

        printWelcome();
    }

    @Override
    public void update(double deltaTime) {
        textNameSuspect.setString(murderer.getName());
        textInput.update();
    }

    @Override
    public void draw() {
        draw(bruce.getSprite());
        draw(murderer.getSprite());

        draw(spriteInventoryBackground);
        draw(spriteTextInputBackground);

        draw(textNameDetective);
        draw(textNameSuspect);
        draw(textNameInventory);
        draw(textLog);

        draw(textInput);
    }

    /**
     * Creates all npc objects and adds them to a local ArrayList.
     */
    private ArrayList<Person> createNpcs() {
        // Maak de sprites voor de npcs
        Sprite spriteWife = new Sprite(128, 128, textureManager.load("Images/women.png"));
        spriteWife.setPosition(new Vector2f(896, 476));
        Sprite spriteHousemaid = new Sprite(128, 128, textureManager.load("Images/Cleaner.png"));
        spriteHousemaid.setPosition(new Vector2f(896, 476));
        Sprite spriteChef = new Sprite(128, 128, textureManager.load("Images/Chef.png"));
        spriteChef.setPosition(new Vector2f(896, 476));
        Sprite spriteGardener = new Sprite(128, 128, textureManager.load("Images/Gardener.png"));
        spriteGardener.setPosition(new Vector2f(896, 476));

        // initalising all npcs
        Person wife = new Person("Wife", "The wife of Gary Larry", spriteWife);
        Person housemaid = new Person("House-maid", "The house maid.", spriteHousemaid);
        Person chef = new Person("Chef", "Gary Larry's personal cook.", spriteChef);
        Person gardener = new Person("Gardener", "The gardener.", spriteGardener);

        // adding all npcs to an arraylist
        ArrayList<Person> npcs = new ArrayList<Person>();
        npcs.add(wife);
        npcs.add(housemaid);
        npcs.add(chef);
        npcs.add(gardener);

        // generating the murderer
        int index = rand.nextInt(npcs.size());
        murderer = npcs.get(index);

        return npcs;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private ArrayList<Room> createRooms()
    {
        // initialising all the rooms
        Room kitchen = new Room("Kitchen", "This is the kitchen.");
        Room storage = new Room("Storage", "This is the storage room.");
        Room bedroom = new Room("Bedroom", "This is the bedroom.");
        Room livingroom = new Room("Living Room", "This is the living room.");
        Room garage = new Room("Garage", "This is the garage.");
        Room garden = new Room("Garden", "This is the garden.");
        Room shed = new Room("Shed", "This is the shed.");

        // initialising all the exits
        kitchen.setExit("livingroom", livingroom);
        storage.setExit("livingroom", livingroom);
        bedroom.setExit("livingroom", livingroom);
        livingroom.setExit("bedroom", bedroom);
        livingroom.setExit("storage", storage);
        livingroom.setExit("kitchen", kitchen);
        livingroom.setExit("garden", garden);
        livingroom.setExit("garage", garage);
        garage.setExit("livingroom", livingroom);
        garage.setExit("garden", garden);
        garden.setExit("livingroom", livingroom);
        garden.setExit("garage", garage);
        shed.setExit("garden", garden);

        // saving each room to an arraylist
        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.add(kitchen);
        rooms.add(storage);
        rooms.add(bedroom);
        rooms.add(livingroom);
        rooms.add(garage);
        rooms.add(garden);
        rooms.add(shed);

        // initialising the room the player starts in
        int index = rand.nextInt(rooms.size());
        currentRoom = rooms.get(index);

        return rooms;
    }


    /**
     * Create all the rooms.
     */
    private ArrayList<Item> createItems() {
        // initialising all the items
        Item vaultKey = new Item("vault-key", "Key for the vault located in the bedroom.");
        Item carKey = new Item("car-key", "Key for the car located in the garage.");
        Item shedKey = new Item("shed-key", "Key for the shed locate inside the garden.");
        Item cellphone = new Item("cellphone", "Cellphone that appears to be someones property.");
        Item shoppingList = new Item("shopping-list", "A list with last nights bought groceries.");
        Item ducktape = new Item("ducktape", "Ducktape? What could this be used for?");
        Item hammer = new Item("hammer", "A hammer.");
        Item kitchenKnive = new Item("kitchen-knive", "A knive, used to cut meat and vegetables.");
        Item poison = new Item("poison", "A suspicious looking bottle with a skull on it");

        // adding the items to an arraylist
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(vaultKey);
        items.add(carKey);
        items.add(shedKey);
        items.add(cellphone);
        items.add(shoppingList);
        items.add(ducktape);
        items.add(hammer);
        items.add(kitchenKnive);
        items.add(poison);

        return items;
    }

    /**
     * Adds items to each room randomly.
     */
    private void addItemsToRooms(ArrayList<Item> items, ArrayList<Room> rooms) {
        for (Item i : items) {
            boolean itemAdded = false;
            while (!itemAdded) {
                int index = rand.nextInt(rooms.size());
                Room room = rooms.get(index);
                int amountOfItems = room.getInspectablesSize();
                if (amountOfItems < 2) {
                    room.addItem(i);
                    itemAdded = true;
                } else {
                    rooms.remove(room);
                }
            }
        }
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        /*printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        addToTextLog("Thank you for playing.  Good bye.");*/
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        addToTextLog("");
        addToTextLog("Welcome to the World of Zuul!");
        addToTextLog("World of Zuul is a new, incredibly boring adventure game.");
        addToTextLog("Type '" + CommandWord.HELP + "' if you need help.");
        addToTextLog("");
        addToTextLog(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                addToTextLog("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                shouldClose = quit(command);
                break;

            case PICKUP:
                pickUp(command.getSecondWord());
                break;

            case INVENTORY:
                inventory();
                break;

            case DROP:
                dropItem(command.getSecondWord());
                break;

            case LOOK:
                look();
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        addToTextLog("You are lost. You are alone. You wander");
        addToTextLog("around at the university.");
        addToTextLog("");
        addToTextLog("Your command words are:");
        parser.showCommands();
    }

    /**
     * Shows the players inventory.
     */
    public void inventory() {
        addToTextLog(bruce.getInventoryString());
    }

    private void pickUp(String item) {
        Item itemToAdd = currentRoom.getItemObject(item);
        if (itemToAdd != null) {
            if (bruce.pickup(itemToAdd)) {
                currentRoom.removeItem(itemToAdd);
                addToTextLog("Picked up: " + item);
            } else {
                addToTextLog("Could not pick up: " + item);
            }
        }  else {
            addToTextLog("That item does not exist.");
        }
    }

    /**
     * Drops an item from the players inventory
     * @param name, the item you want to drop.
     */
    private void dropItem(String name) {
        Item itemToDrop = bruce.getInventoryItem(name);
        if (itemToDrop != null) {
            if (bruce.drop(itemToDrop)) {
                currentRoom.addItem(itemToDrop);
                addToTextLog("Dropped: " + name);
            } else {
                addToTextLog("Could not drop: " + name);
            }
        } else {
            addToTextLog("That item is not in your inventory.");
        }
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            addToTextLog("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            addToTextLog("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            addToTextLog(currentRoom.getLongDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            addToTextLog("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Stuurt de beschrijving van de kamer.
     */
    private void look() {
        addToTextLog(currentRoom.getLongDescription());
    }

    @Override
    public void textInputCallback(String text) {
        addToTextLog(text);
        Command command = parser.getCommand(text);
        processCommand(command);
    }

    public void addToTextLog(String text) {
        String s = textLog.getString();
        s = String.format("%s%s\n", s, text);
        textLog.setString(s);
    }

    public boolean shouldClose() { return shouldClose; }
}
