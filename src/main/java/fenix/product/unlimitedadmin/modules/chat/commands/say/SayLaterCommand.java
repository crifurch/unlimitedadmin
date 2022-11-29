package fenix.product.unlimitedadmin.modules.chat.commands.say;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.api.utils.PlaceHolderUtils;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.ChatModuleConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SayLaterCommand implements ICommand {
    final ChatModule chatModule;

    public SayLaterCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public String getUsageText() {
        return ICommand.super.getUsageText() + " <seconds> <message>";
    }

    @Override
    public @NotNull String getName() {
        return "saylater";
    }

    @Override
    public byte getMinArgsSize() {
        return 2;
    }

    @Override
    public @Nullable List<String> getTabCompletion(CommandSender sender, String[] args, int i) {
        if (i == 0) {
            return Collections.singletonList("<seconsd>");
        }
        return ICommand.EMPTY_TAB_COMPLETIONS;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        int seconds = (int) Double.parseDouble(argsString.get(0));
        if (seconds > ChatModuleConfig.SAY_MAX_DELAY.getInt()) {
            throw new NotifibleException(LangConfig.MAX_SAY_DELAY_ERROR.getText(ChatModuleConfig.SAY_MAX_DELAY.getInt() + ""));
        }
        String message = String.join(" ", argsString.subList(1, argsString.size()));
        String prefix = ChatModuleConfig.SAY_FORMAT.getString();
        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;
            prefix = PlaceHolderUtils.replacePlayerPlaceholder(prefix, entity);
        } else {
            prefix = PlaceHolderUtils.replaceServerPlaceholder(prefix, sender);
        }
        message = prefix.replaceAll("%message", message);
        final Collection<String> notificationNames = chatModule.getNotificationNames();
        String name = "saylater";
        int i = 0;
        while (notificationNames.contains(name)) {
            name = "saylater" + i;
            i++;
        }
        if (!chatModule.addDelayedNotification(name, message, seconds)) {
            throw new RuntimeException("Could not add notification");
        }
    }

}
