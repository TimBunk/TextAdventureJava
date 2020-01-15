/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.  Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 * <p>
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import framework.Scene;
import framework.TextInputCallbackI;
import framework.Font;
import framework.Text;
import framework.TextInput;
import framework.Sprite;

public class Game extends Scene implements TextInputCallbackI {

    private boolean shouldClose;
    private Random rand;

    // Game
    private Parser parser;
    private Detective bruce;
    private ArrayList<String> questions;
    private Person murderer;
    private Room currentRoom;
    private Deque<String> backlog = new ArrayDeque<String>();

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

        shouldClose = false;
        rand = new Random();
        parser = new Parser();
        questions = new ArrayList<>();

        setupGraphics();
        setupRooms();

        printWelcome();
    }

    @Override
    public void update(double deltaTime) {
        Person npc = currentRoom.getNpc();
        if (npc != null) {
            textNameSuspect.setString(npc.getName());
        } else {
            textNameSuspect.setString("");
        }
        textInput.update();
    }

    @Override
    public void draw() {
        Person npc = currentRoom.getNpc();
        if (npc != null) {
            draw(npc.getSprite());
        }
        draw(bruce.getSprite());

        draw(spriteInventoryBackground);
        draw(spriteTextInputBackground);

        draw(textNameDetective);
        draw(textNameSuspect);
        draw(textNameInventory);
        draw(textLog);

        draw(textInput);
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        addToTextLog(Localization.getString(Localization.Text.WELCOME_TEXT));
        addToTextLog(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private void processCommand(Command command) {

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                addToTextLog("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                String previousRoom = goRoom(command);
                if (previousRoom != null) {
                    backlog.push(previousRoom);
                }
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

            case INSPECT:
                inspect(command);
                break;

            case ASK:
                ask(command.getSecondWord());
                break;

            case LANGUAGE:
                language(command.getSecondWord());
                break;

            case BACK:
                goBack();
                break;
        }
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        addToTextLog(Localization.getString(Localization.Text.PRINT_HELP));
        addToTextLog(parser.showCommands());
    }


    private void goBack() {
        if (backlog.size() < 1) {
            addToTextLog("You cannot use this command yet.");
        } else {
            String roomToGoBack = backlog.pop();
            Command c = parser.getCommand(Localization.getString(Localization.Commands.GO_COMMAND) + " " + roomToGoBack);
            goRoom(c);
        }
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
        } else {
            addToTextLog("That item does not exist.");
        }
    }

    /**
     * Drops an item from the players inventory
     *
     * @param name, the item you want to drop.
     */
    private void dropItem(String name) {
        Item droppedItem = bruce.drop(name);
        if (droppedItem != null) {
            currentRoom.addInspectable(droppedItem);
            addToTextLog("Dropped: " + droppedItem.getName());
        } else {
            addToTextLog("That item is not in your inventory.");
        }
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private String goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            addToTextLog("Go where?");
            return null;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            addToTextLog("There is no door!");
            return null;
        } else {
            String previousRoom = currentRoom.getName();
            currentRoom = nextRoom;
            addToTextLog(currentRoom.getLongDescription());
            return previousRoom;
        }
    }

    private void language(String languageCode) {
        if (Localization.setLanguage(languageCode)) {
            addToTextLog(Localization.getString(Localization.Text.LANGUAGE_CHANGE_SUCCESS));
        }
        else {
            addToTextLog(Localization.getLanguageOptionsString());
        }
        CommandWords.setCommandWords();
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     *
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            addToTextLog("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Stuurt de beschrijving van de kamer of een inspectable die in de kamer ligt of in bruce zijn inventory zit
     */
    private void look() {
        addToTextLog(currentRoom.getLongDescription());
    }

    private void inspect(Command command) {
        // Als er geen twee woord is vraag dan wat de gebruiker wil inspecteren
        if (command.hasSecondWord() == false) {
            addToTextLog(Localization.getString(Localization.Text.INSPECT_WHAT));
        }
        else {
            // Als er wel een tweede woord is meegegeven dan kijken we of die inspectable in de kamer zit zodat we die beschrijving kunnen printen
            String description = currentRoom.inspectInspectable(command.getSecondWord());
            if (description != null) {
                addToTextLog(description);
            }
            else {
                // Als het inspectable niet in de kamer ligt kan het zijn dat Bruce cain de inspectable in zijn inventory heeft zitten
                description = bruce.inspectItem(command.getSecondWord());
                if (description != null) {
                    addToTextLog(description);
                }
                // Als de inspectable helemaal niet wordt gevonden dan vertellen we de speler dat de inspectable niet is gevonden
                else {
                    addToTextLog(Localization.getString(Localization.Text.ROOM_INSPECTABLE_NON_EXISTENT) + command.getSecondWord());
                }
            }
        }
    }

    private void ask(String questionNumberString) {
        // Check of er een nummer is gegeven
        int questionNumber = extractNumber(questionNumberString);
        questionNumber--;
        if (questionNumber >= 0 && questionNumber < questions.size()) {
            addToTextLog(String.format("%s: %s", bruce.getName(), questions.get(questionNumber)));
            // Als er een npc in de room is dan gaat hij de vraag beantwoorden
            if (currentRoom.getNpc() != null) {
                // TODO: Laat de npc antwoorden
            } else {
                // Als er niemand in de room is dan antwoord er ook niemand
                addToTextLog("No one responded.");
            }
        }
        // Geef de speler een hint hoe hij ask commando moet gebruiken
        else {
            questionNumberString = "ask { ";
            for (int i = 1;i<=questions.size();i++) {
                questionNumberString += String.format("%d ", i);
            }
            questionNumberString += "}";
            addToTextLog(questionNumberString);
        }
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

    public boolean shouldClose() {
        return shouldClose;
    }

    public void setupRooms() {
        // Initialiseer de rooms
        Room kitchen = new Room("kitchen", "This is the kitchen.");
        Room storage = new Room("storage", "This is the storage room.");
        Room bedroom = new Room("bedroom", "This is the bedroom.");
        Room livingroom = new Room("livingroom", "This is the living room.");
        Room garage = new Room("garage", "This is the garage.");
        Room garden = new Room("garden", "This is the garden.");
        Room shed = new Room("shed", "This is the shed.");

        // Set alle uitgangen
        kitchen.setExit(livingroom.getName(), livingroom);
        storage.setExit(livingroom.getName(), livingroom);
        bedroom.setExit(livingroom.getName(), livingroom);
        livingroom.setExit(bedroom.getName(), bedroom);
        livingroom.setExit(storage.getName(), storage);
        livingroom.setExit(kitchen.getName(), kitchen);
        livingroom.setExit(garden.getName(), garden);
        livingroom.setExit(garage.getName(), garage);
        garage.setExit(livingroom.getName(), livingroom);
        garage.setExit(garden.getName(), garden);
        garden.setExit(livingroom.getName(), livingroom);
        garden.setExit(garage.getName(), garage);
        garden.setExit(shed.getName(), shed);
        shed.setExit(garden.getName(), garden);

        // De shed zit op slot
        shed.lock();

        // De ruimte waarin je begint is de livingroom
        currentRoom = livingroom;

        // Initialiseer de items
        Item vaultKey = new Item("vault-key", "Key for the vault located in the bedroom.");
        Item carKey = new Item("car-key", "Key for the car located in the garage.");
        Item shedKey = new Item("shed-key", "Key for the shed locate inside the garden.");
        Item cellphone = new Item("cellphone", "Cellphone that appears to be someones property.");
        Item shoppingList = new Item("shopping-list", "A list with last nights bought groceries.");
        Item ducktape = new Item("ducktape", "Ducktape? What could this be used for?");
        Item hammer = new Item("hammer", "A hammer.");
        Item kitchenKnive = new Item("kitchen-knive", "A knive, used to cut meat and vegetables.");
        Item poison = new Item("poison", "A suspicious looking bottle with a skull on it");

        // Plaats de items in de kamer
        kitchen.addInspectable(shoppingList);
        kitchen.addInspectable(poison);
        kitchen.addInspectable(kitchenKnive);
        bedroom.addInspectable(cellphone);
        bedroom.addInspectable(carKey);
        garage.addInspectable(ducktape);
        garage.addInspectable(shedKey);
        garden.addInspectable(vaultKey);
        garage.addInspectable(hammer);

        // Maak de sprites voor de npcs
        Sprite spriteWife = new Sprite(128, 128, textureManager.load("Resources/Images/women.png"));
        spriteWife.setPosition(new Vector2f(896, 476));
        Sprite spriteHousemaid = new Sprite(128, 128, textureManager.load("Resources/Images/Cleaner.png"));
        spriteHousemaid.setPosition(new Vector2f(896, 476));
        Sprite spriteChef = new Sprite(128, 128, textureManager.load("Resources/Images/Chef.png"));
        spriteChef.setPosition(new Vector2f(896, 476));
        Sprite spriteGardener = new Sprite(128, 128, textureManager.load("Resources/Images/Gardener.png"));
        spriteGardener.setPosition(new Vector2f(896, 476));

        // Initialiseer de npc's
        Person wife = new Person("wife", "The wife of Gary Larry", spriteWife);
        Person housemaid = new Person("house-maid", "The house maid.", spriteHousemaid);
        Person chef = new Person("chef", "Gary Larry's personal cook.", spriteChef);
        Person gardener = new Person("gardener", "The gardener.", spriteGardener);

        // Plaats de npc's in een kamer
        bedroom.setNpc(wife);
        livingroom.setNpc(housemaid);
        kitchen.setNpc(chef);
        garden.setNpc(gardener);

        // De moordenaar is de chef
        murderer = chef;

        // Initialiseer de questions
        questions.add(Localization.getString(Localization.Questions.QUESTION_1));
        questions.add(Localization.getString(Localization.Questions.QUESTION_2));
        questions.add(Localization.getString(Localization.Questions.QUESTION_3));
        questions.add(Localization.getString(Localization.Questions.QUESTION_4));
        questions.add(Localization.getString(Localization.Questions.QUESTION_5));
    }

    public void setupGraphics() throws IOException {
        // Sprites
        Sprite spriteBruce = new Sprite(128, 128, textureManager.load("Resources/Images/Bruce_Cain.png"));
        spriteBruce.setPosition(new Vector2f(64, 476));
        spriteInventoryBackground = new Sprite(128, 390, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteInventoryBackground.setPosition(new Vector2f(64, 195));
        spriteTextInputBackground = new Sprite(680, 64, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteTextInputBackground.setPosition(new Vector2f(480, 32));
        // Text
        font = fontManager.load("Resources/Fonts/OCR_A_Extended");
        textNameDetective = new Text(font, "bruce caine");
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

        bruce = new Detective("Bruce Caine", "Bruce Caine, the best detective in the west.", spriteBruce);
    }

    /**
     * Deze functie zet een string om in een nummer bv "12".
     * @param str de string waar je de nummer van wil hebben
     * @return je krijgt de nummer terug die in de string staat en bij een onzinning waarde zoals "abcwadawdw" krijg je -1
     */
    private int extractNumber(final String str) {
        // Als de string null is of leeg dat betekent automatisch al dat er geen nummer inzit
        if(str == null || str.isEmpty()) return -1;
        // Loop door de string en kijk of alle chars nummers zijn
        StringBuilder sb = new StringBuilder();
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
            }
            else {
                if (sb.toString().isEmpty()) { return -1; }
                break;
            }
        }
        // Maak van de string een integer
        return Integer.parseInt(sb.toString());
    }
}
