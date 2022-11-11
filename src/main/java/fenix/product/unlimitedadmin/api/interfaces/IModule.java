package fenix.product.unlimitedadmin.api.interfaces;

import fenix.product.unlimitedadmin.api.exceptions.CommandNotEnoughArgsException;
import fenix.product.unlimitedadmin.api.exceptions.CommandOnlyForUserException;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface IModule {

    default List<ICommand> getCommands() {
        return Collections.emptyList();
    }

    default boolean runCommand(String name, CommandSender sender, List<String> args) throws CommandOnlyForUserException, CommandNotEnoughArgsException {
        for (ICommand command : getCommands()) {
            if (command.getName().equals(name)) {
                return command.onCommand(sender, args);
            }
        }
        sender.sendMessage("This command is bad for module " + getName());
        return false;
    }

    String getName();

}
