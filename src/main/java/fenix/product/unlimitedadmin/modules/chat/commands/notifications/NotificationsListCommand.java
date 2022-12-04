package fenix.product.unlimitedadmin.modules.chat.commands.notifications;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.CommandArguments;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class NotificationsListCommand implements ICommand {
    final ChatModule chatModule;

    public NotificationsListCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull String getName() {
        return "listnotifies";
    }

    @Override
    public byte getMaxArgsSize() {
        return 0;
    }

    @Override
    public void onCommand(CommandSender sender, CommandArguments args) throws NotifibleException {
        sender.sendMessage(String.join(", ", chatModule.getNotificationNames()));
    }
}
