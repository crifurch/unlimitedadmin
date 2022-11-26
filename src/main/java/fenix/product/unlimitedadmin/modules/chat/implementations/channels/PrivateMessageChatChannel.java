package fenix.product.unlimitedadmin.modules.chat.implementations.channels;

import fenix.product.unlimitedadmin.modules.chat.interfaces.ILoggedChat;
import fenix.product.unlimitedadmin.modules.chat.interfaces.ISpiedChat;

public class PrivateMessageChatChannel implements ILoggedChat, ISpiedChat {
    @Override
    public String getName() {
        return "private";
    }


}
