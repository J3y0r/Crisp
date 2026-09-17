package me.jeyor.crisp.mixin;

import me.jeyor.crisp.ClientConfig;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void crisp$removeJumpDelay(CallbackInfo ci) {
        if (ClientConfig.REMOVE_JUMP_DELAY.get()) {
            ((LivingEntityAccessor) this).crisp$setNoJumpDelay(0);
        }
    }
}
