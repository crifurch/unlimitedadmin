package fenix.product.unlimitedadmin.modules.chat.commands.notifications;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeleteNotificationCommand implements ICommand {
    final ChatModule chatModule;

    public DeleteNotificationCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull String getName() {
        return "delnotify";
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <name>";
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public byte getMaxArgsSize() {
        return 1;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        chatModule.deleteNotification(argsString.get(0));
    }
}
