package fenix.product.unlimitedadmin.api.exceptions.module;

import fenix.product.unlimitedadmin.api.LangConfig;
import fenix.product.unlimitedadmin.api.exceptions.NotifibleException;

public class ModuleNotFoundException extends NotifibleException {
    public ModuleNotFoundException() {
        super(LangConfig.NO_SUCH_MODULE.getText());
    }
}
