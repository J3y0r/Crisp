package me.jeyor.crisp;

import me.jeyor.crisp.client.ClientSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;

@Mod(Crisp.MODID)
public class Crisp {
    public static final String MODID = "crisp";

    public Crisp() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        context.registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> NetworkConstants.IGNORESERVERONLY,
                        (remoteVersion, network) -> true
                )
        );
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.register();
        }
    }
}
