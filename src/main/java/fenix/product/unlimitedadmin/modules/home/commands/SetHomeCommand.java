package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.home.HomeModuleConfig;
import fenix.product.unlimitedadmin.modules.home.data.Home;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetHomeCommand implements ICommand {
    final HomeModule module;

    public SetHomeCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "sethome";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("only player can own this command");
            return true;
        }
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        final List<Home> homes = module.getHomes(((Player) sender).getUniqueId());
        homes.removeIf(home -> home.isOwner(((Player) sender).getUniqueId().toString()));
        int allowedHomes = HomeModuleConfig.HOME_LIMIT.getInt();

        if (allowedHomes < 0) {
            allowedHomes = Integer.MAX_VALUE;
        }
        if (allowedHomes == 0) {
            sender.sendMessage("You can't have home");
            return true;
        }
        String finalName = name;
        if (homes.stream().noneMatch(home -> home.getName().equals(finalName))) {
            if (homes.size() >= allowedHomes) {
                sender.sendMessage("no more homes can be created");
                return true;
            }
        }
        module.setHome((Player) sender, name, ((Player) sender).getLocation());
        return true;
    }
}
