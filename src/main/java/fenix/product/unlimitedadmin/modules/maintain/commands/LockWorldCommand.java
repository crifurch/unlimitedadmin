package fenix.product.unlimitedadmin.modules.maintain.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.maintain.MaintainModule;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LockWorldCommand implements ICommand {
    final MaintainModule module;

    public LockWorldCommand(MaintainModule module) {
        this.module = module;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <value>";
    }

    @Override
    public @NotNull String getName() {
        return "lockworld";
    }

    @Override
    public byte getMaxArgsSize() {
        return 2;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (i == 0) {
            return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        }
        return Arrays.asList("1", "0", "true", "false");
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (argsString.size() < 1) {
            sender.sendMessage(getUsageText());
            return true;
        }

        String worldName = argsString.get(0);
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(LangConfig.NO_WORLD_FOUND.getText());
            return true;
        }
        if (argsString.size() > 1) {
            final String lock = argsString.get(1);
            final boolean aTrue = lock.equals("1") || lock.equals("true");
            module.setLockedWorld(world, aTrue);
            if (aTrue) {
                sender.sendMessage(LangConfig.WORLD_IS_LOCKED.getText(worldName));
            } else {
                sender.sendMessage(LangConfig.WORLD_IS_UNLOCKED.getText(worldName));
            }
            return true;
        }
        return true;
    }
}
