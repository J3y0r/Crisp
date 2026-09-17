package me.jeyor.j3toolbox.client;

import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class AutoAimConfigScreen extends Screen {
    private static final List<Double> RADIUS_VALUES = List.of(5.0, 10.0, 15.0, 20.0, 30.0, 50.0);
    private final Screen parent;

    public AutoAimConfigScreen(Screen parent) {
        super(Component.translatable("j3toolbox.config.autoaim.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int width = 240;
        int x = this.width / 2 - width / 2;
        int y = this.height / 8;
        y = addOption(x, y, width, "j3toolbox.config.autoaim.enabled", ClientConfig.AUTOAIM_ENABLED);
        this.addRenderableWidget(CycleButton.builder((ClientConfig.TriggerMode mode) ->
                        Component.translatable("j3toolbox.config.autoaim.triggerMode." + mode.name()))
                .withValues(ClientConfig.TriggerMode.values())
                .withInitialValue(ClientConfig.AUTOAIM_TRIGGER_MODE.get())
                .create(x, y, width, 20, Component.translatable("j3toolbox.config.autoaim.triggerMode"),
                        (button, value) -> {
                            ClientConfig.AUTOAIM_TRIGGER_MODE.set(value);
                            ClientConfig.SPEC.save();
                        }));
        y += 24;
        double radius = ClientConfig.AUTOAIM_SEARCH_RADIUS.get();
        double initialRadius = RADIUS_VALUES.stream()
                .min((a, b) -> Double.compare(Math.abs(a - radius), Math.abs(b - radius)))
                .orElse(10.0);
        this.addRenderableWidget(CycleButton.builder((Double value) -> Component.literal(String.valueOf(value.intValue())))
                .withValues(RADIUS_VALUES)
                .withInitialValue(initialRadius)
                .create(x, y, width, 20, Component.translatable("j3toolbox.config.autoaim.searchRadius"),
                        (button, value) -> {
                            ClientConfig.AUTOAIM_SEARCH_RADIUS.set(value);
                            ClientConfig.SPEC.save();
                        }));
        y += 24;
        y = addOption(x, y, width, "j3toolbox.config.autoaim.obstacleCheck", ClientConfig.AUTOAIM_OBSTACLE_CHECK);
        this.addRenderableWidget(CycleButton.builder((ClientConfig.ListMode mode) ->
                        Component.translatable("j3toolbox.config.autoaim.listMode." + mode.name()))
                .withValues(ClientConfig.ListMode.values())
                .withInitialValue(ClientConfig.AUTOAIM_LIST_MODE.get())
                .create(x, y, width, 20, Component.translatable("j3toolbox.config.autoaim.listMode"),
                        (button, value) -> {
                            ClientConfig.AUTOAIM_LIST_MODE.set(value);
                            ClientConfig.SPEC.save();
                        }));
        y += 24;
        y = addOption(x, y, width, "j3toolbox.config.autoaim.showHighlight", ClientConfig.AUTOAIM_SHOW_HIGHLIGHT);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(x, y + 12, width, 20)
                .build());
    }

    private int addOption(int x, int y, int width, String key, ForgeConfigSpec.BooleanValue value) {
        this.addRenderableWidget(CycleButton.booleanBuilder(Component.translatable("options.on"), Component.translatable("options.off"))
                .withInitialValue(value.get())
                .create(x, y, width, 20, Component.translatable(key), (button, enabled) -> {
                    value.set(enabled);
                    ClientConfig.SPEC.save();
                }));
        return y + 24;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
