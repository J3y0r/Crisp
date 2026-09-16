package me.jeyor.j3toolbox;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue REMOVE_BREAK_DELAY;
    public static final ForgeConfigSpec.BooleanValue REMOVE_JUMP_DELAY;
    public static final ForgeConfigSpec.BooleanValue TACZ_REMOVE_RECOIL;
    public static final ForgeConfigSpec.BooleanValue TACZ_AUTO_RELOAD;
    public static final ForgeConfigSpec.BooleanValue TACZ_REMOVE_SHOOT_DELAY;
    public static final ForgeConfigSpec.BooleanValue TACZ_SEMI_AS_AUTO;

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
        TACZ_REMOVE_SHOOT_DELAY = builder
                .comment("Remove TACZ client shoot interval and bolt wait. Dedicated servers still enforce their own cooldown")
                .define("removeShootDelay", true);
        TACZ_SEMI_AS_AUTO = builder
                .comment("Treat TACZ semi-auto fire mode as full-auto while holding the shoot key")
                .define("semiAsAuto", true);
        builder.pop();
        SPEC = builder.build();
    }

    private ClientConfig() {
    }
}
