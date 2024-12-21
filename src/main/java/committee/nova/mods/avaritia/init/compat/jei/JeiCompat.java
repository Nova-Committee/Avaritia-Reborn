package committee.nova.mods.avaritia.init.compat.jei;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.client.screen.CompressorScreen;
import committee.nova.mods.avaritia.client.screen.craft.EndCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.ExtremeCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.NetherCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.SculkCraftScreen;
import committee.nova.mods.avaritia.common.menu.ModCraftMenu;
import committee.nova.mods.avaritia.init.compat.jei.category.CompressorCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.ExtremeSmithingRecipeCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.EndCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.ExtremeCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.NetherCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.SculkCraftingTableCategory;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia.init.registry.ModMenus;
import committee.nova.mods.avaritia.init.registry.ModRecipeTypes;
import committee.nova.mods.avaritia.util.SingularityUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/15 23:09
 * Version: 1.0
 */
@JeiPlugin
public class JeiCompat implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(Static.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CompressorCategory(helper));
        registration.addRecipeCategories(new SculkCraftingTableCategory(helper));
        registration.addRecipeCategories(new NetherCraftingTableCategory(helper));
        registration.addRecipeCategories(new EndCraftingTableCategory(helper));
        registration.addRecipeCategories(new ExtremeCraftingTableCategory(helper));
        registration.addRecipeCategories(new ExtremeSmithingRecipeCategory(helper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var world = Minecraft.getInstance().level;
        if (world != null) {
            var manager = world.getRecipeManager();
            registration.addRecipes(CompressorCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.COMPRESSOR_RECIPE.get()));


            var recipes = Stream.of(1, 2, 3, 4).collect(Collectors.toMap(tier -> tier, tier ->
                    manager.byType(ModRecipeTypes.CRAFTING_TABLE_RECIPE.get()).values()
                            .stream()
                            .filter(recipe -> recipe.hasRequiredTier() ? tier == recipe.getTier() : tier >= recipe.getTier())
                            .toList()
            ));

            registration.addRecipes(SculkCraftingTableCategory.RECIPE_TYPE, recipes.getOrDefault(1, new ArrayList<>()));
            registration.addRecipes(NetherCraftingTableCategory.RECIPE_TYPE, recipes.getOrDefault(2, new ArrayList<>()));
            registration.addRecipes(EndCraftingTableCategory.RECIPE_TYPE, recipes.getOrDefault(3, new ArrayList<>()));
            registration.addRecipes(ExtremeCraftingTableCategory.RECIPE_TYPE, recipes.getOrDefault(4, new ArrayList<>()));

            registration.addIngredientInfo(new ItemStack(ModBlocks.neutron_collector.get().asItem()), VanillaTypes.ITEM_STACK, Component.translatable("jei.tooltip.avaritia.neutron_collector"));
            registration.addIngredientInfo(new ItemStack(ModItems.neutron_pile.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.tooltip.avaritia.neutron_pile"));

        }

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.neutron_compressor.get()), CompressorCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.sculk_crafting_table.get()), SculkCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.nether_crafting_table.get()), NetherCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.end_crafting_table.get()), EndCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.extreme_crafting_table.get()), ExtremeCraftingTableCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ModCraftMenu.class, ModMenus.sculk_crafting_tile_table.get(), SculkCraftingTableCategory.RECIPE_TYPE, 1, 81, 82, 36);
        registration.addRecipeTransferHandler(ModCraftMenu.class, ModMenus.nether_crafting_tile_table.get(), NetherCraftingTableCategory.RECIPE_TYPE, 1, 81, 82, 36);
        registration.addRecipeTransferHandler(ModCraftMenu.class, ModMenus.end_crafting_tile_table.get(), EndCraftingTableCategory.RECIPE_TYPE, 1, 81, 82, 36);
        registration.addRecipeTransferHandler(ModCraftMenu.class, ModMenus.extreme_crafting_table.get(), ExtremeCraftingTableCategory.RECIPE_TYPE, 1, 81, 82, 36);

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 89, 35, 21, 16, CompressorCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SculkCraftScreen.class, 174, 90, 21, 14, SculkCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(NetherCraftScreen.class, 174, 90, 21, 14, NetherCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(EndCraftScreen.class, 174, 90, 21, 14, EndCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(ExtremeCraftScreen.class, 174, 90, 21, 14, ExtremeCraftingTableCategory.RECIPE_TYPE);
//registration.addGhostIngredientHandler(ExtremeRecipeGeneratorScreen.class, new RecipeLinkJEI<>());
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        ModItems.singularity.ifPresent(item -> {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, (stack, context) -> {
                var singularity = SingularityUtils.getSingularity(stack);
                return singularity != null ? singularity.getId().toString() : "";
            });
        });
    }
}
