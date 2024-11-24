package committee.nova.mods.avaritia.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.client.widget.SimpleScrollBar;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:50
 * @Description:
 */
public class InfinityChestScreen extends AbstractContainerScreen<InfinityChestMenu> {
    private InfinityChestWrapper.SlotInfo lastHoveredObject;
    private long lastCount = 0;
    private String lastFormatCountTemp = "";
    private ItemScrollBar scrollBar;

    private static final ResourceLocation GUI_IMG = Static.rl("textures/gui/control_panel.png");

    public InfinityChestScreen(InfinityChestMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 202;
        this.imageHeight = 249;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - imageWidth + 4) / 2;
        this.topPos = (this.height - imageHeight) / 2;
        this.scrollBar = new ItemScrollBar(leftPos + 193, topPos + 6, 4, 152);
        this.scrollBar.setScrolledOn(menu.getScrollOn());
        this.addRenderableWidget(scrollBar);
        menu.scrollOffset(0);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderDummyCount(guiGraphics);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos, 0, 0, imageWidth, 6);

            guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos, 0, 0, imageWidth, 57);
            guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos + 57, 0, 6, imageWidth, 51);
            guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos + 108, 0, 6, imageWidth, 50);
            guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos + 158, 0, 181, imageWidth, 17);
            guiGraphics.blit(GUI_IMG, this.leftPos, this.topPos + 175, 0, 107, imageWidth, 74);

    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderTooltip(GuiGraphics guiGraphics, int pX, int pY) {
        if (this.hoveredSlot != null) {
            if (hoveredSlot.index >= 36) {
                if (menu.getCarried().getCount() == 1 )
                    renderObjectStorageTooltip(guiGraphics, pX, pY);
                else renderCounterTooltip(guiGraphics, pX, pY);
            } else if (!hoveredSlot.getItem().isEmpty() && menu.getCarried().isEmpty()) guiGraphics.renderTooltip(font, this.hoveredSlot.getItem(), pX, pY);
        }
    }

    private void renderCounterTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        if ((hoveredSlot.index - 36) >= menu.viewingItems.size()) return;
        InfinityChestWrapper.SlotInfo hoveredObject = menu.viewingItems.get(hoveredSlot.index - 36);
        List<Component> components = Lists.newArrayList();
        long count;
        components = getTooltipFromItem(this.minecraft, hoveredSlot.getItem());
        count = hoveredObject.count();

        if (!hoveredObject.equals(lastHoveredObject)) {
            String formatCount = Static.DECIMAL_FORMAT.format(count);
            components.add(Component.literal(formatCount));
            this.lastHoveredObject = hoveredObject;
            this.lastCount = count;
            this.lastFormatCountTemp = formatCount;
        } else if (count == lastCount) {
            components.add(Component.literal(lastFormatCountTemp));
        } else {
            String formatCount = Static.DECIMAL_FORMAT.format(count);
            long count2 = count - lastCount;
            String formatCount2 = Static.DECIMAL_FORMAT.format(count2);
            if (count2 >= 0) formatCount += "  |  +§a" + formatCount2;
            else formatCount += "  |  §c" + formatCount2;
            components.add(Component.literal(formatCount));
            lastCount = count;
            lastFormatCountTemp = formatCount;
        }
        guiGraphics.renderTooltip(font, components, hoveredSlot.getItem().getTooltipImage(), pMouseX, pMouseY);
    }

    private void renderObjectStorageTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        ItemStack carried = menu.getCarried();
        boolean hasCapability = carried.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent();
        if (hasCapability) {
            List<Component> components = Lists.newArrayList();
            components.add(Component.translatable("bhs.GUI.capability.tip1", hoveredSlot.getItem().getHoverName()));
            components.add(Component.translatable("bhs.GUI.capability.tip2"));
            components.add(Component.translatable("bhs.GUI.capability.tip3"));
            guiGraphics.renderTooltip(font, components, ItemStack.EMPTY.getTooltipImage(), pMouseX, pMouseY);
        } else renderCounterTooltip(guiGraphics, pMouseX, pMouseY);
    }


    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (scrollBar.isScrolling()) scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseReleased(pMouseX, pMouseY, pButton);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }


    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pMouseX >= leftPos + 5 && pMouseX <= leftPos + 197 && pMouseY >= topPos + 5 && pMouseY <= topPos + 6 + (153) && scrollBar.canScroll()) {
            if (pDelta <= 0) scrollBar.setScrolledOn(menu.onMouseScrolled(false));
            else scrollBar.setScrolledOn(menu.onMouseScrolled(true));
            return true;
        } else return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pMouseX -= i;
        pMouseY -= j;
        return pMouseX >= (double) pX && pMouseX < (double) (pX + pWidth) && pMouseY >= (double) pY && pMouseY < (double) (pY + pHeight);
    }

    public void renderDummyCount(GuiGraphics guiGraphics) {
       var poseStack = guiGraphics.pose();
        for (int i = 0; i < menu.formatCount.size(); i++) {
            Slot slot = menu.slots.get(i + 36);
            String count = menu.formatCount.get(i);
            //this.setBlitOffset(100);
            RenderSystem.enableDepthTest();
            float fontSize = 0.5F;
            poseStack.pushPose();
            poseStack.translate(leftPos + slot.x, topPos + slot.y, 300.0D);
            poseStack.scale(fontSize, fontSize, 1.0F);
            guiGraphics.drawString(font, count,
                    (16 - this.font.width(count) * fontSize) / fontSize,
                    (16 - this.font.lineHeight * fontSize) / fontSize,
                    16777215, false);
            poseStack.popPose();
            //this.setBlitOffset(0);
        }
    }

    private class ItemScrollBar extends SimpleScrollBar {

        private int lastObjectListSize;

        public ItemScrollBar(int x, int y, int weight, int height) {
            super(x, y, weight, height);
            this.setScrollTagSize();
            this.lastObjectListSize = menu.chestWrapper.getSlots();
        }

        public void setScrollTagSize() {
            double v = (double) this.height * ((9.0D) / Math.ceil(menu.chestWrapper.getSlots() / 11.0D));
            this.setScrollTagSize(v);
        }

        @Override
        public void draggedTo(double scrolledOn) {
            menu.onScrollTo(scrolledOn);
        }

        @Override
        public void beforeRender() {
            if (menu.chestWrapper.getSlots() != lastObjectListSize) {
                setScrollTagSize();
                this.lastObjectListSize = menu.chestWrapper.getSlots();
            }
        }
    }

}
