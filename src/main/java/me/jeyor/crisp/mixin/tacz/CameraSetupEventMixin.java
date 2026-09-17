package me.jeyor.crisp.mixin.tacz;

import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.client.event.CameraSetupEvent;
import me.jeyor.crisp.ClientConfig;
import me.jeyor.crisp.client.AutoAimHandler;
import net.minecraftforge.client.event.ViewportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CameraSetupEvent.class, remap = false)
public abstract class CameraSetupEventMixin {
    @Inject(method = "initialCameraRecoil", at = @At("HEAD"), cancellable = true)
    private static void crisp$cancelRecoilInit(GunFireEvent event, CallbackInfo ci) {
        if (ClientConfig.TACZ_REMOVE_RECOIL.get() || AutoAimHandler.isAiming()) {
            ci.cancel();
        }
    }

    @Inject(method = "applyCameraRecoil", at = @At("HEAD"), cancellable = true)
    private static void crisp$cancelRecoilApply(ViewportEvent.ComputeCameraAngles event, CallbackInfo ci) {
        if (ClientConfig.TACZ_REMOVE_RECOIL.get() || AutoAimHandler.isAiming()) {
            ci.cancel();
        }
    }
}
