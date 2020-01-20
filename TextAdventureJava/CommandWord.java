/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public enum CommandWord {
    // A value for each command word along with its
    // corresponding user interface string.
    GO(Localization.Commands.GO_COMMAND),
    QUIT(Localization.Commands.QUIT_COMMAND),
    HELP(Localization.Commands.HELP_COMMAND),
    PICKUP(Localization.Commands.PICKUP_COMMAND),
    INVENTORY(Localization.Commands.INVENTORY_COMMAND),
    DROP(Localization.Commands.DROP_COMMAND),
    UNKNOWN(Localization.Commands.UNKNOWN_COMMAND),
    INSPECT(Localization.Commands.INSPECT_COMMAND),
    LOOK(Localization.Commands.LOOK_COMMAND),
    BACK(Localization.Commands.BACK_COMMAND),
    LANGUAGE(Localization.Commands.LANGUAGE_COMMAND),
    ASK(Localization.Commands.ASK_COMMAND),
    ARREST(Localization.Commands.ARREST_COMMAND);

    // The command string.
    private String commandString;

    /**
     * Initialise with the corresponding command string.
     *
     * @param commandString The command string.
     */
    private CommandWord(String commandString) {
        this.commandString = commandString;
    }

    /**
     * @return The command word as a string.
     */
    public String toString() {
        return Localization.getString(commandString);
    }
}
