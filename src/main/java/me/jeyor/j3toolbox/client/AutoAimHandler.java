package me.jeyor.j3toolbox.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class AutoAimHandler {
    public static final KeyMapping AIM_KEY = new KeyMapping(
            "key.j3toolbox.autoaim",
            GLFW.GLFW_KEY_X,
            "key.categories.j3toolbox"
    );

    private static boolean toggleState;
    private static boolean keyWasPressed;
    private static LivingEntity currentTarget;
    private static boolean aiming;

    private AutoAimHandler() {
    }

    public static boolean isAiming() {
        return aiming && currentTarget != null && currentTarget.isAlive();
    }

    public static void registerKey(RegisterKeyMappingsEvent event) {
        event.register(AIM_KEY);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || !ClientConfig.AUTOAIM_ENABLED.get()) {
            clearAim();
            keyWasPressed = AIM_KEY.isDown();
            return;
        }

        boolean keyPressed = AIM_KEY.isDown();
        boolean shouldAim = switch (ClientConfig.AUTOAIM_TRIGGER_MODE.get()) {
            case HOLD -> keyPressed;
            case TOGGLE -> {
                if (keyPressed && !keyWasPressed) {
                    toggleState = !toggleState;
                }
                yield toggleState;
            }
        };
        keyWasPressed = keyPressed;

        if (!shouldAim) {
            clearAim();
            return;
        }

        aiming = true;
        if (canKeepLock(player, currentTarget)) {
            lookAtPosition(player, currentTarget.getEyePosition(1.0f), 1.0f);
            return;
        }
        Optional<LivingEntity> target = findClosestToFov(player);
        target.ifPresentOrElse(entity -> {
            currentTarget = entity;
            lookAtPosition(player, entity.getEyePosition(1.0f), 1.0f);
        }, AutoAimHandler::clearTarget);
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !isAiming()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        lookAtPosition(player, currentTarget.getEyePosition(event.renderTickTime), event.renderTickTime);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!isAiming()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || event.getCamera().getEntity() != player) {
            return;
        }
        event.setYaw(player.getYRot());
        event.setPitch(player.getXRot());
        event.setRoll(0.0f);
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES
                || !isAiming()
                || !ClientConfig.AUTOAIM_SHOW_HIGHLIGHT.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        int color = ClientConfig.AUTOAIM_HIGHLIGHT_COLOR.get();
        float alpha = (float) ((color >> 24) & 0xFF) / 255.0f;
        float red = (float) ((color >> 16) & 0xFF) / 255.0f;
        float green = (float) ((color >> 8) & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;

        float partialTick = event.getPartialTick();
        Vec3 cam = event.getCamera().getPosition();
        Vec3 pos = currentTarget.getPosition(partialTick);
        AABB aabb = currentTarget.getBoundingBox().move(pos.subtract(currentTarget.position()));

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        RenderSystem.lineWidth(ClientConfig.AUTOAIM_HIGHLIGHT_WIDTH.get().floatValue());
        LevelRenderer.renderLineBox(
                poseStack,
                bufferSource.getBuffer(RenderType.lines()),
                aabb,
                red, green, blue, alpha
        );
        poseStack.popPose();
        RenderSystem.lineWidth(1.0f);
    }

    private static void clearAim() {
        aiming = false;
        currentTarget = null;
        if (ClientConfig.AUTOAIM_TRIGGER_MODE.get() == ClientConfig.TriggerMode.HOLD) {
            toggleState = false;
        }
    }

    private static void clearTarget() {
        currentTarget = null;
    }

    private static boolean canKeepLock(LocalPlayer player, LivingEntity entity) {
        return entity != null && !entity.isRemoved() && inRange(player, entity) && isValidTarget(entity, player);
    }

    private static boolean inRange(LocalPlayer player, LivingEntity entity) {
        double radius = ClientConfig.AUTOAIM_SEARCH_RADIUS.get();
        return player.getEyePosition().distanceToSqr(entity.getEyePosition()) <= radius * radius;
    }

    private static Optional<LivingEntity> findClosestToFov(LocalPlayer player) {
        Vec3 center = player.getEyePosition();
        double radius = ClientConfig.AUTOAIM_SEARCH_RADIUS.get();
        AABB area = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );
        Vec3 look = player.getViewVector(1.0f);
        return player.level().getEntitiesOfClass(LivingEntity.class, area, e -> isValidTarget(e, player) && inRange(player, e))
                .stream()
                .min(Comparator.comparingDouble(e -> angleToLook(center, look, e)));
    }

    private static double angleToLook(Vec3 eye, Vec3 look, LivingEntity entity) {
        Vec3 toTarget = entity.getEyePosition().subtract(eye);
        double length = toTarget.length();
        if (length < 1.0E-7D) {
            return 0.0;
        }
        return Math.acos(Mth.clamp(look.dot(toTarget) / length, -1.0, 1.0));
    }

    private static boolean isValidTarget(LivingEntity entity, LocalPlayer player) {
        if (entity == player || !entity.isAlive() || entity.isInvisible() || entity.isSpectator()) {
            return false;
        }
        if (ClientConfig.AUTOAIM_OBSTACLE_CHECK.get()) {
            Vec3 start = player.getEyePosition();
            Vec3 end = entity.getEyePosition();
            BlockHitResult result = player.level().clip(new ClipContext(
                    start, end,
                    ClipContext.Block.VISUAL,
                    ClipContext.Fluid.NONE,
                    player
            ));
            if (result.getType() == HitResult.Type.BLOCK) {
                return false;
            }
        }
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (key == null) {
            return false;
        }
        List<? extends String> entityList = ClientConfig.AUTOAIM_ENTITY_LIST.get();
        return switch (ClientConfig.AUTOAIM_LIST_MODE.get()) {
            case BLACKLIST -> !entityList.contains(key.toString());
            case WHITELIST -> entityList.contains(key.toString());
        };
    }

    private static void lookAtPosition(LocalPlayer player, Vec3 targetPos, float partialTick) {
        Vec3 eyePos = player.getEyePosition(partialTick);
        Vec3 direction = targetPos.subtract(eyePos);
        if (direction.lengthSqr() < 1.0E-7D) {
            return;
        }
        direction = direction.normalize();
        double yaw = Mth.wrapDegrees(Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0);
        double pitch = Mth.wrapDegrees(Math.toDegrees(-Math.asin(direction.y)));
        float yRot = (float) yaw;
        float xRot = (float) Mth.clamp(pitch, -90.0F, 90.0F);
        player.setYRot(yRot);
        player.setXRot(xRot);
        player.yRotO = yRot;
        player.xRotO = xRot;
        player.yHeadRot = yRot;
        player.yHeadRotO = yRot;
        player.yBodyRot = yRot;
        player.yBodyRotO = yRot;
    }
}
