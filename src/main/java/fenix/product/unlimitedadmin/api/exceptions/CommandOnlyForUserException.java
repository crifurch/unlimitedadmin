package fenix.product.unlimitedadmin.api.exceptions;

import fenix.product.unlimitedadmin.LangConfig;

public class CommandOnlyForUserException extends Exception {
    public CommandOnlyForUserException() {
        super(LangConfig.ONLY_FOR_PLAYER_COMMAND.getText());
    }

    public CommandOnlyForUserException(String message) {
        super(message);
    }
}
