package committee.nova.mods.avaritia.api.common.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/22 01:42
 * @Description:
 */
public class FakeSlot extends Slot {
    public FakeSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    @Override
    public void set(ItemStack pStack) {
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
    }

    @Override
    public ItemStack remove(int pAmount) {
        return ItemStack.EMPTY;
    }

    @Override
    public Optional<ItemStack> tryRemove(int pCount, int pDecrement, Player pPlayer) {
        return Optional.of(ItemStack.EMPTY);
    }

    @Override
    public ItemStack safeInsert(ItemStack pStack, int pIncrement) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack safeTake(int pCount, int pDecrement, Player pPlayer) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onQuickCraft(ItemStack pOldStack, ItemStack pNewStack) {
    }

    @Override
    public void setChanged() {
    }
}