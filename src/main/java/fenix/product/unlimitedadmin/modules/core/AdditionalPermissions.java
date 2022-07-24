package fenix.product.unlimitedadmin.modules.core;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;

public enum AdditionalPermissions {
    OTHER("other"),
    SAFE_ON_LOGIN("safe_on_login");

    private final String permission;

    AdditionalPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermissionForCommand(ICommand command) {
        return command.getCommandPermission() + '.' + this.permission;
    }
}
