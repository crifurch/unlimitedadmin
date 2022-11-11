package fenix.product.unlimitedadmin.api.exceptions;

import fenix.product.unlimitedadmin.LangConfig;

public class CommandNotEnoughArgsException extends Exception {
    public CommandNotEnoughArgsException(String usage) {
        super(usage);
    }
}
