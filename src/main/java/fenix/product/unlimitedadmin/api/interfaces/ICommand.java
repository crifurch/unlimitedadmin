package fenix.product.unlimitedadmin.api.interfaces;

import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface ICommand {
    String baseCommandPermission = "unlimitedadmin.commands.";

    @NotNull
    String getName();

    default String getUsageText() {
        return "/" + getName();
    }

    default List<String> getPermissions() {
        return Collections.emptyList();
    }

    boolean onCommand(CommandSender sender, List<String> argsString);

    default boolean isNicknamesCompletionsAllowed() {
        return true;
    }

    default boolean isAutoSetPermission() {
        return true;
    }

    default byte getMaxArgsSize() {
        return Byte.MAX_VALUE;
    }

    @Nullable
    default List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return null;
    }

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

    default String getCommandPermission() {
        return baseCommandPermission + getName();
    }
}
