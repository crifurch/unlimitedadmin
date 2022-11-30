package fenix.product.unlimitedadmin.modules.chat.commands.privatemessages;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;
import fenix.product.unlimitedadmin.modules.chat.ChatModule;
import fenix.product.unlimitedadmin.modules.chat.data.sender.ChatMessageSender;
import fenix.product.unlimitedadmin.modules.chat.data.sender.PlayerMessageSender;
import fenix.product.unlimitedadmin.modules.chat.implementations.channels.PrivateMessageChatChannel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class AnswerCommand implements ICommand {
    private final ChatModule chatModule;

    public AnswerCommand(ChatModule chatModule) {
        this.chatModule = chatModule;
    }

    @Override
    public @NotNull String getName() {
        return "answer";
    }

    @Override
    public @Nullable List<String> getAliases() {
        return Arrays.asList("a", "r", "reply");
    }

    @Override
    public byte getMinArgsSize() {
        return 1;
    }

    @Override
    public void onCommand(CommandSender sender, List<String> argsString) throws NotifibleException {
        assertSenderIsPlayer(sender);
        Player player = (Player) sender;
        if (argsString.size() > 0) {
            String message = String.join(" ", argsString);
            final PlayerMessageSender forAnswer = chatModule.getForAnswer(player);
            if (forAnswer == null) {
                throw new NotifibleException(LangConfig.CHAT_NO_ONE_TO_ANSWER.getText());
            }
            chatModule.broadcastMessage(ChatMessageSender.fromSender(sender), PrivateMessageChatChannel.prepareMessage(message, forAnswer.getName()));
        }
    }


}