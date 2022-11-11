package fenix.product.unlimitedadmin.modules.maintain.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.CommandNotEnoughArgsException;
import fenix.product.unlimitedadmin.api.exceptions.CommandOnlyForUserException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.maintain.MaintainModule;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        return ICommand.super.getUsageText() + " <value>\n" +
                ICommand.super.getUsageText() + " <world> <value>";
    }

    @Override
    public @NotNull String getName() {
        return "lockworld";
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
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
    public boolean onCommand(CommandSender sender, List<String> argsString) throws CommandNotEnoughArgsException, CommandOnlyForUserException {
        assertArgsSize(argsString);
        World world;
        final String lockValue;
        boolean lock;
        if (argsString.size() == getMinArgsSize()) {
            assertSenderIsPlayer(sender);
            world = ((Player) sender).getWorld();
            lockValue = argsString.get(0);
        } else {
            lockValue = argsString.get(1);
            world = Bukkit.getWorld(argsString.get(0));
        }
        if (world == null) {
            sender.sendMessage(LangConfig.NO_WORLD_FOUND.getText());
            return true;
        }
        lock = lockValue.equalsIgnoreCase("1") || lockValue.equalsIgnoreCase("true");
        module.setLockedWorld(world, lock);
        if (lock) {
            sender.sendMessage(LangConfig.WORLD_IS_LOCKED.getText(world.getName()));
        } else {
            sender.sendMessage(LangConfig.WORLD_IS_UNLOCKED.getText(world.getName()));
        }
        return true;
    }
}
