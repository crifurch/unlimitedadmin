package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class CommandErrorException extends NotifibleException {

    public CommandErrorException() {
        super(LangConfig.ERROR_WHILE_COMMAND.getText());
    }

    public CommandErrorException(String message) {
        super(message);
    }
}
