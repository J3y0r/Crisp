package me.jeyor.j3toolbox;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public final class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue REMOVE_BREAK_DELAY;
    public static final ForgeConfigSpec.BooleanValue REMOVE_JUMP_DELAY;
    public static final ForgeConfigSpec.BooleanValue TACZ_REMOVE_RECOIL;
    public static final ForgeConfigSpec.BooleanValue TACZ_AUTO_RELOAD;
    public static final ForgeConfigSpec.BooleanValue TACZ_SEMI_AS_AUTO;
    public static final ForgeConfigSpec.BooleanValue AUTOAIM_ENABLED;
    public static final ForgeConfigSpec.EnumValue<TriggerMode> AUTOAIM_TRIGGER_MODE;
    public static final ForgeConfigSpec.DoubleValue AUTOAIM_SEARCH_RADIUS;
    public static final ForgeConfigSpec.BooleanValue AUTOAIM_OBSTACLE_CHECK;
    public static final ForgeConfigSpec.EnumValue<ListMode> AUTOAIM_LIST_MODE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AUTOAIM_ENTITY_LIST;
    public static final ForgeConfigSpec.BooleanValue AUTOAIM_SHOW_HIGHLIGHT;
    public static final ForgeConfigSpec.IntValue AUTOAIM_HIGHLIGHT_COLOR;
    public static final ForgeConfigSpec.DoubleValue AUTOAIM_HIGHLIGHT_WIDTH;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("vanilla");
        REMOVE_BREAK_DELAY = builder
                .comment("Remove the 5-tick delay after breaking a block before another break can start")
                .define("removeBreakDelay", true);
        REMOVE_JUMP_DELAY = builder
                .comment("Remove the 10-tick delay after jumping before another jump can start")
                .define("removeJumpDelay", true);
        builder.pop();
        builder.push("tacz");
        TACZ_REMOVE_RECOIL = builder
                .comment("Remove TACZ camera recoil")
                .define("removeRecoil", true);
        TACZ_AUTO_RELOAD = builder
                .comment("Automatically reload TACZ guns when the magazine is empty")
                .define("autoReload", true);
        TACZ_SEMI_AS_AUTO = builder
                .comment("Treat TACZ semi-auto fire mode as full-auto while holding the shoot key")
                .define("semiAsAuto", true);
        builder.pop();
        builder.push("autoaim");
        AUTOAIM_ENABLED = builder
                .comment("Enable auto aim (activated by the Auto Aim key)")
                .define("enabled", true);
        AUTOAIM_TRIGGER_MODE = builder
                .comment("Trigger mode: HOLD or TOGGLE")
                .defineEnum("triggerMode", TriggerMode.HOLD);
        AUTOAIM_SEARCH_RADIUS = builder
                .comment("Search radius in blocks (1-50)")
                .defineInRange("searchRadius", 10.0, 1.0, 50.0);
        AUTOAIM_OBSTACLE_CHECK = builder
                .comment("Check for obstacles between player and target")
                .define("obstacleCheck", true);
        AUTOAIM_LIST_MODE = builder
                .comment("Filter mode: BLACKLIST or WHITELIST")
                .defineEnum("listMode", ListMode.BLACKLIST);
        AUTOAIM_ENTITY_LIST = builder
                .comment("Entity list (registry names)")
                .defineList("entityList",
                        Arrays.asList("minecraft:armor_stand", "minecraft:bat"),
                        obj -> obj instanceof String);
        AUTOAIM_SHOW_HIGHLIGHT = builder
                .comment("Show target highlight effect")
                .define("showHighlight", true);
        AUTOAIM_HIGHLIGHT_COLOR = builder
                .comment("Highlight color (ARGB hex)")
                .defineInRange("highlightColor", 0x80FF0000, Integer.MIN_VALUE, Integer.MAX_VALUE);
        AUTOAIM_HIGHLIGHT_WIDTH = builder
                .comment("Highlight line width (0.1-5.0)")
                .defineInRange("highlightWidth", 2.0, 0.1, 5.0);
        builder.pop();
        SPEC = builder.build();
    }

    public enum TriggerMode {
        HOLD,
        TOGGLE
    }

    public enum ListMode {
        BLACKLIST,
        WHITELIST
    }

    private ClientConfig() {
    }
}
