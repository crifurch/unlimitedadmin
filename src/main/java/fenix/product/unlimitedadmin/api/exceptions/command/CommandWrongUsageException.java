package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;
import fenix.product.unlimitedadmin.api.interfaces.ICommand;

public class CommandWrongUsageException extends NotifibleException {
    public CommandWrongUsageException(ICommand command) {
        super(command.getUsageText());
    }
}
