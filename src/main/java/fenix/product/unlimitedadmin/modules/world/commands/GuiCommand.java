package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.world.WorldsModule;
import fenix.product.unlimitedadmin.modules.world.gui.WorldManagerGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiCommand implements ICommand {

    private final WorldsModule manager;

    public GuiCommand(WorldsModule manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getName() {
        return "gui";
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws CommandOnlyForUserException {
        if (sender.isOp() || sender.hasPermission("unlimitedadmin.worlds.gui")) {
            if (!(sender instanceof HumanEntity)) {
                throw new CommandOnlyForUserException();
            } else {
                new WorldManagerGui(manager).openInventory((HumanEntity) sender);
            }
        }
    }
}
