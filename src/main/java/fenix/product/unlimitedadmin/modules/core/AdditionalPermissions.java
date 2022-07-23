package fenix.product.unlimitedadmin.modules.core;

import fenix.product.unlimitedadmin.api.interfaces.ICommand;

public enum AdditionalPermissions {
    OTHER("other");

    private final String permission;

    AdditionalPermissions(String permission) {
        this.permission = permission;
    }

    public String getPermissionForCommand(ICommand command) {
        return command.getCommandPermission() + '.' + this.permission;
    }
}
