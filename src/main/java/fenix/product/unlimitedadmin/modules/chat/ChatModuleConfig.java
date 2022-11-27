package fenix.product.unlimitedadmin.modules.chat;

import fenix.product.unlimitedadmin.UnlimitedAdmin;
import fenix.product.unlimitedadmin.api.ModuleConfig;
import fenix.product.unlimitedadmin.api.UnlimitedAdminPermissionsList;
import fenix.product.unlimitedadmin.api.interfaces.IConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public enum ChatModuleConfig implements IConfig {
    IS_GLOBAL_CHAT_WORLD_WIDE("chat.global.world-wide", true, "if true, global chat will be world wide, if false, global chat will be only in world where sender is"),
    IS_GLOBAL_CHAT_ENABLED("chat.global.enabled", true, "if true, global chat will be enabled, if false, global chat will be disabled"),
    GLOBAL_CHAT_FORMAT("chat.global.format", "%prefix%displayname%suffix: &e%message", "format of global chat"),
    GLOBAL_CHAT_PREFIX("chat.global.prefix", "!", "prefix of global chat"),
    IS_LOCAL_CHAT_ENABLED("chat.local.enabled", true, "if true, local chat will be enabled, if false, local chat will be disabled"),
    LOCAL_CHAT_FORMAT("chat.local.format", "%prefix%displayname%suffix: &f%message", "format of local chat"),
    LOCAL_CHAT_PREFIX("chat.local.prefix", "", "prefix of local chat"),
    LOCAL_CHAT_RADIUS("chat.local.radius", 50, "radius of local chat"),

    IS_SPY_CHAT_ENABLED("chat.spy.enabled", true, "if true ops and users with permission(permission: " + UnlimitedAdminPermissionsList.CHAT_SPY + ") can see all local and private messages"),
    SPY_CHAT_FORMAT("chat.spy.format", "&e[SPY] &f%message", "format of spy chat"),
    SPY_CHAT_SHOW_CHANNEL("chat.spy.show-channel", true, "if true, spy chat will show channel of message, if false"),

    IS_PRIVATE_CHAT_ENABLED("chat.private.enabled", true, "if true, private chat will be enabled, if false, private chat will be disabled"),

    PRIVATE_CHAT_FORMAT("chat.private.format", "&f[%prefix%displayname%suffix -> %prefix2%displayname2%suffix2]: &e%message", "format of private chat"),
    IS_LOG_CHAT_ENABLED("log.enabled", true, "if true, log chat will be enabled, if false, log chat will be disabled"),
    SHOW_NOBODY_HEAR_YOU_MESSAGE("show-nobody-hear-you-message", true, "if true, nobody hear you message will be shown, if false, nobody hear you message will be hidden"),
    ADS_ENABLED("ads.enabled", true, "if true, ads will be enabled, if false, ads will be disabled"),

    ;

    private static ModuleConfig config;
    private final Object value;
    private final String path;
    private final String description;
    private final boolean optional;

    ChatModuleConfig(String path, Object val, String description) {
        this.path = path;
        this.value = val;
        this.description = description;
        this.optional = false;
    }

    ChatModuleConfig(String path, Object val, String description, boolean optional) {
        this.path = path;
        this.value = val;
        this.description = description;
        this.optional = optional;
    }

    public static void init(ChatModule module) {
        config = UnlimitedAdmin.getInstance().getModuleConfig(module);
        config.load(Arrays.asList(values()));
    }

    public int getInt() {
        return config.getInt(getPath());
    }

    public boolean getBoolean() {
        return config.getBoolean(getPath());
    }

    public String getString() {
        return config.getString(getPath());
    }

    public ConfigurationSection getSection() {
        return config.getConfigurationSection(getPath());
    }


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getDefaultValue() {
        return value;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }
}