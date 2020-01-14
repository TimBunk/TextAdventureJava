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
    }

    // alle text
    public interface Text {
        String WELCOME_TEXT = "text.welcome";
        String ROOM_DESCRIPTION = "text.room_long_description.";
        String ROOM_EMPTY = "text.room_empty";
        String ROOM_START_DESCRIPTION = "text.room_start_description";
        String EXIT_STRING = "text.exit_string";
        String PRINT_HELP = "text.print_help"; 
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

    private static String language;
    // de hashmap waarin we onze data opslaan
    private static HashMap<String, HashMap<String, String>> dictionary = new HashMap<>();

    /**
     * De static intialiser.
     * Deze word aangeroepen wanneer de jvm de klasse compileert.
     */
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
        englishWords.put(Commands.USE_COMMAND, "use");

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
        dutchWords.put(Commands.USE_COMMAND, "gebruik");  
        
        dutchWords.put(Text.WELCOME_TEXT, "Er is iemand vermoord." +
                "\nBruce Cain de beste detective te vinden op deze planeet, moet de moordenaar zien te vinden.\n" +
                "Er zijn 4 verdachten, jij moet als Bruce onderzoek doen naar wie de moordenaar is." +
                "Veel geluk, en type help voor een lijst voor alle commando's.");
        dutchWords.put(Text.ROOM_DESCRIPTION, "Je bent in de ");
        dutchWords.put(Text.ROOM_START_DESCRIPTION, "Er zijn wat dingen in deze kamer: ");
        dutchWords.put(Text.ROOM_EMPTY, "Deze kamer is leeg. \n");
        dutchWords.put(Text.EXIT_STRING, "De uitgangen zijn: ");
        dutchWords.put(Text.PRINT_HELP, "Je probeert de moordenaar van de rijke man te zoeken. Je hebt de volgende commandos:\n");

        englishWords.put(Text.WELCOME_TEXT, "Someone has been murdered." +
                "\nBruce Cain the best detective on this planet has to find the murderer.\n" +
                "There are 4 suspects, you have to play as Bruce and find the murderer." +
                "Good luck, and type help for a list of all commands.");
        englishWords.put(Text.ROOM_DESCRIPTION, "You are in the ");
        englishWords.put(Text.ROOM_START_DESCRIPTION, "There are some things in this room: ");
        englishWords.put(Text.ROOM_EMPTY, "This room is empty. \n");
        englishWords.put(Text.EXIT_STRING, "The exits are: ");
        englishWords.put(Text.PRINT_HELP, "Youre trying to find the rich guys murderer. You've got acces to the following commands:\n");  

        dutchWords.put(Rooms.BEDROOM_ROOM_NAME, "Slaapkamer");
        dutchWords.put(Rooms.BEDROOM_ROOM_DESCRIPTION, "De slaapkamer van de eigenaar.");
        dutchWords.put(Rooms.GARAGE_ROOM_NAME, "Garage");
        dutchWords.put(Rooms.GARAGE_ROOM_DESCRIPTION, "De garage, hier staat de auto geparkeerd.");
        dutchWords.put(Rooms.GARDEN_ROOM_NAME, "Tuin");
        dutchWords.put(Rooms.GARDEN_ROOM_DESCRIPTION, "De tuin, er staat een mooie fontijn en de bloemen zijn aan het bloeien.");
        dutchWords.put(Rooms.KITCHEN_ROOM_NAME, "Keuken");
        dutchWords.put(Rooms.KITCHEN_ROOM_DESCRIPTION, "De keuken, met al het keukengerei en een modern kookstel.");
        dutchWords.put(Rooms.SHED_ROOM_NAME, "Schuur");
        dutchWords.put(Rooms.SHED_ROOM_DESCRIPTION, "De schuur, een geheimzinnige ruimte.");
        dutchWords.put(Rooms.STORAGE_ROOM_NAME, "Opslag");
        dutchWords.put(Rooms.STORAGE_ROOM_DESCRIPTION, "De opslag, veel dozen en een hoop rommel.");
        dutchWords.put(Rooms.LIVINGROOM_ROOM_NAME, "Woonkamer");
        dutchWords.put(Rooms.LIVINGROOM_ROOM_DESCRIPTION, "De woonkamer, een gezellige knusse ruimte.");

        englishWords.put(Rooms.BEDROOM_ROOM_NAME, "Bedroom");
        englishWords.put(Rooms.BEDROOM_ROOM_DESCRIPTION, "The owners bedroom.");
        englishWords.put(Rooms.GARAGE_ROOM_NAME, "Garage");
        englishWords.put(Rooms.GARAGE_ROOM_DESCRIPTION, "The garage, the car is parked here.");
        englishWords.put(Rooms.GARDEN_ROOM_NAME, "Garden");
        englishWords.put(Rooms.GARDEN_ROOM_DESCRIPTION, "The garden, with a nice fountain and beatifull flowers.");
        englishWords.put(Rooms.KITCHEN_ROOM_NAME, "Kitchen");
        englishWords.put(Rooms.KITCHEN_ROOM_DESCRIPTION, "The kitchen, with all of its equipment and a mordern stove.");
        englishWords.put(Rooms.SHED_ROOM_NAME, "Shed");
        englishWords.put(Rooms.SHED_ROOM_DESCRIPTION, "The shed, a somewhat mysterious location.");
        englishWords.put(Rooms.STORAGE_ROOM_NAME, "Storage");
        englishWords.put(Rooms.STORAGE_ROOM_DESCRIPTION, "The storage, a lot of boxes and a lot of junk");
        dutchWords.put(Rooms.LIVINGROOM_ROOM_NAME, "Livingroom");
        dutchWords.put(Rooms.LIVINGROOM_ROOM_DESCRIPTION, "The livingroom, a tidy and cosy place.");

        dictionary.put("en", englishWords);
        dictionary.put("nl", dutchWords);

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
    public static void setLanguage(final String languageCode) {
        language = languageCode;
    }
}