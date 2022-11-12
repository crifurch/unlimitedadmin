package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class CommandPermissionException extends NotifibleException {
    public CommandPermissionException() {
        super(LangConfig.NO_PERMISSIONS.getText());
    }
}
