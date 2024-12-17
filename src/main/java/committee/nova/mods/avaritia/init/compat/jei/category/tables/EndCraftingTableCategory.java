package committee.nova.mods.avaritia.init.compat.jei.category.tables;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.common.crafting.ISpecialRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.*;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia.util.lang.Localizable;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/16 23:46
 * Version: 1.0
 */
public class EndCraftingTableCategory implements IRecipeCategory<BaseTableCraftingRecipe> {

    public static final RecipeType<BaseTableCraftingRecipe> RECIPE_TYPE = RecipeType.create(Static.MOD_ID, "end_craft", BaseTableCraftingRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Static.MOD_ID, "textures/gui/jei/tables/end_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public EndCraftingTableCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 194, 134);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.end_crafting_table.get()));
    }


    @Override
    public @NotNull RecipeType<BaseTableCraftingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Localizable.of("jei.category.avaritia.end_crafting_table").build();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, BaseTableCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        var level = Minecraft.getInstance().level;
        assert level != null;
        var inputs = recipe.getIngredients();
        var output = recipe.getResultItem(level.registryAccess());
        if (recipe instanceof ShapedTableCraftingRecipe shaped) {
            int stackIndex = 0;
            int heightOffset = Math.floorDiv(7 - shaped.getHeight(), 2);
            int widthOffset = Math.floorDiv(7 - shaped.getWidth(), 2);

            for (int i = heightOffset; i < shaped.getHeight() + heightOffset; i++) {
                for (int j = widthOffset; j < shaped.getWidth() + widthOffset; j++) {
                    builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 5, i * 18 + 5).addIngredients(inputs.get(stackIndex));
                    stackIndex++;
                }
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 169, 58).addItemStack(output);
        } else if (recipe instanceof ShapelessTableCraftingRecipe) {
            shapelessRecipe(builder, inputs);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 169, 58).addItemStack(output);
        } else if (recipe instanceof InfinityCatalystCraftRecipe) {
            shapelessRecipe(builder, inputs);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 169, 58).addItemStack(new ItemStack(ModItems.infinity_catalyst.get()));
        } else if (recipe instanceof EternalSingularityCraftRecipe) {
            shapelessRecipe(builder, inputs);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 169, 58).addItemStack(new ItemStack(ModItems.eternal_singularity.get()));
        }
        builder.moveRecipeTransferButton(170, 100);
    }

    private void shapelessRecipe(@NotNull IRecipeLayoutBuilder builder, NonNullList<Ingredient> inputs) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                int index = j + (i * 7);

                if (index < inputs.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, j * 18 + 2, i * 18 + 2).addIngredients(inputs.get(index));
                }
            }
        }
        builder.setShapeless(170, 140);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(@NotNull BaseTableCraftingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        var shapeless = recipe instanceof ShapelessTableCraftingRecipe;
        int sX = (shapeless ? 340 : 306) / 2, sY = 200 / 2;

        if (shapeless && mouseX > sX + 10 && mouseX < sX + 20 && mouseY > sY - 1 && mouseY < sY + 8) {
            return Collections.singletonList(Localizable.of("jei.tooltip.shapeless.recipe").build());
        }

        return Collections.emptyList();
    }

}
