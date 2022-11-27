package fenix.product.unlimitedadmin.api.interfaces;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface IModule {

    default List<ICommand> getCommands() {
        return Collections.emptyList();
    }

    default void runCommand(String name, CommandSender sender, List<String> args) throws NotifibleException {
        for (ICommand command : getCommands()) {
            if (command.getName().equals(name)) {
                command.assertArgsSize(args);
                command.onCommand(sender, args);
            }
        }
        sender.sendMessage("This command is bad for module " + getName());
    }

    String getName();

}
