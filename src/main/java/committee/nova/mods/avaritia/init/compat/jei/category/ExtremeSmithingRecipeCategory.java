package committee.nova.mods.avaritia.init.compat.jei.category;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.crafting.recipe.ExtremeSmithingRecipe;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.util.RecipeUtils;
import committee.nova.mods.avaritia.api.utils.lang.Localizable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/21 14:27
 * @Description:
 */
public class ExtremeSmithingRecipeCategory implements IRecipeCategory<ExtremeSmithingRecipe> {
    public static final RecipeType<ExtremeSmithingRecipe> RECIPE_TYPE = RecipeType.create(Static.MOD_ID, "extreme_smithing", ExtremeSmithingRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Static.MOD_ID, "textures/gui/jei/extreme_smithing_jei.png");
    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeSmithingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 170, 64);
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.extreme_smithing_table.get()));
    }

    @Override
    public @NotNull RecipeType<ExtremeSmithingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Localizable.of("jei.category.avaritia.extreme_smithing_table").build();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, @NotNull ExtremeSmithingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 23)
                .addIngredients(recipe.template);

        builder.addSlot(RecipeIngredientRole.INPUT, 45, 23)
                .addIngredients(recipe.base);

        builder.addSlot(RecipeIngredientRole.INPUT, 45, 5)
                .addIngredients(Ingredient.of(Arrays.stream(recipe.additions.getItems()).toList().get(0)));
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 23)
                .addIngredients(Ingredient.of(Arrays.stream(recipe.additions.getItems()).toList().get(1)));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 41)
                .addIngredients(Ingredient.of(Arrays.stream(recipe.additions.getItems()).toList().get(2)));


        builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 23)
                .addItemStack(RecipeUtils.getResultItem(recipe));
        builder.moveRecipeTransferButton(160, 68);
    }

    @Override
    public boolean isHandled(@NotNull ExtremeSmithingRecipe recipe) {
        if (recipe.isIncomplete()) {
            return false;
        }
        return recipe instanceof ExtremeSmithingRecipe;
    }
}
