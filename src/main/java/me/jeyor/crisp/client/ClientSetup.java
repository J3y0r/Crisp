package me.jeyor.crisp.client;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ClientSetup {
    private ClientSetup() {
    }

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> new CrispConfigScreen(parent))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(AutoAimHandler::registerKey);
        MinecraftForge.EVENT_BUS.register(AutoAimHandler.class);
    }
}
