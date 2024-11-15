package committee.nova.mods.avaritia.api.iface;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/15 14:42
 * @Description:
 */
public interface InitEnchantItem {
    int getInitEnchantLevel(ItemStack stack, Enchantment enchantment);
}
