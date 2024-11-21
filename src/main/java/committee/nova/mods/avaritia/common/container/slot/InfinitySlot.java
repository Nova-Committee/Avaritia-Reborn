package committee.nova.mods.avaritia.common.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/17 02:29
 * @Description:
 */
public class InfinitySlot extends Slot {
    public InfinitySlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack pStack) {
        return Integer.MAX_VALUE;
    }
}
