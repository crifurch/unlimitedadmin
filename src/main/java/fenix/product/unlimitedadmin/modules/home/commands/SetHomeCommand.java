package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.GlobalConstants;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandPermissionException;
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
    public void onCommand(CommandSender sender, List<String> argsString) throws CommandOnlyForUserException, CommandPermissionException {
        assertSenderIsPlayer(sender);
        String name = GlobalConstants.defaultEntryName;
        if (argsString.size() > 0) {
            name = argsString.get(0);
        }
        final List<Home> homes = module.getOwnerHomes(((Player) sender).getUniqueId());
        int allowedHomes = HomeModuleConfig.HOME_LIMIT.getInt();

        if (allowedHomes < 0) {
            allowedHomes = Integer.MAX_VALUE;
        }
        if (allowedHomes == 0) {
            throw new CommandPermissionException();
        }
        String finalName = name;
        if (homes.stream().noneMatch(home -> home.getName().equals(finalName))) {
            if (homes.size() >= allowedHomes) {
                throw new CommandPermissionException();
            }
        }
        module.setHome((Player) sender, name, ((Player) sender).getLocation());
        sender.sendMessage(LangConfig.HOME_CREATED.getText(name));
    }
}
