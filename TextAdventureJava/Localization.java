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

    }

    private static String language;
    private static HashMap<String, HashMap<String, String>> dictionary = new HashMap<>();

    // static initializer
    // called when class is loaded by the jvm
    static {
        HashMap<String, String> englishWords = new HashMap<>();
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
