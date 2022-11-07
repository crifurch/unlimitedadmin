package fenix.product.unlimitedadmin.modules.maintain.commands;

import fenix.product.unlimitedadmin.LangConfig;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.maintain.MaintainModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MaintainModeCommand implements ICommand {
    final MaintainModule module;

    public MaintainModeCommand(MaintainModule module) {
        this.module = module;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <value>";
    }

    @Override
    public @NotNull String getName() {
        return "mode";
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return Arrays.asList("1", "0", "true", "false");
    }

    @Override
    public boolean onCommand(CommandSender sender, List<String> argsString) {
        if (argsString.size() < 1) {
            sender.sendMessage(getUsageText());
            return true;
        }
        final String s = argsString.get(0);
        final boolean aTrue = s.equals("1") || s.equals("true");
        module.setMaintainMode(aTrue);
        if (aTrue) {
            sender.sendMessage(LangConfig.SERVER_IN_MAINTAIN_MODE.getText());
        } else {
            sender.sendMessage(LangConfig.SERVER_NOT_IN_MAINTAIN_MODE.getText());
        }
        return true;
    }
}
