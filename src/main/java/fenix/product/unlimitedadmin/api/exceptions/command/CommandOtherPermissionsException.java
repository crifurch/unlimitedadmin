package fenix.product.unlimitedadmin.api.exceptions.command;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class CommandOtherPermissionsException extends NotifibleException {
    public CommandOtherPermissionsException() {
        super(LangConfig.NO_PERMISSIONS_USE_ON_OTHER.getText());
    }

}
