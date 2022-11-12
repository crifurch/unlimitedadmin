package fenix.product.unlimitedadmin.modules.world.commands;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CollectionUtils;
import fenix.product.unlimitedadmin.modules.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements ICommand {
    private final WorldManager manager;

    public ListCommand(WorldManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getName() {
        return "list";
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (i == 0) {
            return Arrays.asList("custom", "standard", "all");
        }
        return ICommand.super.getTabCompletion(sender, args, i);
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) {
        int wide = 1;
        if (argsString.size() > 0) {
            if ("custom".equalsIgnoreCase(argsString.get(0))) {
                wide = -1;
            } else if ("standard".equalsIgnoreCase(argsString.get(0))) {
                wide = 0;
            }
        }
        List<String> worlds = new ArrayList<>();
        if (wide >= 0) {
            worlds.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
        }
        if (wide != 0) {
            worlds.addAll(manager.getWorlds());
        }
        worlds = CollectionUtils.removeDuplicates(worlds);
        Collections.sort(worlds);

        StringBuilder result = new StringBuilder();
        worlds.forEach(e -> {
            result.append(e);
            result.append("\n");
        });
        sender.sendMessage(result.toString());
    }


}
