package committee.nova.mods.avaritia.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.client.screen.BaseContainerScreen;
import committee.nova.mods.avaritia.client.widget.SimpleScrollBar;
import committee.nova.mods.avaritia.common.menu.InfinityChestMenu;
import committee.nova.mods.avaritia.util.StorageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:50
 * @Description:
 */
public class InfinityChestScreen extends AbstractContainerScreen<InfinityChestMenu> {
    private String[] lastHoveredObject = new String[2];
    private long lastCount = 0;
    private String lastFormatCountTemp = "";
    private SortButton sortButton;
    private ItemScrollBar scrollBar;
    private EditBox longSearchBox;

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
        this.scrollBar.setScrolledOn(menu.dummyContainer.getScrollOn());
        this.addRenderableWidget(scrollBar);
        this.sortButton = new SortButton(this.leftPos + 177, this.topPos + 176);
        this.addRenderableWidget(sortButton);

        this.longSearchBox = new EditBox(this.font, leftPos + 41, topPos + 163, 77, 9, Component.translatable("bhs.GUI.search"));
        this.longSearchBox.setMaxLength(64);
        this.longSearchBox.setBordered(false);
        this.longSearchBox.setValue(menu.filter);
        this.addRenderableWidget(longSearchBox);
        menu.dummyContainer.refreshContainer(true);
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
        RenderSystem.setShaderTexture(0, GUI_IMG);
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
            if (hoveredSlot.index >= 51) {
                if (menu.getCarried().getCount() == 1 )
                    renderObjectStorageTooltip(guiGraphics, pX, pY);
                else renderCounterTooltip(guiGraphics, pX, pY);
            } else if (!hoveredSlot.getItem().isEmpty() && menu.getCarried().isEmpty()) guiGraphics.renderTooltip(font, this.hoveredSlot.getItem(), pX, pY);
        } else {
            if (isInsideEditBox(pX, pY)) {
                List<FormattedCharSequence> list = new ArrayList<>();
                list.add(Component.translatable("bhs.GUI.search.tip1").getVisualOrderText());
                list.add(Component.translatable("bhs.GUI.search.tip2").getVisualOrderText());
                list.add(Component.translatable("bhs.GUI.search.tip3").getVisualOrderText());
                guiGraphics.renderTooltip(font, list, pX, pY);
            }
            //else if (sortButton.isHoveredOrFocused()) sortButton.renderToolTip(pPoseStack, pX, pY);
        }
    }


    private boolean isInsideEditBox(double pMouseX, double pMouseY) {
        return pMouseX >= leftPos + 40 && pMouseX <= leftPos + 124 && pMouseY >= topPos + 162 && pMouseY <= topPos + 172;
    }

    private void renderCounterTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        if ((hoveredSlot.index - 51) >= menu.dummyContainer.viewingObject.size()) return;
        String[] hoveredObject = menu.dummyContainer.viewingObject.get(hoveredSlot.index - 51);
        List<Component> components = Lists.newArrayList();
        long count;
        if (hoveredObject[0].equals("item")) {
            components = this.getTooltipFromItem(this.minecraft, hoveredSlot.getItem());
            count = menu.channel.getRealItemAmount(hoveredObject[1]);
        } else {
            count = menu.channel.getItemAmount(hoveredObject[1]);
        }
        if (!Arrays.equals(hoveredObject, lastHoveredObject)) {
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
    public void containerTick() {
        super.containerTick();
        if (longSearchBox.isFocused()) longSearchBox.tick();
    }

    @Override
    public void onClose() {
       // NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new ControlPanelFilterPack(menu.containerId, menu.filter));
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean lshift = InputConstants.isKeyDown(getMinecraft().getWindow().getWindow(), InputConstants.KEY_LSHIFT);
        if (pButton == 1) {
            //长搜索框
            if (longSearchBox.isMouseOver(pMouseX, pMouseY)) {
                menu.filter = "";
                longSearchBox.setValue("");
                menu.dummyContainer.refreshContainer(true);
                longSearchBox.setEditable(true);
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
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
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (longSearchBox.isFocused()) {
            if (pKeyCode >= InputConstants.KEY_0 && pKeyCode <= InputConstants.KEY_Z) return true;
        }
        if (pKeyCode == InputConstants.KEY_LSHIFT) menu.LShifting = true;
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (longSearchBox.isFocused()) {
            String s = longSearchBox.getValue().toLowerCase();
            if (!s.equals(menu.filter)) {
                menu.filter = s;
                menu.dummyContainer.refreshContainer(true);
            }
        }
        if (pKeyCode == InputConstants.KEY_LSHIFT) {
            menu.LShifting = false;
            menu.dummyContainer.refreshContainer(true);
        }
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pMouseX >= leftPos + 5 && pMouseX <= leftPos + 197 && pMouseY >= topPos + 5 && pMouseY <= topPos + 6 + (153) && scrollBar.canScroll()) {
            if (pDelta <= 0) scrollBar.setScrolledOn(menu.dummyContainer.onMouseScrolled(false));
            else scrollBar.setScrolledOn(menu.dummyContainer.onMouseScrolled(true));
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
        for (int i = 0; i < menu.dummyContainer.formatCount.size(); i++) {
            Slot slot = menu.slots.get(i + 51);
            String count = menu.dummyContainer.formatCount.get(i);
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

    private void cycleSort() {
        if (InputConstants.isKeyDown(getMinecraft().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
            menu.reverseSort();
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
        } else {
            menu.nextSort();
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
        }
    }


    private class SortButton extends ImageButton {

        public SortButton(int pX, int pY) {
            super(pX, pY, 19, 16, 202, 0, GUI_IMG, pButton -> cycleSort());
        }

        @Override
        @ParametersAreNonnullByDefault
        public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            int uOffset = this.isHoveredOrFocused() ? 221 : 202;
            int vOffset = menu.sortType * 16;
            guiGraphics.blit(GUI_IMG, this.getX(), this.getY(), (float) uOffset, (float) vOffset, this.width, this.height, 256, 256);
        }
    }

    private class ItemScrollBar extends SimpleScrollBar {

        private int lastObjectListSize;

        public ItemScrollBar(int x, int y, int weight, int height) {
            super(x, y, weight, height);
            this.setScrollTagSize();
            this.lastObjectListSize = menu.dummyContainer.sortedObject.size();
        }

        public void setScrollTagSize() {
            double v = (double) this.height * ((9.0D) / Math.ceil(menu.dummyContainer.sortedObject.size() / 11.0D));
            this.setScrollTagSize(v);
        }

        @Override
        public void draggedTo(double scrolledOn) {
            menu.dummyContainer.onScrollTo(scrolledOn);
        }

        @Override
        public void beforeRender() {
            if (menu.dummyContainer.sortedObject.size() != lastObjectListSize) {
                setScrollTagSize();
                this.lastObjectListSize = menu.dummyContainer.sortedObject.size();
            }
        }
    }

}
