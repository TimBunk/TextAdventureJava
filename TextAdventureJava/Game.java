/**
 * De game klasse.
 * Hierin worden alle game rules vastgelegd.
 * @author Ruben Eekhof, Tim Bunk
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
    private Deque<Room> backlog = new ArrayDeque<Room>();
    private ArrayList<Locker> lockers;

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
                Room previousRoom = goRoom(command);
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

            case UNLOCK:
                unlock(command.getSecondWord());
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
            Room roomToGoBack = backlog.pop();
            Command c = parser.getCommand(Localization.getString(Localization.Commands.GO_COMMAND) + " " + roomToGoBack.getName());
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
    private Room goRoom(Command command) {
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
        }
        if (nextRoom.isLocked()) {
            addToTextLog(Localization.getString(Localization.Text.ROOM_LOCKED));
            return null;
        } else {
            Room previousRoom = currentRoom;
            currentRoom = nextRoom;
            addToTextLog(currentRoom.getLongDescription());
            return previousRoom;
        }
    }

    /**
     * Verandert de taal van de game
     * @param languageCode, de taal van de game.
     */
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
        if (!command.hasSecondWord()) {
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

    /**
     * Haalt iets van het slot af.
     * @param lockable, het lockable object dat we van het slot willen halen.
     */
    private void unlock(String lockable) {
        Room roomToUnlock = currentRoom.getExit(lockable);
        // were trying to unlock a room not a locker
        if (roomToUnlock != null) {
            Item key = bruce.containsItem(roomToUnlock.getKey());
            if (roomToUnlock.isLocked() && key != null) {
                roomToUnlock.unlock(key);
                addToTextLog(Localization.getString(Localization.Text.UNLOCKED_ROOM));
            } else {
                addToTextLog(Localization.getString(Localization.Text.COULDNT_UNLOCK));
            }
        // maybe we are trying to unlock a locker?
        } else {
            for(Locker l : lockers) {
                if(l.getName().equals(lockable) || l.getName().toLowerCase().equals(lockable)) {
                    Item itemsInLocker = l.getContents();
                    currentRoom.addInspectable(itemsInLocker);
                    addToTextLog(Localization.getString(Localization.Text.UNLOCKED_SUCCES) + itemsInLocker);
                } else {
                    addToTextLog(Localization.getString(Localization.Text.COULDNT_UNLOCK));
                }
            }
        }
    }

    /**
     * Vraag iets aan een person.
     * @param questionNumberString, het nummer van de vraag die je wilt stellen.
     */
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

    /**
     * Arresteert de verdachte
     * @param name, de naam van de verdachte.
     */
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
        Room kitchen = new Room(Localization.Rooms.KITCHEN_ROOM_NAME, Localization.Rooms.KITCHEN_ROOM_DESCRIPTION);
        Room storage = new Room(Localization.Rooms.STORAGE_ROOM_NAME, Localization.Rooms.STORAGE_ROOM_DESCRIPTION);
        Room bedroom = new Room(Localization.Rooms.BEDROOM_ROOM_NAME, Localization.Rooms.BEDROOM_ROOM_DESCRIPTION);
        Room livingroom = new Room(Localization.Rooms.LIVINGROOM_ROOM_NAME, Localization.Rooms.LIVINGROOM_ROOM_DESCRIPTION);
        Room garage = new Room(Localization.Rooms.GARAGE_ROOM_NAME, Localization.Rooms.GARAGE_ROOM_DESCRIPTION);
        Room garden = new Room(Localization.Rooms.GARDEN_ROOM_NAME, Localization.Rooms.GARDEN_ROOM_DESCRIPTION);
        Room shed = new Room(Localization.Rooms.SHED_ROOM_NAME, Localization.Rooms.SHED_ROOM_DESCRIPTION);

        // Set alle uitgangen
        kitchen.setExit(livingroom);
        storage.setExit(livingroom);
        bedroom.setExit(livingroom);
        livingroom.setExit(bedroom);
        livingroom.setExit(storage);
        livingroom.setExit(kitchen);
        livingroom.setExit(garden);
        livingroom.setExit(garage);
        garage.setExit(livingroom);
        garage.setExit(garden);
        garden.setExit(livingroom);
        garden.setExit(garage);
        garden.setExit(shed);
        shed.setExit(garden);

        // De ruimte waarin je begint is de livingroom
        currentRoom = livingroom;

        Sprite spriteVaultKey = new Sprite(64, 64, textureManager.load("Resources/Images/vault-key.png"));
        Sprite spriteDucktape = new Sprite(64, 64, textureManager.load("Resources/Images/duct-tape.png"));
        Sprite spriteShedKey = new Sprite(64, 64, textureManager.load("Resources/Images/shed-key.png"));
        Sprite cellPhoneSprite = new Sprite(64, 64, textureManager.load("Resources/Images/phone.png"));

        Item vaultKey = new Item(Localization.Items.VAULT_KEY_NAME, Localization.Items.VAULT_KEY_DESCRIPTION, spriteVaultKey);
        Item ducktape = new Item(Localization.Items.DUCKTAPE_NAME, Localization.Items.DUCKTAPE_DESCRIPTION, spriteDucktape);
        Item shedKey = new Item(Localization.Items.SHED_KEY_NAME, Localization.Items.SHED_KEY_DESCRIPTION, spriteShedKey);
        Inspectable knifeDisplay = new Inspectable(Localization.Items.KNIFE_DISPLAY_NAME, Localization.Items.KNIFE_DISPLAY_DESCRIPTION);
        Inspectable bed = new Inspectable(Localization.Items.BED_NAME, Localization.Items.BED_DESCRIPTION);
        Inspectable kitchenGarden = new Inspectable(Localization.Items.KITCHEN_GARDEN_NAME, Localization.Items.KITCHEN_GARDEN_DESCRIPTION);
        Inspectable workbench = new Inspectable(Localization.Items.WORKBENCH_NAME, Localization.Items.WORKBENCH_DESCRIPTION);
        Locker vault = new Locker(Localization.Items.VAULT_NAME, Localization.Items.VAULT_DESCRIPTION);
        Inspectable deadBody = new Inspectable(Localization.Items.DEAD_BODY_NAME, "");
        Item cellPhone = new Item(Localization.Items.CELLPHONE_NAME, "", cellPhoneSprite);

        garden.addInspectable(kitchenGarden);
        bedroom.addInspectable(bed);
        storage.addInspectable(ducktape);
        shed.addInspectable(vaultKey);
        kitchen.addInspectable(knifeDisplay);
        shed.addInspectable(workbench);
        garage.addInspectable(shedKey);
        bedroom.addInspectable(vault);
        livingroom.addInspectable(deadBody);

        vault.lock(vaultKey);
        shed.lock(shedKey);
        vault.addContents(cellPhone);

        lockers = new ArrayList<>();
        lockers.add(vault);

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
        Person wife = new Person(Localization.Persons.WIFE_NAME, Localization.Persons.WIFE_DESCRIPTION, spriteWife);
        wife.addAnswer(Localization.Answers.WIFE_1);
        wife.addAnswer(Localization.Answers.WIFE_2);
        Person housemaid = new Person(Localization.Persons.HOUSEMAID_NAME, Localization.Persons.HOUSEMAID_DESCRIPTION, spriteHousemaid);
        housemaid.addAnswer(Localization.Answers.HOUSE_MAID_1);
        housemaid.addAnswer(Localization.Answers.HOUSE_MAID_2);
        Person chef = new Person(Localization.Persons.CHEF_NAME, Localization.Persons.CHEF_DESCRIPTION, spriteChef);
        chef.addAnswer(Localization.Answers.CHEF_1);
        chef.addAnswer(Localization.Answers.CHEF_2);
        Person gardener = new Person(Localization.Persons.GARDENER_NAME, Localization.Persons.GARDENER_DESCRIPTION, spriteGardener);
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

            case "ernesto":
                deadBody.setDescription(Localization.Items.DEAD_BODY_DESCRIPTION_GARDENER);
                cellPhone.setDescription(Localization.Items.CELLPHONE_DESCRIPTION_GARDENER);
                Sprite pillSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pills.png"));
                Item pills = new Item(Localization.Items.PILLS_NAME, Localization.Items.PILLS_DESCRIPTION, pillSprite);
                kitchen.addInspectable(pills);
                Sprite hammerSprite = new Sprite(64, 64, textureManager.load("Resources/Images/hammer.png"));
                Item hammer = new Item(Localization.Items.HAMMER_NAME, Localization.Items.HAMMER_DESCRIPTION, hammerSprite);
                shed.addInspectable(hammer);
                break;

            case "vianne":
                deadBody.setDescription(Localization.Items.DEAD_BODY_DESCRIPTION_HOUSEMAID);
                cellPhone.setDescription(Localization.Items.CELLPHONE_DESCRIPTION_HOUSEMAID);
                housemaid.setDescription(Localization.Persons.HOUSEMAID_DESCRIPTION2);
                Sprite pencilSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pencil.png"));
                Item pencil = new Item(Localization.Items.PENCIL_NAME, Localization.Items.PENCIL_DESCRIPTION, pencilSprite);
                livingroom.addInspectable(pencil);
                Sprite pencilBoxSprite = new Sprite(64, 64, textureManager.load("Resources/Images/pencil-box.png"));
                Item pencilBox = new Item(Localization.Items.PENCIL_BOX_NAME, Localization.Items.PENCIL_BOX_DESCRIPTION, pencilBoxSprite);
                storage.addInspectable(pencilBox);
                break;

            case "gordon":
                deadBody.setDescription(Localization.Items.DEAD_BODY_DESCRIPTION_CHEF);
                cellPhone.setDescription(Localization.Items.CELLPHONE_DESCRIPTION_CHEF);
                Sprite poisonSprite = new Sprite(64, 64, textureManager.load("Resources/Images/poison.png"));
                Item poison = new Item(Localization.Items.POISON_NAME, Localization.Items.POISON_DESCRIPTION, poisonSprite);
                kitchen.addInspectable(poison);

            case "lisa":
                deadBody.setDescription(Localization.Items.DEAD_BODY_DESCRIPTION_WIFE);
                cellPhone.setDescription(Localization.Items.CELLPHONE_DESCRIPTION_WIFE);
                Sprite knifeSprite = new Sprite(64, 64, textureManager.load("Resources/Images/knife.png"));
                Item knife = new Item(Localization.Items.KITCHEN_KNIVE_NAME, Localization.Items.KITCHEN_KNIVE_DESCRIPTION, knifeSprite);
                livingroom.addInspectable(knife);
                Sprite diarySprite = new Sprite(64, 64, textureManager.load("Resources/Images/diary.png"));
                Item diary = new Item(Localization.Items.DIARY_NAME, Localization.Items.DIARY_DESCRIPTION, diarySprite);
                bedroom.addInspectable(diary);
                knifeDisplay.setDescription(Localization.Items.KNIFE_DISPLAY_DESCRIPTION_WIFE);
                bed.setDescription(Localization.Items.BED_DESCRIPTION_WIFE);
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

        bruce = new Detective(Localization.Persons.DETECTIVE_NAME, Localization.Persons.DETECTIVE_DESCRIPTION, spriteBruce);
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
