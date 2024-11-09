package committee.nova.mods.avaritia.init.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/31 10:33
 * Version: 1.0
 */
public class ModToolTiers{

    public static ForgeTier CRYSTAL_SWORD = new ForgeTier(8888, 8888, 8888f, 50F, 0, Tags.Blocks.NEEDS_WOOD_TOOL, () -> {
        return Ingredient.of(ModItems.crystal_matrix_ingot.get());
    });
    public static ForgeTier CRYSTAL_PICKAXE = new ForgeTier(8888, 8888, 8888f, 8.0F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.crystal_matrix_ingot.get());
    });
    public static ForgeTier BLAZE_PICKAXE = new ForgeTier(7777, 7777, 7777f, 10F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier  BLAZE_SWORD = new ForgeTier(7777, 7777, 7777f, 25F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier  BLAZE_SHOVEL = new ForgeTier(7777, 7777, 7777f, 10F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier  BLAZE_HOE = new ForgeTier(7777, 7777, 7777f, 10F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier BLAZE_AXE = new ForgeTier(7777, 7777, 7777f, 35F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier  INFINITY_PICKAXE = new ForgeTier(9999, 9999, 9999f, 50F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier INFINITY_SWORD = new ForgeTier(9999, 9999, 9999f, 1000F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier INFINITY_SHOVEL = new ForgeTier(9999, 9999, 9999f, 50F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier INFINITY_HOE = new ForgeTier(9999, 9999, 9999f, 50F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });
    public static ForgeTier INFINITY_AXE = new ForgeTier(9999, 9999, 9999f, 100F, 0, Tags.Blocks.NEEDS_WOOD_TOOL,() -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });

}
