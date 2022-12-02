package fenix.product.unlimitedadmin.api.interfaces;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandNotEnoughArgsException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.exceptions.command.CommandOtherPermissionsException;
import fenix.product.unlimitedadmin.api.permissions.AdditionalPermissions;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface ICommand extends ICommandDataProvider {

    default String getUsageText() {
        return "/" + getName();
    }


    void onCommand(CommandSender sender, List<String> argsString)
            throws NotifibleException;

    default void setCommandExecutor(JavaPlugin plugin, CommandExecutor executor) {
        final PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        command.setExecutor(executor);
    }

    default void setTabCompleter(JavaPlugin plugin, TabCompleter completer) {
        final PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        command.setTabCompleter(completer);
    }

    default TabCompleter getTabCompleter(JavaPlugin plugin) {
        final PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        return command.getTabCompleter();
    }

    default void setPermissionMessage(JavaPlugin plugin, String message) {
        final PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        command.setPermissionMessage(message);
    }

    default void setPermission(JavaPlugin plugin, String permission) {
        final PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        command.setPermission(permission);
    }

    default void assertArgsSize(List<String> argsString) throws CommandNotEnoughArgsException {
        if (argsString.size() < getMinArgsSize()) {
            throw new CommandNotEnoughArgsException(getUsageText());
        }
    }

    default void assertSenderIsPlayer(CommandSender sender) throws CommandOnlyForUserException {
        if (!(sender instanceof Player)) {
            throw new CommandOnlyForUserException();
        }
    }

    default void assertOtherPermission(CommandSender sender) throws CommandOtherPermissionsException {
        if (PermissionsProvider.getInstance().havePermission(sender,
                AdditionalPermissions.OTHER.getPermissionForCommand(this)) != PermissionStatus.PERMISSION_TRUE) {
            throw new CommandOtherPermissionsException();
        }
    }
}
