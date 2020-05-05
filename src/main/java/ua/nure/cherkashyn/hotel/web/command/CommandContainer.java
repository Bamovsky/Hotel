package ua.nure.cherkashyn.hotel.web.command;

import org.apache.log4j.Logger;
import ua.nure.cherkashyn.hotel.web.command.common.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author Vladimir Cherkashyn
 */
public class CommandContainer {

    private static final Logger LOG = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<>();

    static {

        LOG.debug("Command container was successfully initialized");

        commands.put("approve", new ApproveCommand());
        commands.put("recovery", new RecoveryCommand());
        commands.put("recoveryHelper", new RecoveryHelperCommand());
        commands.put("showApartments", new ShowApartmentsCommand());
        commands.put("showApartmentsContent", new ShowApartmentsContentCommand());
        commands.put("changeLocale", new ChangeLocaleCommand());
        commands.put("makeOrder", new MakeOrderCommand());
        commands.put("logOut", new LogOutCommand());

    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            LOG.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}