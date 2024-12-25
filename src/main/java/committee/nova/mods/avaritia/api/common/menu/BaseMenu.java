package committee.nova.mods.avaritia.api.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/19 19:46
 * Version: 1.0
 */
public abstract class BaseMenu<T extends BlockEntity> extends AbstractContainerMenu {

    private BlockPos pos;
    public final Level level;
    public final Player player;

    protected BaseMenu(MenuType<?> menu, int id, Inventory playerInventory, @NotNull BlockPos pos) {
        this(menu, id, playerInventory);
        this.pos = pos;
    }

    protected BaseMenu(MenuType<?> menu, int id, Inventory playerInventory) {
        super(menu, id);
        this.player = playerInventory.player;
        this.level = playerInventory.player.level();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return pos == null || player.distanceToSqr(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }


    @SuppressWarnings("unchecked")
    protected T getTileEntity() {
        return (T) level.getBlockEntity(pos);
    }

    protected void createInventorySlots(Inventory pInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pInventory, k, 8 + k * 18, 142));
        }
    }
}
