package me.jeyor.j3toolbox;

import me.jeyor.j3toolbox.client.ClientSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;

@Mod(J3toolbox.MODID)
public class J3toolbox {
    public static final String MODID = "j3toolbox";

    public J3toolbox() {
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
