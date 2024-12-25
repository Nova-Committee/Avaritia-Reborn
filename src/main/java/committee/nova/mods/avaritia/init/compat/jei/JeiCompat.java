package committee.nova.mods.avaritia.init.compat.jei;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.client.screen.CompressorScreen;
import committee.nova.mods.avaritia.client.screen.ExtremeAnvilScreen;
import committee.nova.mods.avaritia.client.screen.ExtremeSmithingScreen;
import committee.nova.mods.avaritia.client.screen.craft.EndCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.ExtremeCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.NetherCraftScreen;
import committee.nova.mods.avaritia.client.screen.craft.SculkCraftScreen;
import committee.nova.mods.avaritia.common.menu.CompressorMenu;
import committee.nova.mods.avaritia.common.menu.ExtremeAnvilMenu;
import committee.nova.mods.avaritia.common.menu.ExtremeSmithingMenu;
import committee.nova.mods.avaritia.common.menu.TierCraftMenu;
import committee.nova.mods.avaritia.init.compat.jei.category.CompressorCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.ExtremeAnvilRecipeCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.ExtremeSmithingRecipeCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.EndCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.ExtremeCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.NetherCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.category.tables.SculkCraftingTableCategory;
import committee.nova.mods.avaritia.init.compat.jei.utils.AnvilRecipeMaker;
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
        registration.addRecipeCategories(new ExtremeAnvilRecipeCategory(helper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var world = Minecraft.getInstance().level;
        var vanillaRecipeFactory = registration.getVanillaRecipeFactory();
        var ingredientManager = registration.getIngredientManager();
        if (world != null) {
            var manager = world.getRecipeManager();
            registration.addRecipes(CompressorCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.COMPRESSOR_RECIPE.get()));

            registration.addRecipes(ExtremeSmithingRecipeCategory.RECIPE_TYPE, manager.getAllRecipesFor(ModRecipeTypes.EXTREME_SMITHING_RECIPE.get()));
            registration.addRecipes(ExtremeAnvilRecipeCategory.RECIPE_TYPE, AnvilRecipeMaker.getAnvilRecipes(vanillaRecipeFactory, ingredientManager));

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
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.extreme_smithing_table.get()), ExtremeSmithingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.extreme_anvil.get()), ExtremeAnvilRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CompressorMenu.class, ModMenus.compressor.get(), CompressorCategory.RECIPE_TYPE, 1, 1, 2, 36);
        registration.addRecipeTransferHandler(TierCraftMenu.class, ModMenus.sculk_crafting_tile_table.get(), SculkCraftingTableCategory.RECIPE_TYPE, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(TierCraftMenu.class, ModMenus.nether_crafting_tile_table.get(), NetherCraftingTableCategory.RECIPE_TYPE, 1, 25, 26, 36);
        registration.addRecipeTransferHandler(TierCraftMenu.class, ModMenus.end_crafting_tile_table.get(), EndCraftingTableCategory.RECIPE_TYPE, 1, 49, 50, 36);
        registration.addRecipeTransferHandler(TierCraftMenu.class, ModMenus.extreme_crafting_table.get(), ExtremeCraftingTableCategory.RECIPE_TYPE, 1, 81, 82, 36);
        registration.addRecipeTransferHandler(ExtremeSmithingMenu.class, ModMenus.extreme_smithing_table.get(), ExtremeSmithingRecipeCategory.RECIPE_TYPE, 1, 5, 6, 36);
        registration.addRecipeTransferHandler(ExtremeAnvilMenu.class, ModMenus.extreme_anvil.get(), ExtremeAnvilRecipeCategory.RECIPE_TYPE, 0, 2, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 86, 25, 31, 8, CompressorCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SculkCraftScreen.class, 61, 24, 22, 12, SculkCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(NetherCraftScreen.class, 100, 44, 22, 12, NetherCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(EndCraftScreen.class, 135, 62, 22, 12, EndCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(ExtremeCraftScreen.class, 174, 90, 22, 12, ExtremeCraftingTableCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(ExtremeSmithingScreen.class, 86, 27, 22, 12, ExtremeSmithingRecipeCategory.RECIPE_TYPE);//todo
        registration.addRecipeClickArea(ExtremeAnvilScreen.class, 102, 48, 22, 15, ExtremeAnvilRecipeCategory.RECIPE_TYPE);
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
