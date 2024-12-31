package committee.nova.mods.avaritia.api.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 12:40
 * Version: 1.0
 */
public class NBTUtils {
    public static String getString(ItemStack stack, String key) {
        return stack.hasTag() ? getTagCompound(stack).getString(key) : "";
    }

    public static CompoundTag getTagCompound(ItemStack stack) {
        validateCompound(stack);
        return stack.getTag();
    }

    public static void validateCompound(ItemStack stack) {
        if (!stack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            stack.setTag(tag);
        }
    }

    public static Tag getTag(ItemStack stack, String key) {
        return stack.hasTag() ? getTagCompound(stack).get(key) : null;
    }
}
