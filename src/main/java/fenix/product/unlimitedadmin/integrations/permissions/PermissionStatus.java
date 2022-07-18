package fenix.product.unlimitedadmin.integrations.permissions;

public enum PermissionStatus {
    PERMISSION_UNSET,
    PERMISSION_TRUE,
    PERMISSION_FALSE;

    boolean isPermittedOrUnset() {
        return this == PERMISSION_TRUE || this == PERMISSION_UNSET;
    }
}
