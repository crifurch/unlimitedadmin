package fenix.product.unlimitedadmin.modules.chat.commands.notifications;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CancelNotificationCommand implements ICommand {
    private final ChatModule chatModule;

    public CancelNotificationCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public String getName() {
        return "cancelad";
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
    public List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        return new ArrayList<>(chatModule.getNotificationNames());
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        assertArgsSize(argsString);
        final boolean b = chatModule.cancelNotification(argsString.get(0));
        if (!b) throw new NotifibleException(LangConfig.NO_SUCH_NOTIFICATION.getText(argsString.get(0)));
    }
}
