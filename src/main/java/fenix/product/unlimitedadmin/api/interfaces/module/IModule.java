package fenix.product.unlimitedadmin.api.interfaces.module;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.ICommandGroup;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface IModule extends ICommandGroup {
    @Override
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

    @Override
    default boolean shouldOverrideUsageText() {
        return false;
    }
}
