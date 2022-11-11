package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.exceptions.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.home.HomeModuleConfig;
import fenix.product.unlimitedadmin.modules.home.data.Home;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class DelHomeCommand implements ICommand {
    final HomeModule module;

    public DelHomeCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "delhome";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        try {
            assertSenderIsPlayer(sender);
        } catch (CommandOnlyForUserException e) {
            throw new RuntimeException(e);
        }
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        if (name.contains(":")) {
            final String[] split = name.split(":");
            if (split.length != 2) {
                sender.sendMessage(LangConfig.NO_SUCH_HOME.getText(name));
                return true;
            }
            name = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(split[0]) + ":" + split[1];
        } else {
            name = ((Player) sender).getUniqueId() + ":" + name;
        }
        final List<Home> homes = module.getOwnerHomes(((Player) sender).getUniqueId());
        String finalName = name;
        final Home home = homes.stream().filter(h -> finalName.equals(h.getId() + ":" + h.getName())).findFirst().orElse(null);
        if (home == null) {
            sender.sendMessage(LangConfig.NO_SUCH_HOME.getText(finalName.split(":")[1]));
            return true;
        }
        module.deleteHome(UUID.fromString(home.getId()), home.getName());
        sender.sendMessage(LangConfig.HOME_DELETED.getText(home.getName()));
        return true;
    }
}
