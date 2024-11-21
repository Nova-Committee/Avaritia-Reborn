package committee.nova.mods.avaritia.api.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/19 19:46
 * Version: 1.0
 */
public abstract class BaseMenu extends AbstractContainerMenu {

    private BlockPos pos;

    protected BaseMenu(MenuType<?> menu, int id, @Nullable BlockPos pos) {
        this(menu, id);
        this.pos = pos;
    }

    protected BaseMenu(MenuType<?> menu, int id) {
        super(menu, id);
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

}
