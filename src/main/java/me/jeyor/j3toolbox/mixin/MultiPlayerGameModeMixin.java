package me.jeyor.j3toolbox.mixin;

import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow
    private int destroyDelay;

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    private void j3toolbox$removeBreakDelay(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (ClientConfig.REMOVE_BREAK_DELAY.get()) {
            this.destroyDelay = 0;
        }
    }
}
