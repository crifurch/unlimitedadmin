package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import fenix.product.unlimitedadmin.modules.world.gui.WorldManagerGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiCommand implements ICommand {

    private final WorldManager manager;

    public GuiCommand(WorldManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getName() {
        return "gui";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if(sender.isOp()||sender.hasPermission("unlimitedadmin.worlds.gui")){
            if(!(sender instanceof HumanEntity)){
                sender.sendMessage("Only player can open gui");
            }else{
                new WorldManagerGui(manager).openInventory((HumanEntity) sender);
            }
        }
        return false;
    }
}
