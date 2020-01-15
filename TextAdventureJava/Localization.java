import java.util.HashMap;


/**
 * Localization gaat over het opslaan van de vertalingen van de game.
 * @author: Ruben Eekhof
 */
public class Localization {

    /**
     * Een interface die alle dictionary keys opslaat voor de commandos.
     * Dit doen we ook voor alle andere objecten die een vertaling nodig heeft.
     * Dit voorkomt type fouten.
     */
    // alle commandos
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
        String USE_COMMAND = "commands.use.name";
        String INSPECT_COMMAND = "commands.inspect.name";
    }

    // alle text
    public interface Text {
        String WELCOME_TEXT = "text.welcome";
        String ROOM_DESCRIPTION = "text.room_long_description.";
        String ROOM_EMPTY = "text.room_empty";
        String ROOM_START_DESCRIPTION = "text.room_start_description";
        String EXIT_STRING = "text.exit_string";
        String PRINT_HELP = "text.print_help";
        String LANGUAGE_CHANGE_SUCCESS = "text.language_change_success";
        String LANGUAGE_OPTIONS = "text.language_options";
    }

    // alle items
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

    // alle kamers
    public interface Rooms {
        String KITCHEN_ROOM_NAME = "room.kitchen.name";
        String KITCHEN_ROOM_DESCRIPTION = "room.kitchen.description";
        String STORAGE_ROOM_NAME = "room.storage.name";
        String STORAGE_ROOM_DESCRIPTION = "room.storage.description";
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
    
    // alle personen
    public interface Persons {
        String WIFE_NAME = "person.wife.name";
        String WIFE_DESCRIPTION = "person.wife.description"; 
        String GARDENER_NAME = "person.gardener.name";  
        String GARDENER_DESCRIPTION = "person.gardener.description";  
        String CHEF_NAME = "person.chef.name"; 
        String CHEF_DESCRIPTION = "person.chef.description"; 
        String HOUSEMAID_NAME = "person.housemaid.name";
        String HOUSEMAID_DESCRIPTION = "person.housemaid.description";   
    }

    private interface LanguageHashMaps {
        HashMap<String, String> englishWords = new HashMap<>();
        HashMap<String, String> dutchWords = new HashMap<>();
        void addWords(String key, String english, String dutch);
    }

    private static String language;
    // de hashmap waarin we onze data opslaan
    private static HashMap<String, HashMap<String, String>> dictionary = new HashMap<>();

    /**
     * De static intialiser.
     * Deze word aangeroepen wanneer de jvm de klasse compileert.
     */
    static {

        LanguageHashMaps languages = (String key, String english, String dutch) -> { LanguageHashMaps.englishWords.put(key, english); LanguageHashMaps.dutchWords.put(key, dutch); };

        // adding all commands to the hashmap
        languages.addWords(Commands.GO_COMMAND,
                "go",
                "verplaats"
        );
        languages.addWords(Commands.QUIT_COMMAND,
                "quit",
                "sluit"
        );
        languages.addWords(Commands.HELP_COMMAND,
                "help",
                "help"
        );
        languages.addWords(Commands.PICKUP_COMMAND,
                "pickup",
                "oppakken"
        );
        languages.addWords(Commands.INVENTORY_COMMAND,
                "inventory",
                "inventaris"
        );
        languages.addWords(Commands.DROP_COMMAND,
                "drop",
                "drop"
        );
        languages.addWords(Commands.UNKNOWN_COMMAND,
                "unknown",
                "onbekend"
        );
        languages.addWords(Commands.LOOK_COMMAND,
                "look",
                "kijk"
        );
        languages.addWords(Commands.BACK_COMMAND,
                "back",
                "terug"
        );
        languages.addWords(Commands.LANGUAGE_COMMAND,
                "language",
                "taal"
        );
        languages.addWords(Commands.USE_COMMAND,
                "use",
                "gebruik"
        );
        languages.addWords(Commands.INSPECT_COMMAND,
                "inspect",
                "inspecteer"
        );

        // Welkom
        languages.addWords(Text.WELCOME_TEXT,
                "Someone has been murdered." +
                "\nBruce Cain the best detective on this planet has to find the murderer.\n" +
                "There are 4 suspects, you have to play as Bruce and find the murderer." +
                "Good luck, and type help for a list of all commands.",
                "Er is iemand vermoord." +
                "\nBruce Cain de beste detective te vinden op deze planeet, moet de moordenaar zien te vinden.\n" +
                "Er zijn 4 verdachten, jij moet als Bruce onderzoek doen naar wie de moordenaar is." +
                "Veel geluk, en type help voor een lijst voor alle commando's."
        );

        // Kamer beschrijvingen
        languages.addWords(Text.ROOM_DESCRIPTION,
                "You are in the ",
                "Je bent in de "
        );
        languages.addWords(Text.ROOM_START_DESCRIPTION,
                "There are some things in this room: ",
                "Er zijn wat dingen in deze kamer: "
        );
        languages.addWords(Text.ROOM_EMPTY,
                "This room is empty. \n",
                "Deze kamer is leeg. \n"
        );
        languages.addWords(Text.EXIT_STRING,
                "The exits are: ",
                "De uitgangen zijn: "
        );
        languages.addWords(Text.PRINT_HELP,
                "Youre trying to find the rich guys murderer. You've got acces to the following commands:\n",
                "Je probeert de moordenaar van de rijke man te zoeken. Je hebt de volgende commandos:\n"
        );

        languages.addWords(Rooms.BEDROOM_ROOM_NAME,
                "bedroom",
                "slaapkamer"
        );
        languages.addWords(Rooms.BEDROOM_ROOM_DESCRIPTION,
                "The owners bedroom.",
                "De slaapkamer van de eigenaar."
        );
        languages.addWords(Rooms.GARAGE_ROOM_NAME,
                "garage",
                "garage"
        );
        languages.addWords(Rooms.GARAGE_ROOM_DESCRIPTION,
                "The garage, the car is parked here.",
                "De garage, hier staat de auto geparkeerd."
        );
        languages.addWords(Rooms.GARDEN_ROOM_NAME,
                "garden",
                "tuin"
        );
        languages.addWords(Rooms.GARDEN_ROOM_DESCRIPTION,
                "The garden, with a nice fountain and beatifull flowers.",
                "De tuin, er staat een mooie fontijn en de bloemen zijn aan het bloeien."
        );
        languages.addWords(Rooms.KITCHEN_ROOM_NAME,
                "kitchen",
                "keuken"
        );
        languages.addWords(Rooms.KITCHEN_ROOM_DESCRIPTION,
                "The kitchen, with all of its equipment and a mordern stove.",
                "De keuken, met al het keukengerei en een modern kookstel."
        );
        languages.addWords(Rooms.SHED_ROOM_NAME,
                "shed",
                "schuur"
        );
        languages.addWords(Rooms.SHED_ROOM_DESCRIPTION,
                "The shed, a somewhat mysterious location.",
                "De schuur, een geheimzinnige ruimte."
        );
        languages.addWords(Rooms.STORAGE_ROOM_NAME,
                "storage",
                "opslag"
        );
        languages.addWords(Rooms.STORAGE_ROOM_DESCRIPTION,
                "The storage, a lot of boxes and a lot of junk",
                "De opslag, veel dozen en een hoop rommel."
        );
        languages.addWords(Rooms.LIVINGROOM_ROOM_NAME,
                "livingroom",
                "woonkamer"
        );
        languages.addWords(Rooms.LIVINGROOM_ROOM_DESCRIPTION,
                "The livingroom, a tidy and cosy place.",
                "De woonkamer, een gezellige knusse ruimte."
        );

        // Talen
        languages.addWords(Text.LANGUAGE_OPTIONS,
                "Choose one of the folling languages:\n",
                "Kies een van de volgende talen:\n"
        );
        languages.addWords(Text.LANGUAGE_CHANGE_SUCCESS,
                "The languages has been changed.",
                "De taal is verandert."
        );

        dictionary.put("en", LanguageHashMaps.englishWords);
        dictionary.put("nl", LanguageHashMaps.dutchWords);

        // setting the default language
        language = "nl";
    }

    /**
     * Haalt de correcte string op uit de hashmap.
     */
    public static String getString(final String code) {
        return dictionary.get(language).get(code);
    }

    /**
     * Setter voor de language.
     */
    public static boolean setLanguage(final String languageCode) {
        for (String key : dictionary.keySet()) {
            if (key.equals(languageCode)) {
                language = languageCode;
                return true;
            }
        }
        return false;
    }

    public static String getLanguageOptionsString() {
        String languageOptions = getString(Text.LANGUAGE_OPTIONS);
        for (String key : dictionary.keySet()) {
            languageOptions = String.format("%s%s ", languageOptions, key);
        }
        return languageOptions;
    }
}