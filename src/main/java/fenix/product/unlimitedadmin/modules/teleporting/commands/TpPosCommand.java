package fenix.product.unlimitedadmin.modules.teleporting.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import fenix.product.unlimitedadmin.modules.core.AdditionalPermissions;
import fenix.product.unlimitedadmin.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TpPosCommand implements ICommand {
    private final UnlimitedAdmin plugin;


    public TpPosCommand(UnlimitedAdmin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <x> <y> <z> [world|current] [player]";
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, int i) {
        if (i < 3) {
            List<String> result = new ArrayList<>();
            for (int it = i; it < 3; it++) {
                StringBuilder s = new StringBuilder();
                for (int k = 0; k < 3 - it; k++) {
                    s.append("~ ");
                }
                result.add(s.toString());
            }
            return result;
        }
        if (i == 3) {
            final List<String> collect = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
            collect.add(0, "current");
            return collect;
        }

        return ICommand.super.getTabCompletion(sender, i);
    }

    @Override
    public byte getMaxArgsSize() {
        return 5;
    }

    @Override
    public @NotNull String getName() {
        return "tppos";
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        UUID targetPlayer = null;
        if (argsString.size() < 3) {
            sender.sendMessage(getUsageText());
            return true;
        }
        if (argsString.size() > 4) {
            targetPlayer = plugin.getPlayersMapModule().getPlayerUUID(argsString.get(4));
            if (targetPlayer == null) {
                sender.sendMessage(LangConfig.NO_SUCH_PLAYER.getText());
                return true;
            } else if (PermissionsProvider.getInstance().havePermissionOrOp(sender,
                    AdditionalPermissions.OTHER.getPermissionForCommand(this)) != PermissionStatus.PERMISSION_TRUE) {
                sender.sendMessage(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
                return true;
            }
        }
        if (targetPlayer == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
                return true;
            }
            targetPlayer = ((Player) sender).getUniqueId();
        }

        final Location location = PlayerUtils.getLocation(targetPlayer);
        try {
            assert location != null;
            location.setX(mapPos(location.getX(), argsString.get(0)));
            location.setY(mapPos(location.getY(), argsString.get(1)));
            location.setZ(mapPos(location.getZ(), argsString.get(2)));
            if (argsString.size() > 3) {
                if (!argsString.get(3).equals("current")) {
                    final ArrayList<World> worlds = new ArrayList<>(Bukkit.getWorlds());
                    worlds.removeIf(world -> !world.getName().equals(argsString.get(3)));
                    if (worlds.isEmpty()) {
                        sender.sendMessage(LangConfig.NO_SUCH_WORLD.getText());
                        return true;
                    }
                    location.setWorld(worlds.get(0));
                }
            }
        } catch (Exception e) {
            sender.sendMessage(LangConfig.ERROR_WHILE_COMMAND.getText());
            sender.sendMessage(getUsageText());
            return true;
        }

        if (!PlayerUtils.setLocation(targetPlayer, location)) {
            sender.sendMessage(LangConfig.ERROR_WHILE_COMMAND.getText());
        }

        return true;
    }

    private double mapPos(double coordinate, String add) {
        if (add.startsWith("~")) {
            if (add.length() <= 1) {
                return coordinate;
            }
            try {
                return coordinate + Double.parseDouble(add.substring(1));
            } catch (Exception e) {
                return coordinate + Integer.parseInt(add.substring(1));
            }
        }
        try {
            return Double.parseDouble(add);
        } catch (Exception e) {
            return Integer.parseInt(add);
        }
    }
}
