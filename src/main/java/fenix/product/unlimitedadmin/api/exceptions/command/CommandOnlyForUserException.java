package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class CommandOnlyForUserException extends NotifibleException {
    public CommandOnlyForUserException() {
        super(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
    }

    public CommandOnlyForUserException(String message) {
        super(message);
    }
}
