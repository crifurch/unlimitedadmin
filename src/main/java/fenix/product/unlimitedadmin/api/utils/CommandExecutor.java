package fenix.product.unlimitedadmin.api.utils;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionStatus;
import fenix.product.unlimitedadmin.integrations.permissions.PermissionsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    protected final UnlimitedAdmin plugin;
    protected final ICommand command;
    protected TabCompleter completer;

    public CommandExecutor(UnlimitedAdmin plugin, ICommand command) {
        this.plugin = plugin;
        this.command = command;
        command.setCommandExecutor(plugin, this);
        command.setTabCompleter(plugin, getCompleter());
        command.setPermissionMessage(plugin, "No permissions");
        if (command.isAutoSetPermission()) {
            command.setPermission(plugin, command.getCommandPermission());
        }
    }

    public UnlimitedAdmin getPlugin() {
        return plugin;
    }

    public ICommand getCommand() {
        return command;
    }

    @NotNull
    public TabCompleter getCompleter() {
        if (completer == null) {
            completer = new CustomTabCompleter(this);
        }
        return completer;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!havePermissions(sender, this.command)) {
            return false;
        }
        final List<String> argsString = Arrays.asList(args).subList(0, Math.min(args.length, this.command.getMaxArgsSize()));
        try {
            this.command.onCommand(sender, argsString);
        } catch (NotifibleException e) {
            sender.sendMessage(PlaceHolderUtils.replaceColors(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(PlaceHolderUtils.replaceColors(LangConfig.ERROR_WHILE_COMMAND.getText()));
        }
        return true;
    }


    public PermissionStatus getPermissionStatus(@NotNull CommandSender sender, @NotNull ICommand command, @NotNull String subsituation) {
        if (sender instanceof ConsoleCommandSender || (sender instanceof Player && sender.isOp())) {
            return PermissionStatus.PERMISSION_TRUE;
        }
        assert sender instanceof Player;
        String permission = command.getCommandPermission();
        if (!subsituation.isEmpty()) {
            permission += "." + subsituation;
        }
        return PermissionsProvider.getInstance().havePermission((Player) sender, permission);
    }

    public boolean havePermissions(@NotNull CommandSender sender, @NotNull ICommand command, @NotNull String subsituation) {
        return getPermissionStatus(sender, command, subsituation) == PermissionStatus.PERMISSION_TRUE;
    }

    public boolean havePermissions(@NotNull CommandSender sender, @NotNull ICommand command) {
        if (havePermissions(sender, command, "")) {
            return true;
        }
        final List<String> permissions = command.getPermissions();
        for (String i : permissions) {
            if (havePermissions(sender, command, i)) {
                return true;
            }
        }
        return false;
    }

    protected static class CustomTabCompleter implements TabCompleter {
        protected final CommandExecutor executor;

        protected CustomTabCompleter(CommandExecutor executor) {
            this.executor = executor;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            final List<String> completions = new ArrayList<>();
            if (!executor.havePermissions(sender, executor.getCommand())
                    || args.length > executor.getCommand().getMaxArgsSize()) {
                return completions;
            }
            List<String> tabCompletions = executor.command.getTabCompletion(sender, args, args.length - 1);
            if (tabCompletions == null
                    && executor.command.isNicknamesCompletionsAllowed()
            ) {
                tabCompletions = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                        .sorted().collect(Collectors.toList());
                if (PermissionsProvider.getInstance().havePermissionOrOp(sender,
                        ICommand.baseCommandPermission + ".completion.offline") == PermissionStatus.PERMISSION_TRUE) {
                    final List<String> allPlayers = executor.plugin.getPlayersMapModule().getPlayers().stream()
                            .map(cachedPlayer -> cachedPlayer.name).collect(Collectors.toList());
                    final List<String> finalTabCompletions = tabCompletions;
                    tabCompletions.addAll(allPlayers.stream().filter(s -> !finalTabCompletions.contains(s)).collect(Collectors.toList()));
                }
            }
            if (tabCompletions != null) {
                StringUtil.copyPartialMatches(args[args.length - 1], tabCompletions, completions);
            }
            return completions;
        }
    }
}
