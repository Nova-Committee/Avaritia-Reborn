package committee.nova.mods.avaritia.common.container;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper;
import committee.nova.mods.avaritia.common.wrappers.InfinityChestWrapper.*;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/14 22:58
 * @Description:
 */
public class InfinityChestContainer extends SimpleContainer {


    public InfinityChestContainer() {
        super(99);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }
}
