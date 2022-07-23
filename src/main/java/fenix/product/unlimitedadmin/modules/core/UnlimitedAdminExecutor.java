package fenix.product.unlimitedadmin.modules.core;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.interfaces.IModule;
import fenix.product.unlimitedadmin.api.utils.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnlimitedAdminExecutor extends CommandExecutor {

    public UnlimitedAdminExecutor(UnlimitedAdmin plugin) {
        super(plugin, new BaseAdminCommand() {
            @Override
            public boolean onCommand(CommandSender sender, List<String> argsString) {
                final String module = argsString.get(0).toLowerCase();
                final List<String> args = new ArrayList<>(argsString);
                args.remove(0);
                final String commandName = args.get(0);
                args.remove(0);
                for (IModule i : plugin.getModules()) {
                    if (i.getName().equals(module)) {
                        return i.runCommand(commandName, sender, args);
                    }
                }
                sender.sendMessage("No such module");
                return true;
            }
        });
        command.setPermissionMessage(plugin, "No you are not admin, try later... hah");
    }


    @Override
    public @NotNull TabCompleter getCompleter() {
        if (completer == null) {
            completer = new UnlimitedTabCompleter(this);
        }
        return super.getCompleter();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return false;
        }
        final List<String> argsString = new ArrayList<>(Arrays.asList(args));
        argsString.remove(0);
        while (argsString.size() > this.command.getMaxArgsSize()) {
            argsString.remove(argsString.size() - 1);
        }
        final boolean b = this.command.onCommand(sender, argsString);
        if (!b) {
            sender.sendMessage(this.command.getUsageText());
        }
        return true;
    }


    private static class UnlimitedTabCompleter extends CustomTabCompleter {

        protected UnlimitedTabCompleter(UnlimitedAdminExecutor executor) {
            super(executor);
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            final List<String> completions = new ArrayList<>();
            if (!executor.havePermissions(sender, executor.getCommand())) {
                return completions;
            }
            if (args.length == 1) {
                StringUtil.copyPartialMatches(args[0], executor.getPlugin().getModules().stream().map(this::moduleToName).collect(Collectors.toList()), completions);
            } else if (args.length == 2) {
                for (final IModule module : executor.getPlugin().getModules())
                    StringUtil.copyPartialMatches(args[1], module.getCommands().stream().map(this::commandToName).collect(Collectors.toList()), completions);
            } else {
                ICommand target = null;
                for (final IModule module : executor.getPlugin().getModules()) {
                    for (ICommand command1 : module.getCommands()) {
                        if (command.getName().equals(args[1])) {
                            target = command1;
                            break;
                        }
                    }
                }
                if (target != null) {
                    final List<@NotNull String> strings = Arrays.asList(args);
                    strings.remove(0);
                    strings.remove(0);
                    return executor.getCommand().getTabCompleter(executor.getPlugin()).onTabComplete(sender, command, alias, strings.toArray(new String[0]));
                }
            }
            return completions;
        }

        private String moduleToName(IModule module) {
            return module.getName();
        }

        private String commandToName(ICommand command) {
            return command.getName();
        }
    }
}
