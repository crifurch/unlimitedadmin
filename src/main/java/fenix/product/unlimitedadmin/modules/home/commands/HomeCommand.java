package fenix.product.unlimitedadmin.modules.home.commands;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.GlobalConstants;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandErrorException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandPermissionException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.home.HomeModule;
import fenix.product.unlimitedadmin.modules.home.data.Home;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HomeCommand implements ICommand {
    final HomeModule module;

    public HomeCommand(HomeModule module) {
        this.module = module;
    }

    @Override
    public List<String> getPermissions() {
        return Collections.singletonList(getCommandPermission() + ".other");
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (!(sender instanceof Player)) {
            return ICommand.super.getTabCompletion(sender, args, i);
        }
        final UUID uniqueId = ((Player) sender).getUniqueId();
        return module.getHomes(uniqueId).stream().map(home -> {
            if (home.isOwner(uniqueId.toString())) {
                return home.getName();
            } else {
                return UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerName(UUID.fromString(home.getUUID())) + ":" + home.getName();
            }
        }).collect(Collectors.toList());
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "home";
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        String homeName = GlobalConstants.defaultEntryName;
        UUID homePlayerUuid = null;
        if (sender instanceof Player) {
            homePlayerUuid = ((Player) sender).getUniqueId();
        }
        UUID playerUuid = null;
        if (args.count() > 0) {
            homeName = args.get(0);
        }
        if (args.count() > 1) {
            assertOtherPermission(sender);
            UUID player = UnlimitedAdmin.getInstance().getPlayersMapModule().getPlayerUUID(args.get(1));
            if (player == null) {
                throw new CommandErrorException(LangConfig.NO_SUCH_PLAYER.getText(args.get(1)));
            }
            playerUuid = player;
            if (homePlayerUuid == null) {
                homePlayerUuid = playerUuid;
            }
        }
        if (playerUuid == null) {
            playerUuid = homePlayerUuid;
        }
        if (playerUuid == null) {
            throw new CommandOnlyForUserException();
        }

        Home home = module.getHome(homePlayerUuid, homeName);
        if (home == null) {
            throw new CommandErrorException(LangConfig.NO_SUCH_HOME.getText(homeName));
        }
        if (!module.teleportPlayerToHome(playerUuid, home)) {
            throw new CommandPermissionException();
        }
    }
}
