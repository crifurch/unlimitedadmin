package fenix.product.unlimitedadmin.modules.chat.commands.notifications;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AddNotificationCommand implements ICommand {
    final ChatModule chatModule;

    public AddNotificationCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <name> <seconds> ";
    }

    @Override
    public @NotNull String getName() {
        return "addnotify";
    }

    @Override
    public byte getMinArgsSize() {
        return 3;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        switch (i) {
            case 0:
                return Collections.singletonList("<name>");
            case 1:
                return Collections.singletonList("<seconde>=1>");
            default:
                return ICommand.EMPTY_TAB_COMPLETIONS;
        }
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        final String name = args.get(0);
        final int interval = (int) Double.parseDouble(args.get(1));
        final String message = args.getMessage(2);
        final boolean b = chatModule.addCyclicNotification(name, message, interval);
        if (!b) throw new NotifibleException(LangConfig.NOTIFICATIONS_REMOVE_BEFORE_ADD.getText(name));
    }
}
