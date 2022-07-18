package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

public class DeleteCommand implements ICommand {
    private final WorldManager manager;
    private boolean isBusy = false;

    public DeleteCommand(WorldManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getName() {
        return "remove";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (isBusy) {
            sender.sendMessage("Can't delete world now, another world deleting now");
            return true;
        }
        if(argsString.size()<1){
            return false;
        }
        String error;

        isBusy = true;
        try {
            error = manager.deleteWorld(argsString.get(0));
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, e.toString());
            error = "Error when creating world " + argsString.get(0);
        }
        isBusy = false;
        if (error != null) {
            sender.sendMessage(error);
            return false;
        }else{
            sender.sendMessage("World " + argsString.get(0) + " deleted");
        }
        return true;
    }
}
