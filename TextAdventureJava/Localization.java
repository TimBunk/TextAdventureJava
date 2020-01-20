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
        String ASK_COMMAND = "commands.ask.number";
        String ARREST_COMMAND = "commands.arrest.name";
    }

    // alle text
    public interface Text {
        String WELCOME_TEXT = "text.welcome";
        String UNKNOWN_COMMAND = "text.unkown_command";
        String UNUSABLE_COMMAND = "text.unusable_command";
        String ROOM_DESCRIPTION = "text.room_long_description.";
        String ROOM_EMPTY = "text.room_empty";
        String ROOM_INSPECTABLE_NON_EXISTENT = "text.room_inspectable_non_existent";
        String ROOM_START_DESCRIPTION = "text.room_start_description";
        String EXIT_STRING = "text.exit_string";
        String PRINT_HELP = "text.print_help";
        String LANGUAGE_CHANGE_SUCCESS = "text.language_change_success";
        String LANGUAGE_OPTIONS = "text.language_options";
        String INSPECT_WHAT = "text.inspect_what";
        String QUIT_WHAT = "text.quit_what";
        String PLACEHOLDER = "text.placeholder";
        String INVENTORY = "text.inventory";
        String DEVELOPED_BY = "text.developed_by";
        String YOU_WON = "text.you_won";
        String YOU_LOST = "text.you_lost";
        String ACTUAL_MURDERER = "text.actual_murderer";
        String REPLAY = "text.replay";
        String ARRESTED = "text.arrested";
        String MURDERER = "text.murderer";
    }

    // alle vragen
    public interface Questions {
        String QUESTION_1 = "question.1";
        String QUESTION_2 = "question.2";
    }

    // alle antwoorden
    public interface Answers {
        String CHEF_1 = "answer.chef.1";
        String CHEF_2 = "answer.chef.2";
        String GARDENER_1 = "answer.gardener.1";
        String GARDENER_2 = "answer.gardener.2";
        String HOUSE_MAID_1 = "answer.house_maid.1";
        String HOUSE_MAID_2 = "answer.house_maid.2";
        String WIFE_1 = "answer.wife.1";
        String WIFE_2 = "answer.wife.2";

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
        String PICKED_UP = "text.picked_up";
        String PICKED_UP_FAILED = "text.picked_up_failed";
        String ITEM_NOT_EXIST = "text.item_not_exist";
        String ITEM_NOT_INVENTORY = "text.item_not_inventory";
        String DROPPED = "text.dropped";
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
        String GO_WHERE = "text.go_where";
        String NO_DOOR = "text.no_door";
    }
    
    // alle personen
    public interface Persons {
        String WIFE_DESCRIPTION = "person.wife.description";
        String GARDENER_DESCRIPTION = "person.gardener.description";
        String CHEF_DESCRIPTION = "person.chef.description";
        String HOUSEMAID_DESCRIPTION = "person.housemaid.description";
        String NO_RESPONSE = "text.no_response";
        String ARREST_EMPTY_ROOM = "text.arrest.room_is_emppty";
        String ARREST_WHO = "text.arrest.who";
        String ARREST_PERSON_NOT_IN_ROOM = "text.arrest.person_not_in_room";
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
        languages.addWords(Commands.ASK_COMMAND,
                "ask",
                "vraag"
        );
        languages.addWords(Commands.ARREST_COMMAND,
                "arrest",
                "arresteer"
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

        // Text
        languages.addWords(Text.UNKNOWN_COMMAND,
                "I don't know what you mean...",
                "Ik weet niet wat je bedoelt..."
        );
        languages.addWords(Text.UNUSABLE_COMMAND,
                "You cannot use this command right now.",
                "Deze command kan je nu niet gebruiken"
        );
        languages.addWords(Text.DEVELOPED_BY,
                "Developed by: Ruben Eekhof && Tim Bunk\nTested by: Arjan Bakema",
                "Gemaakt door: Ruben Eekhof && Tim Bunk\nGetest door: Arjan Bakema"
        );
        languages.addWords(Text.YOU_WON,
                "You won! You arrrested: ",
                "Je hebt gewonnen! Gearresteerd: "
        );
        languages.addWords(Text.YOU_LOST,
                "You lost! You arrrested: ",
                "Je hebt verloren! Gearresteerd: "
        );
        languages.addWords(Text.ACTUAL_MURDERER,
                "The actual murderer is: ",
                "De echt moordenaar is: "
        );
        languages.addWords(Text.REPLAY,
                "To replay the game press ENTER, to exit press the close button of the window",
                "Om het spel opnieuw te spelen klik op ENTER, druk op de sluitknop van het venster om af te sluiten"
        );
        languages.addWords(Text.ARRESTED,
                "Arrested",
                "Gearresteerd"
        );
        languages.addWords(Text.MURDERER,
                "Murderer",
                "Moordenaar"
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
        languages.addWords(Rooms.GO_WHERE,
                "Go where?",
                "Verplaats waar naar toe?"
        );
        languages.addWords(Rooms.NO_DOOR,
                "There is no door.",
                "Er is geen deur.");

        // Text
        languages.addWords(Text.ROOM_INSPECTABLE_NON_EXISTENT,
                "There is no ",
                "Er is geen "
        );
        languages.addWords(Text.INSPECT_WHAT,
                "Inspect what?",
                "Inspecteer wat?"
        );
        languages.addWords(Text.QUIT_WHAT,
                "Quit what?",
                "Sluit wat?"
        );
        languages.addWords(Text.PLACEHOLDER,
                "TYPE SOMETHING...",
                "TIEP IETS..."
        );
        languages.addWords(Text.INVENTORY,
                "Inventory",
                "Inventaris"
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

        // Vragen
        languages.addWords(Questions.QUESTION_1,
                "How was your relationship with the man?",
                "Hoe was jou contact met de man?"
        );
        languages.addWords(Questions.QUESTION_2,
                "How are you doing?",
                "Hoe gaat het met je?"
        );

        // Antwoorden
        languages.addWords(Answers.CHEF_1,
                "I hated to cook for him, the only reason I was still working here was for the money. That stupid donkey did not appreciat my food!",
                "Ik haat het om voor hem te koken de enigste reden dat ik hier nog aan het werk was, was voor het geld. Die stomme ezel weet niet wat lekker eten is!"
        );
        languages.addWords(Answers.CHEF_2,
                "Good, I was planning to find a other job anyways.",
                "Goed, ik was toch van plan nieuw werk te zoeken."
        );
        languages.addWords(Answers.GARDENER_1,
                "We always had a fight because I didn't do my job properly according to him! That bastard!",
                "We hadden altijd ruzie omdat ik mijn werk niet goed deed volgens hem! Die klootzak!"
        );
        languages.addWords(Answers.GARDENER_2,
                "I am rather angry because I have lost my pills. Without my pills I can just get a tantrum!",
                "Ik ben nogal boos omdat ik mijn pillen kwijt ben. Zonder mijn pillen kan ik zomaar een woedeaanval krijgen!"
        );
        languages.addWords(Answers.HOUSE_MAID_1,
                "I was afraid of him because he always yelled at me and because he threatened me in the past to hit me if I didn't do my job well.",
                "Ik was bang voor hem omdat hij altijd tegen mij schreeuwde en omdat hij mij in het verleden heeft bedreigd om mij te gaan slaan als ik mijn werk niet goed deed."
        );
        languages.addWords(Answers.HOUSE_MAID_2,
                "It was a terrible man but he did not deserve this.",
                "Het was een vreselijke man maar dit gun je niemand."
        );
        languages.addWords(Answers.WIFE_1,
                "We had little contact. The only reason I was married to him was for the money",
                "We hadden weinig contact, het ging mij alleen maar om het geld."
        );
        languages.addWords(Answers.WIFE_2,
                "I may not care much for him, but nobody deserves to be killed.",
                "Ik mocht dan wel niet veel om hem geven maar niemand verdient het om vermoord te worden."
        );

        // Personen
        languages.addWords(Persons.CHEF_DESCRIPTION,
                "Brian's private chef.",
                "De prive kok van Brian."
        );
        languages.addWords(Persons.WIFE_DESCRIPTION,
                "The wife of Brian.",
                "De vrouw van Brian."
        );
        languages.addWords(Persons.HOUSEMAID_DESCRIPTION,
                "The house maid.",
                "De schoonmaakster"
        );
        languages.addWords(Persons.GARDENER_DESCRIPTION,
                "The gardener.",
                "De tuinier."
        );
        languages.addWords(Persons.NO_RESPONSE,
                "No one responded.",
                "Niemand geeft antwoord."
        );
        languages.addWords(Persons.ARREST_EMPTY_ROOM,
                "There is no one in this room.",
                "Er is niemand in deze kamer."
        );
        languages.addWords(Persons.ARREST_PERSON_NOT_IN_ROOM,
                "That person is not in this room.",
                "Die persoon is niet in deze kamer."
        );
        languages.addWords(Persons.ARREST_WHO,
                "Arrest who?",
                "Arresteer wie?"
        );

        // Items
        languages.addWords(Items.PICKED_UP,
                "Picked up: ",
                "Opgepakt: "
        );
        languages.addWords(Items.PICKED_UP_FAILED,
                "Could not pick up: ",
                "Kon item niet oppakken: "
        );
        languages.addWords(Items.ITEM_NOT_EXIST,
                "You can not pick that up.",
                "Dat kan je niet oppakken."
        );
        languages.addWords(Items.ITEM_NOT_INVENTORY,
                "That item is not in your inventory.",
                "Die item zit niet in je inventaris"
        );
        languages.addWords(Items.DROPPED,
                "Dropped: ",
                "Laten vallen: "
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