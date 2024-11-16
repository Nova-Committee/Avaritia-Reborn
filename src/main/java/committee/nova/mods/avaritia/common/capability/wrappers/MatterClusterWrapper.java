package committee.nova.mods.avaritia.common.capability.wrappers;

import committee.nova.mods.avaritia.api.common.item.BaseItemStackHandler;
import committee.nova.mods.avaritia.common.item.MatterClusterItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/16 20:49
 * @Description:
 */
public class MatterClusterWrapper implements ICapabilitySerializable<CompoundTag> {
    private final BaseItemStackHandler inv;
    private final LazyOptional<ItemStackHandler> inventoryCap;

    public MatterClusterWrapper(ItemStack stack, CompoundTag nbt) {
        this.inv = new BaseItemStackHandler(256);
        this.inv.setSlotValidator((integer, itemStack) -> itemStack.getItem() instanceof MatterClusterItem);
        this.inventoryCap = LazyOptional.of(() -> inv);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCap.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return inv.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        inv.deserializeNBT(nbt);
    }
}
