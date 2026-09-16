package me.jeyor.j3toolbox.mixin.tacz;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.gameplay.LocalPlayerShoot;
import com.tacz.guns.resource.pojo.data.gun.Bolt;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LocalPlayerShoot.class, remap = false)
public abstract class LocalPlayerShootMixin {
    @Inject(method = "getCoolDown", at = @At("HEAD"), cancellable = true)
    private void j3toolbox$removeShootDelay(IGun iGun, ItemStack mainHandItem, GunData gunData, CallbackInfoReturnable<Long> cir) {
        if (ClientConfig.TACZ_REMOVE_SHOOT_DELAY.get()) {
            cir.setReturnValue(0L);
        }
    }

    @Redirect(
            method = "preCheck",
            at = @At(value = "INVOKE", target = "Lcom/tacz/guns/api/entity/IGunOperator;getSynIsBolting()Z")
    )
    private boolean j3toolbox$skipBolting(IGunOperator operator) {
        if (ClientConfig.TACZ_REMOVE_SHOOT_DELAY.get()) {
            return false;
        }
        return operator.getSynIsBolting();
    }

    @Redirect(
            method = "preCheck",
            at = @At(value = "INVOKE", target = "Lcom/tacz/guns/resource/pojo/data/gun/GunData;getBolt()Lcom/tacz/guns/resource/pojo/data/gun/Bolt;")
    )
    private Bolt j3toolbox$skipManualBolt(GunData gunData) {
        Bolt bolt = gunData.getBolt();
        if (ClientConfig.TACZ_REMOVE_SHOOT_DELAY.get() && bolt == Bolt.MANUAL_ACTION) {
            return Bolt.CLOSED_BOLT;
        }
        return bolt;
    }
}
