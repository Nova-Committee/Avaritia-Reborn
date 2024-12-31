package committee.nova.mods.avaritia.common.capability;

import committee.nova.mods.avaritia.init.registry.ModCaps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/29 19:51
 * @Description:
 */
public class ItemFiltersProvider implements ICapabilitySerializable<CompoundTag> {
    private ItemFilters itemFilters;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCaps.ITEM_FILTERS ? LazyOptional.of(this::getOrCreateCapability).cast() : LazyOptional.empty();
    }

    public ItemFilters getOrCreateCapability() {
        if (itemFilters == null) this.itemFilters = new ItemFilters();
        return this.itemFilters;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        getOrCreateCapability().deserializeNBT(tag);
    }
}
