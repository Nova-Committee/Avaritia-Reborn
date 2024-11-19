package committee.nova.mods.avaritia.client.screen;

import committee.nova.mods.avaritia.api.client.screen.BaseContainerScreen;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:50
 * @Description:
 */
public class InfinityChestScreen extends BaseContainerScreen<InfinityChestMenu> {
    private float scrollOffs;

    public InfinityChestScreen(InfinityChestMenu container, Inventory inventory, Component title, ResourceLocation bgTexture, int bgWidth, int bgHeight) {
        super(container, inventory, title, bgTexture, bgWidth, bgHeight);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (!this.canScroll()) {
            return false;
        } else {
            this.scrollOffs = this.menu.subtractInputFromScroll(this.scrollOffs, pDelta);
            this.menu.scrollTo(this.scrollOffs);
            return true;
        }
    }

    @Override
    public void resize(@NotNull Minecraft pMinecraft, int pWidth, int pHeight) {
        int i = this.menu.getRowIndexForScroll(this.scrollOffs);
        this.init(pMinecraft, pWidth, pHeight);
        this.scrollOffs = this.menu.getScrollForRowIndex(i);
        this.menu.scrollTo(this.scrollOffs);
    }

    private boolean canScroll() {
        return this.menu.canScroll();
    }
}
