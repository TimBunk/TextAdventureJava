import java.util.HashMap;

public class Localization {

    // all command words
    public interface Commands {
        String GO_COMMAND = "commands.go.name";
        String QUIT_COMMAND = "text.quit.command";
        String HELP_COMMAND = "commands.help.name";
        String PICKUP_COMMAND = "commands.pickup.name";
        String INVENTORY_COMMAND = "commands.inventory.name";
        String DROP_COMMAND = "commands.drop.name";
        String UNKNOWN_COMMAND = "commands.unknown.name";
        String LOOK_COMMAND = "commands.look.name";
        String BACK_COMMAND = "commands.back.name";
        String LANGUAGE_COMMAND = "commands.language.name";
    }

    // all text
    public interface Text {
        String WELCOME_TEXT = "text.welcome";
        String ROOM_DESCRIPTION = "text.room_long_description.";
        String ROOM_EMPTY = "text.room_empty";
        String ROOM_START_DESCRIPTION = "text.room_start_description";
        String EXIT_STRING = "text.exit_string";
    }

    // all items
    public interface Items {
        String VAULT_KEY_NAME = "item.vault_key_name";
        String VAULT_KEY_DESCRIPTION = "item.vault_key_description";
        String CAR_KEY_NAME = "item.car_key_name";
        String CAR_KEY_DESCRIPTION = "item.car_key_description";
        String SHED_KEY_NAME = "item.shed_key_name";
        String SHED_KEY_DESCRIPTION = "item.shed_key_description";
        String CELLPHONE_NAME = "item.cellphone_name";
        String CELLPHONE_DESCRIPTION = "item.cellphone_description";
        String SHOPPING_LIST_NAME = "item.shopping_list_name";
        String SHOPPING_LIST_DESCRIPTION = "item.shopping_list_description";
        String DUCKTAPE_NAME = "item.ducktape_name";
        String DUCKTAPE_DESCRIPTION = "item.ducktape_description";
        String HAMMER_NAME = "item.hammer_name";
        String HAMMER_DESCRIPTION = "item.hammer_description";
        String KITCHEN_KNIVE_NAME = "item.kitchen_knive_name";
        String KITCHEN_KNIVE_DESCRIPTION = "item.kitchen_knive_description";
        String POISON_NAME = "item.poison_name";
        String POISON_DESCRIPTION = "item.poison_description";
    }

    public interface Rooms {
        String KITCHEN_ROOM_NAME = "room.kitchen.name";
        String KITCHEN_ROOM_DESCRIPTION = "room.kitchen.description";
        String STORAGE_ROOM_NAME = "room.kitchen.name";
        String STORAGE_ROOM_DESCRIPTION = "room.kitchen.description";
        String BEDROOM_ROOM_NAME = "room.bedroom.name";
        String BEDROOM_ROOM_DESCRIPTION = "room.bedroom.description";
        String LIVINGROOM_ROOM_NAME = "room.livingroom.name";
        String LIVINGROOM_ROOM_DESCRIPTION = "room.livingroom.description";
        String GARAGE_ROOM_NAME = "room.garage.name";
        String GARAGE_ROOM_DESCRIPTION = "room.garage.description";
        String GARDEN_ROOM_NAME = "room.garden.name";
        String GARDEN_ROOM_DESCRIPTION = "room.garden.description";
        String SHED_ROOM_NAME = "room.shed.name";
        String SHED_ROOM_DESCRIPTION = "room.shed.description";
    }

    private static String language;
    private static HashMap<String, HashMap<String, String>> dictionary = new HashMap<>();

    // static initializer
    // called when class is loaded by the jvm
    static {
        HashMap<String, String> englishWords = new HashMap<>();
        // adding all commands to the hashmap
        englishWords.put(Commands.GO_COMMAND, "go");
        englishWords.put(Commands.QUIT_COMMAND, "quit");
        englishWords.put(Commands.HELP_COMMAND, "help");
        englishWords.put(Commands.PICKUP_COMMAND, "pickup");
        englishWords.put(Commands.INVENTORY_COMMAND, "inventory");
        englishWords.put(Commands.DROP_COMMAND, "drop");
        englishWords.put(Commands.UNKNOWN_COMMAND, "unknown");
        englishWords.put(Commands.LOOK_COMMAND, "look");
        englishWords.put(Commands.BACK_COMMAND, "back");
        englishWords.put(Commands.LANGUAGE_COMMAND, "language");

        HashMap<String, String> dutchWords = new HashMap<>();
        dutchWords.put(Commands.GO_COMMAND, "verplaats");
        dutchWords.put(Commands.QUIT_COMMAND, "sluit");
        dutchWords.put(Commands.HELP_COMMAND, "help");
        dutchWords.put(Commands.PICKUP_COMMAND, "oppakken");
        dutchWords.put(Commands.INVENTORY_COMMAND, "inventaris");
        dutchWords.put(Commands.DROP_COMMAND, "drop");
        dutchWords.put(Commands.UNKNOWN_COMMAND, "onbekend");
        dutchWords.put(Commands.LOOK_COMMAND, "kijk");
        dutchWords.put(Commands.BACK_COMMAND, "terug");
        dutchWords.put(Commands.LANGUAGE_COMMAND, "taal");

        // adding all the text to the hashmap
        dutchWords.put(Text.WELCOME_TEXT, "Er is iemand vermoord." +
                "\nBruce Cain de beste detective te vinden op deze planeet, moet de moordenaar zien te vinden.\n" +
                "Er zijn 4 verdachten, jij moet als Bruce onderzoek doen naar wie de moordenaar is." +
                "Veel geluk, en type help voor een lijst voor alle commando's.");
        dutchWords.put(Text.ROOM_DESCRIPTION, "Je bent in de ");
        dutchWords.put(Text.ROOM_START_DESCRIPTION, "Er zijn wat dingen in deze kamer: ");
        dutchWords.put(Text.ROOM_EMPTY, "Deze kamer is leeg. \n");
        dutchWords.put(Text.EXIT_STRING, "De uitgangen zijn: ");

        englishWords.put(Text.WELCOME_TEXT, "Someone has been murdered." +
                "\nBruce Cain the best detective on this planet has to find the murderer.\n" +
                "There are 4 suspects, you have to play as Bruce and find the murderer." +
                "Good luck, and type help for a list of all commands.");
        englishWords.put(Text.ROOM_DESCRIPTION, "You are in the ");
        englishWords.put(Text.ROOM_START_DESCRIPTION, "There are some things in this room: ");
        englishWords.put(Text.ROOM_EMPTY, "This room is empty. \n");
        englishWords.put(Text.EXIT_STRING, "The exits are: ");

        dictionary.put("en", englishWords);
        dictionary.put("nl", dutchWords);

        language = "nl";
    }

    public static String getString(final String code) {
        return dictionary.get(language).get(code);
    }

    public static void setLanguage(final String languageCode) {
        language = languageCode;
    }
}
