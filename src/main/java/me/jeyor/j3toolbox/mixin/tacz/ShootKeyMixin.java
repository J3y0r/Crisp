package me.jeyor.j3toolbox.mixin.tacz;

import com.tacz.guns.api.client.gameplay.IClientPlayerGunOperator;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.client.input.ShootKey;
import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShootKey.class, remap = false)
public abstract class ShootKeyMixin {
    @Redirect(
            method = "autoShoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tacz/guns/api/item/IGun;getFireMode(Lnet/minecraft/world/item/ItemStack;)Lcom/tacz/guns/api/item/gun/FireMode;"
            )
    )
    private static FireMode j3toolbox$semiAsAuto(IGun gun, ItemStack stack) {
        FireMode fireMode = gun.getFireMode(stack);
        if (ClientConfig.TACZ_SEMI_AS_AUTO.get() && fireMode == FireMode.SEMI) {
            return FireMode.AUTO;
        }
        return fireMode;
    }

    @Inject(method = "autoShoot", at = @At("TAIL"))
    private static void j3toolbox$autoReload(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if (!ClientConfig.TACZ_AUTO_RELOAD.get() || event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || player.isSpectator()) {
            return;
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (!(mainHandItem.getItem() instanceof AbstractGunItem gunItem)) {
            return;
        }
        if (gunItem.useInventoryAmmo(mainHandItem)) {
            return;
        }
        IGunOperator operator = IGunOperator.fromLivingEntity(player);
        if (operator.getSynReloadState().getStateType().isReloading()) {
            return;
        }
        if (operator.needCheckAmmo() && !gunItem.canReload(player, mainHandItem)) {
            return;
        }
        if (gunItem.getCurrentAmmoCount(mainHandItem) > 0 || gunItem.hasBulletInBarrel(mainHandItem)) {
            return;
        }
        IClientPlayerGunOperator.fromLocalPlayer(player).reload();
    }
}
