package committee.nova.mods.avaritia.common.capability;

import committee.nova.mods.avaritia.api.common.caps.IFilters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/29 19:51
 * @Description:
 */
@AutoRegisterCapability
public class ItemFilters implements IFilters, INBTSerializable<CompoundTag> {
    private static final String TAG = "avaritia_filters";
    private final List<ItemStack> filters = new ArrayList<>();

    @Override
    public List<ItemStack> getAllFilters() {
        return this.filters;
    }

    @Override
    public void addFilter(ItemStack stack) {
        this.filters.add(stack);
    }

    @Override
    public void removeFilter(ItemStack stack) {
        this.filters.remove(stack);
    }

    @Override
    public void removeAll() {
        this.filters.clear();
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        final ListTag filters = new ListTag();
        this.filters.forEach((itemStack) -> {
            filters.add(itemStack.serializeNBT());
        });
        tag.put(TAG, filters);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        final ListTag filters = nbt.getList(TAG, Tag.TAG_COMPOUND);
        for(int i = 0; i < filters.size(); ++i) {
            CompoundTag account = filters.getCompound(i);
            this.filters.add(ItemStack.of(account));
        }
    }
}
