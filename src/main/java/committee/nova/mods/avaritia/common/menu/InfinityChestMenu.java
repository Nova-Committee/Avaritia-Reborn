package committee.nova.mods.avaritia.common.menu;

import committee.nova.mods.avaritia.api.common.item.BaseItemStackHandler;
import committee.nova.mods.avaritia.api.common.menu.BaseMenu;
import committee.nova.mods.avaritia.common.inventory.slot.InfinitySlot;
import committee.nova.mods.avaritia.init.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 01:51
 * @Description:
 */
public class InfinityChestMenu extends BaseMenu {
    public static final SimpleContainer CONTAINER = new SimpleContainer(45);
    public final BaseItemStackHandler stackHandler;

    private InfinityChestMenu(MenuType<?> type, int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(type, id, playerInventory, new BaseItemStackHandler(Integer.MAX_VALUE), buffer.readBlockPos());
    }

    protected InfinityChestMenu(MenuType<?> menu, int id, Inventory playerInventory, BaseItemStackHandler stackHandler, @Nullable BlockPos pos) {
        super(menu, id, pos);
        this.stackHandler = stackHandler;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new InfinitySlot(CONTAINER, i * 9 + j, 9 + j * 18, 18 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 9 + k * 18, 112));
        }

        this.scrollTo(0.0F);
    }

    public static InfinityChestMenu create(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
        return new InfinityChestMenu(ModMenus.infinity_chest.get(), windowId, playerInventory, buffer);
    }

    public static InfinityChestMenu create(int windowId, Inventory playerInventory, BaseItemStackHandler stackHandler, BlockPos pos) {
        return new InfinityChestMenu(ModMenus.infinity_chest.get(), windowId, playerInventory, stackHandler, pos);
    }

    protected int calculateRowCount() {
        return Mth.positiveCeilDiv(this.stackHandler.getStacks().size(), 9) - 5;
    }

    public int getRowIndexForScroll(float pScrollOffs) {
        return Math.max((int) ((double) (pScrollOffs * (float) this.calculateRowCount()) + 0.5D), 0);
    }

    public float getScrollForRowIndex(int pRowIndex) {
        return Mth.clamp((float) pRowIndex / (float) this.calculateRowCount(), 0.0F, 1.0F);
    }

    public float subtractInputFromScroll(float pScrollOffs, double pInput) {
        return Mth.clamp(pScrollOffs - (float) (pInput / (double) this.calculateRowCount()), 0.0F, 1.0F);
    }

    public void scrollTo(float pPos) {
        int i = this.getRowIndexForScroll(pPos);

        for (int j = 0; j < 5; ++j) {
            for (int k = 0; k < 9; ++k) {
                int l = k + (j + i) * 9;
                if (l >= 0 && l < this.stackHandler.getStacks().size()) {
                    CONTAINER.setItem(k + j * 9, this.stackHandler.getStacks().get(l));
                } else {
                    CONTAINER.setItem(k + j * 9, ItemStack.EMPTY);
                }
            }
        }

    }

    public boolean canScroll() {
        return this.stackHandler.getStacks().size() > 45;
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        if (pIndex >= this.slots.size() - 9 && pIndex < this.slots.size()) {
            Slot slot = this.slots.get(pIndex);
            if (slot.hasItem()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canDragTo(Slot pSlot) {
        return pSlot.container != CONTAINER;
    }
}
