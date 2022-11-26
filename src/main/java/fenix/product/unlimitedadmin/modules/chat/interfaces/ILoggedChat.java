package fenix.product.unlimitedadmin.modules.chat.interfaces;

public interface ILoggedChat extends IChatChanel {
    default String getLogPrefix() {
        return "[channel:" + getName() + "]";
    }


}
