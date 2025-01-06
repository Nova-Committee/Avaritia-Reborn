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
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2025/1/6 13:07
 * @Description:
 */
public abstract class BaseTileMenu<T extends BlockEntity> extends BaseMenu {

    private BlockPos pos;

    protected BaseTileMenu(MenuType<?> menu, int id, Inventory playerInventory, @NotNull BlockPos pos) {
        this(menu, id, playerInventory);
        this.pos = pos;
    }

    protected BaseTileMenu(MenuType<?> menu, int id, Inventory playerInventory) {
        super(menu, id, playerInventory);
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

}
