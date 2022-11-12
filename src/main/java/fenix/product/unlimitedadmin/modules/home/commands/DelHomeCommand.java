package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
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
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        assertSenderIsPlayer(sender);
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        name = module.parseHomeName((Player) sender, name);
        if (name == null) {
            throw new CommandErrorException(LangConfig.NO_SUCH_HOME.getText());
        }
        final List<Home> homes = module.getOwnerHomes(((Player) sender).getUniqueId());
        String finalName = name.replace(':', '@');
        final Home home = homes.stream().filter(h ->
                finalName.equals(h.getId())
        ).findFirst().orElse(null);
        if (home == null) {
            throw new CommandErrorException(LangConfig.NO_SUCH_HOME.getText(name.split(":")[1]));
        }

        final boolean b = module.deleteHome(UUID.fromString(home.getUUID()), home.getName());
        if (!b) {
            throw new CommandErrorException();
        }
        sender.sendMessage(LangConfig.HOME_DELETED.getText(home.getName()));
    }
}
