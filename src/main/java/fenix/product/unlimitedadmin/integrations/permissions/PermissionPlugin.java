package fenix.product.unlimitedadmin.integrations.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface PermissionPlugin {

    String getName();

    String getPrefix(Player p);

    String getSuffix(Player p);

    String[] getGroupNames(Player p);

    PermissionStatus havePermission(Player p, String permission);

    PermissionStatus havePermissionOrOp(CommandSender p, String permission);
}