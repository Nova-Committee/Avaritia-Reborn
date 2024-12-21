package committee.nova.mods.avaritia.init.registry;

import committee.nova.mods.avaritia.Static;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @Project: Avaritia-forge
 * @Author: cnlimiter
 * @CreateTime: 2024/1/8 22:43
 * @Description:
 */

public class ModTags {
    public static final TagKey<Item> SINGULARITY = ItemTags.create(Static.rl("singularity"));
    public static final TagKey<Item> DRAWERS = ItemTags.create(new ResourceLocation("storagedrawers", "drawers"));
    public static final TagKey<Block> NEUTRON_UNBREAK = BlockTags.create(Static.rl("neutron_unbreak"));
}
