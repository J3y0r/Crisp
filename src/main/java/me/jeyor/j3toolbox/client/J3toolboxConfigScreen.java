package me.jeyor.j3toolbox.client;

import me.jeyor.j3toolbox.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;

public class J3toolboxConfigScreen extends Screen {
    private final Screen parent;

    public J3toolboxConfigScreen(Screen parent) {
        super(Component.translatable("j3toolbox.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int width = 240;
        int x = this.width / 2 - width / 2;
        int y = this.height / 8;
        y = addOption(x, y, width, "j3toolbox.config.removeBreakDelay", ClientConfig.REMOVE_BREAK_DELAY);
        y = addOption(x, y, width, "j3toolbox.config.removeJumpDelay", ClientConfig.REMOVE_JUMP_DELAY);
        if (ModList.get().isLoaded("tacz")) {
            y = addOption(x, y, width, "j3toolbox.config.tacz.removeRecoil", ClientConfig.TACZ_REMOVE_RECOIL);
            y = addOption(x, y, width, "j3toolbox.config.tacz.autoReload", ClientConfig.TACZ_AUTO_RELOAD);
            y = addOption(x, y, width, "j3toolbox.config.tacz.semiAsAuto", ClientConfig.TACZ_SEMI_AS_AUTO);
        }
        this.addRenderableWidget(Button.builder(Component.translatable("j3toolbox.config.autoaim.title"),
                        button -> this.minecraft.setScreen(new AutoAimConfigScreen(this)))
                .bounds(x, y, width, 20)
                .build());
        y += 24;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(x, y + 12, width, 20)
                .build());
    }

    private int addOption(int x, int y, int width, String key, ForgeConfigSpec.BooleanValue value) {
        this.addRenderableWidget(booleanOption(x, y, width, key, value));
        return y + 24;
    }

    private static CycleButton<Boolean> booleanOption(
            int x, int y, int width, String key, ForgeConfigSpec.BooleanValue value
    ) {
        return CycleButton.booleanBuilder(Component.translatable("options.on"), Component.translatable("options.off"))
                .withInitialValue(value.get())
                .create(x, y, width, 20, Component.translatable(key), (button, enabled) -> {
                    value.set(enabled);
                    ClientConfig.SPEC.save();
                });
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
