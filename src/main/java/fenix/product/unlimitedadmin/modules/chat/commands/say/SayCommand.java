package fenix.product.unlimitedadmin.modules.chat.commands.say;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.NotificationsChatChannel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SayCommand implements ICommand {
    final ChatModule chatModule;

    public SayCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <message>";
    }

    @Override
    public @NotNull String getName() {
        return "say";
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        String message = String.join(" ", argsString);
        Entity entity = null;
        String prefix = ChatModuleConfig.SAY_FORMAT.getString();
        if (sender instanceof Entity) {
            entity = (Entity) sender;
            prefix = PlaceHolderUtils.replacePlayerPlaceholder(prefix, entity);
        } else {
            prefix = PlaceHolderUtils.replaceServerPlaceholder(prefix, sender);
        }
        message = prefix.replaceAll("%message", message);
        chatModule.broadcastMessage(entity, NotificationsChatChannel.CHANNEL_PREFIX + message);
    }
}
