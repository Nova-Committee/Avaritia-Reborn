package committee.nova.mods.avaritia.api.common.wrapper;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/21 22:49
 * @Description:
 */
public interface ICapItemHandler extends IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
}
