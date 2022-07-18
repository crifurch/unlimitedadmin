package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CreateCommand implements ICommand {
    private final WorldManager manager;
    private static final Map<String, World.Environment> supportedEnvironment;
    private boolean isBusy = false;

    static {
        supportedEnvironment = new HashMap<>();
        supportedEnvironment.put("NORMAL", World.Environment.NORMAL);
        supportedEnvironment.put("END", World.Environment.THE_END);
        supportedEnvironment.put("NETHER", World.Environment.NETHER);
    }

    public CreateCommand(WorldManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getName() {
        return "create";
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (isBusy) {
            sender.sendMessage("Can't create world now, another world creating now");
            return true;
        }
        if (argsString.size() < 2) {
            sender.sendMessage("Usage /una " + manager.getName() + " create");
            return false;
        }
        final String envString = argsString.get(1).toUpperCase();
        World.Environment env = supportedEnvironment.get(envString);
        if (env == null) {
            sender.sendMessage("Unsupported world type");
            return false;
        }
        String error;

        isBusy = true;
        try {
            error = manager.createWorld(argsString.get(0), env, WorldType.NORMAL);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, e.toString());
            error = "Error when creating world " + argsString.get(0);
        }
        isBusy = false;
        if (error != null) {
            sender.sendMessage(error);
            return false;
        }else{
            sender.sendMessage("World " + argsString.get(0) + " created");
        }
        return true;
    }
}
