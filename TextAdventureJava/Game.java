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
import java.util.*;

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
    private Person arrested;
    private Room currentRoom;
    private Deque<String> backlog = new ArrayDeque<String>();

    // Text
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
        textNameInventory.setString(Localization.getString(Localization.Text.INVENTORY));
        textInput.setPlaceHolder(Localization.getString(Localization.Text.PLACEHOLDER), new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
        textInput.update();
    }

    @Override
    public void draw() {
        // Teken de npc als er een npc is
        Person npc = currentRoom.getNpc();
        if (npc != null) {
            draw(npc.getSprite());
        }
        // Teken de items in de inventory als die er zijn
        ArrayList<Item> inventory = bruce.getInventory();
        for (int i=0;i<inventory.size();i++) {
            Sprite s = inventory.get(i).getSprite();
            s.setPosition(new Vector2f(64, 335 - (i*72)));
            draw(s);
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
                addToTextLog(Localization.getString(Localization.Text.UNKNOWN_COMMAND));
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

            case ARREST:
                arrest(command.getSecondWord());
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
            addToTextLog(Localization.getString(Localization.Text.UNUSABLE_COMMAND));
        } else {
            String roomToGoBack = backlog.pop();
            Command c = parser.getCommand(Localization.getString(Localization.Commands.GO_COMMAND) + " " + roomToGoBack);
            goRoom(c);
        }
    }

    /**
     * Shows the players inventory.
     */
    private void inventory() {
        addToTextLog(bruce.getInventoryString());
    }

    private void pickUp(String item) {
        Item itemToAdd = currentRoom.getItemObject(item);
        if (itemToAdd != null) {
            if (bruce.pickup(itemToAdd)) {
                currentRoom.removeItem(itemToAdd);
                addToTextLog(String.format("%s%s",Localization.getString(Localization.Items.PICKED_UP), item));
            } else {
                addToTextLog(String.format("%s%s",Localization.getString(Localization.Items.PICKED_UP_FAILED), item));
            }
        } else {
            addToTextLog(Localization.getString(Localization.Items.ITEM_NOT_EXIST));
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
            addToTextLog(Localization.getString(Localization.Items.DROPPED) + droppedItem.getName());
        } else {
            addToTextLog(Localization.getString(Localization.Items.ITEM_NOT_INVENTORY));
        }
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private String goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            addToTextLog(Localization.getString(Localization.Rooms.GO_WHERE));
            return null;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            addToTextLog(Localization.getString(Localization.Rooms.NO_DOOR));
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
            addToTextLog(Localization.getString(Localization.Text.QUIT_WHAT));
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
                else if (bruce.getName().equals(command.getSecondWord())) {
                    addToTextLog(bruce.inspect());
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
            addToTextLog(String.format("%s: %s", bruce.getName(), Localization.getString(questions.get(questionNumber))));
            // Als er een npc in de room is dan gaat hij de vraag beantwoorden
            Person npc = currentRoom.getNpc();
            if (npc != null) {
                String answer = npc.getAnswer(questionNumber);
                if (answer != null) {
                    addToTextLog(String.format("%s: %s", npc.getName(), answer));
                } else {
                    addToTextLog(Localization.getString(Localization.Persons.NO_RESPONSE));
                }
            } else {
                // Als er niemand in de room is dan antwoord er ook niemand
                addToTextLog(Localization.getString(Localization.Persons.NO_RESPONSE));
            }
        }
        // Geef de speler een hint hoe hij ask commando moet gebruiken
        else {
            questionNumberString = String.format("%s { ", Localization.getString(Localization.Commands.ASK_COMMAND));
            for (int i = 1;i<=questions.size();i++) {
                questionNumberString += String.format("%d ", i);
            }
            questionNumberString += "}";
            addToTextLog(questionNumberString);
        }
    }

    private void arrest(String name) {
        // Kijk of er een npc in de room is
        Person npc = currentRoom.getNpc();
        if (npc == null) {
            addToTextLog(Localization.getString(Localization.Persons.ARREST_EMPTY_ROOM));
        }
        // Check of de speler wel een naam heeft opgegeven
        else if(name == null) {
            addToTextLog(Localization.getString(Localization.Persons.ARREST_WHO));
        }
        // Check of naam matcht met de persoon in de kamer
        else if (npc.getName().equals(name) == false) {
            addToTextLog(Localization.getString(Localization.Persons.ARREST_PERSON_NOT_IN_ROOM));
        }
        // Als alles klopt dan arresteeren we die persoon
        else {
            arrested = npc;
        }
    }

    @Override
    public void textInputCallback(String text) {
        addToTextLog(text);
        Command command = parser.getCommand(text);
        processCommand(command);
    }

    private void addToTextLog(String text) {
        String s = textLog.getString();
        s = String.format("%s%s\n", s, text);
        textLog.setString(s);
    }

    public boolean shouldClose() {
        return shouldClose;
    }

    private void setupRooms() {
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

        Sprite spriteVaultKey = new Sprite(64, 64, textureManager.load("Resources/Images/vault-key.png"));
        Sprite spriteDucktape = new Sprite(64, 64, textureManager.load("Resources/Images/duct-tape.png"));
        Sprite spriteShedKey = new Sprite(64, 64, textureManager.load("Resources/Images/shed-key.png"));
        Sprite cellPhoneSprite = new Sprite(64, 64, textureManager.load("Resources/Images/phone.png"));

        Item vaultKey = new Item("vault-key", "Key for the vault located in the bedroom.", spriteVaultKey);
        Item ducktape = new Item("ducktape", "Ducktape? What could this be used for?", spriteDucktape);
        Item shedKey = new Item("shed-key", "A rusty looking key for the shed in the garden.", spriteShedKey);
        Inspectable knifeDisplay = new Inspectable("knive-display", "A display for knives.");
        Inspectable bed = new Inspectable("bed", "This is where brian sleeps with his wife.");
        Inspectable kitchenGarden = new Inspectable("kitchen-garden", "A bunch of crops are being grown here.");
        Inspectable workbench = new Inspectable("workbench", "A bunch of tools are being displayed here.");
        Locker vault = new Locker("vault", "A solid vault, that can withstand some hits.");
        Inspectable deadBody = new Inspectable("Deadbody", "A very dead body.");
        Item cellPhone = new Item("cellphone", "wow its a cellphone.", cellPhoneSprite);

        garden.addInspectable(kitchenGarden);
        bedroom.addInspectable(bed);
        storage.addInspectable(ducktape);
        shed.addInspectable(vaultKey);
        kitchen.addInspectable(knifeDisplay);
        shed.addInspectable(workbench);
        garage.addInspectable(shedKey);
        bedroom.addInspectable(vault);

        vault.lock();
        vault.addContents(cellPhone);

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
        Person wife = new Person("lisa", Localization.Persons.WIFE_DESCRIPTION, spriteWife);
        wife.addAnswer(Localization.Answers.WIFE_1);
        wife.addAnswer(Localization.Answers.WIFE_2);
        Person housemaid = new Person("vianne", Localization.Persons.HOUSEMAID_DESCRIPTION, spriteHousemaid);
        housemaid.addAnswer(Localization.Answers.HOUSE_MAID_1);
        housemaid.addAnswer(Localization.Answers.HOUSE_MAID_2);
        Person chef = new Person("gordon", Localization.Persons.CHEF_DESCRIPTION, spriteChef);
        chef.addAnswer(Localization.Answers.CHEF_1);
        chef.addAnswer(Localization.Answers.CHEF_2);
        Person gardener = new Person("ernesto", Localization.Persons.GARDENER_DESCRIPTION, spriteGardener);
        gardener.addAnswer(Localization.Answers.GARDENER_1);
        gardener.addAnswer(Localization.Answers.GARDENER_2);

        // Plaats de npc's in een kamer
        bedroom.setNpc(wife);
        livingroom.setNpc(housemaid);
        kitchen.setNpc(chef);
        garden.setNpc(gardener);

        // De moordenaar is de chef
        Person[] possibleMuderers = {chef, housemaid, wife, gardener};
        int randomIndex = rand.nextInt(possibleMuderers.length);
        murderer = possibleMuderers[randomIndex];
        arrested = null;

        // Initialiseer de questions
        questions.add(Localization.Questions.QUESTION_1);
        questions.add(Localization.Questions.QUESTION_2);

        switch (murderer.getName()) {

            case "gardener":
                deadBody.setDescription("Brian is covered in bruises, and has a bump on his head.");
                Sprite pillSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pills.png"));
                Item pills = new Item("pills", "Pills to prevent anger attacks, it has a label on it with Ernesto's name", pillSprite);
                kitchen.addInspectable(pills);
                Sprite hammerSprite = new Sprite(64, 64, textureManager.load("Resources/Images/hammer.png"));
                Item hammer = new Item("Hammer", "A hammer that has some blood on the tip.", hammerSprite);
                shed.addInspectable(hammer);
                cellPhone.setDescription("anonymous: WHERE ARE MY FUCKING PILLS? IL HURT YOU!!!!!!");
                break;

            case "housemaid":
                deadBody.setDescription("Brian has a hole in his neck.");
                housemaid.setDescription("She has multiple bruises and it appears someone hit her on her left eye.");
                Sprite pencilSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pencil.png"));
                Item pencil = new Item("pencil", "A pencil with some blood on the tip.", pencilSprite);
                livingroom.addInspectable(pencil);
                Sprite pencilBoxSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pencil-box.png"));
                Item pencilBox = new Item("pencil-box", "This box has 1 missing pencil and its labelled with the name: Vianne", pencilBoxSprite);
                storage.addInspectable(pencilBox);
                cellPhone.setDescription("anonymous: I will resign if you don't stop hurting me!! :(((((");
                break;

            case "chef":
                Sprite poisonSprite = new Sprite(64, 64, textureManager.load("Resources/Images/poison.png"));
                Item poison = new Item("mysterious-bottle", "Hmmm a mysterious bottle with a skull on it, and it smells odd.", poisonSprite);
                kitchen.addInspectable(poison);
                deadBody.setDescription("Brian is looking a little green, and does not seem to be hurt. He has thrown up besides him.");
                cellPhone.setDescription("anonymous: HOW DARE YOU HATE MY FOOD YOU DONKEY, MAYBE I SHOULD PUT YOU IN THE OVEN????");

            case "wife":
                Sprite knifeSprite = new Sprite(64, 64, textureManager.load("Resources/Images/knife.png"));
                Item knife = new Item("knife", "A sharp looking kitchen knife, with some blood on the tip.", knifeSprite);
                livingroom.addInspectable(knife);
                knifeDisplay.setDescription("1 knife appears to be missing from this nicely arranged display.");
                deadBody.setDescription("Brian appears to be stabbed multiple times. He is bloody all over.");
                Sprite diarySprite = new Sprite(64, 64, textureManager.load("Resources/Images/diary.png"));
                Item diary = new Item("diary", "I hope that my husband doesnt find out im cheatin :(((", diarySprite);
                bedroom.addInspectable(diary);
                bed.setDescription("The bed is really messy, and there is some blood on the sheeds.");
                cellPhone.setDescription("Brian: If i ever see you cheating again i will divorce you.");
                break;

        }
    }

    private void setupGraphics() throws IOException {
        // Sprites
        Sprite spriteBruce = new Sprite(128, 128, textureManager.load("Resources/Images/Bruce_Cain.png"));
        spriteBruce.setPosition(new Vector2f(64, 476));
        spriteInventoryBackground = new Sprite(128, 390, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteInventoryBackground.setPosition(new Vector2f(64, 195));
        spriteTextInputBackground = new Sprite(680, 64, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        spriteTextInputBackground.setPosition(new Vector2f(480, 32));
        // Text
        Font font = fontManager.load("Resources/Fonts/OCR_A_Extended");
        textNameDetective = new Text(font, "bruce-caine");
        textNameDetective.setSize(14);
        textNameDetective.setPosition(new Vector2f(0, 412));
        textNameSuspect = new Text(font, "");
        textNameSuspect.setSize(14);
        textNameSuspect.setPosition(new Vector2f(832, 412));
        textNameInventory = new Text(font, "");
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
        textInput.setCallback(this);
        textInput.setMaxChars(70);

        bruce = new Detective("bruce-caine", "Bruce Caine, the best detective in the west.", spriteBruce);
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

    public Person getMurderer() { return murderer; }
    public Person getArrested() { return arrested; }
}
