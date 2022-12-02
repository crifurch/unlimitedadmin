package fenix.product.unlimitedadmin.api.permissions;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;

public enum AdditionalPermissions {
    OTHER("other"),
    SAFE_ON_LOGIN("safe_on_login"),
    SHOW_OFFLINE("show_offline"),
    ;

    private final String permission;

    AdditionalPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermissionForCommand(ICommand command) {
        return command.getCommandPermission() + '.' + this.permission;
    }
}
