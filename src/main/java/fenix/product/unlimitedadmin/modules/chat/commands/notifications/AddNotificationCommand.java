package fenix.product.unlimitedadmin.modules.chat.commands.notifications;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
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
    public @NotNull String getName() {
        return "addAd";
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
                return Collections.singletonList("<message>");
        }
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        final String name = argsString.get(0);
        final int interval = (int) Double.parseDouble(argsString.get(1));
        final String message = String.join(" ", argsString.subList(2, argsString.size()));
        final boolean b = chatModule.addNotification(name, message, interval);
        if (!b) throw new NotifibleException(LangConfig.NOTIFICATIONS_REMOVE_BEFORE_ADD.getText(name));
    }
}
