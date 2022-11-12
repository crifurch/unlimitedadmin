package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class CommandNotEnoughArgsException extends NotifibleException {
    public CommandNotEnoughArgsException(String usage) {
        super(usage);
    }
}
