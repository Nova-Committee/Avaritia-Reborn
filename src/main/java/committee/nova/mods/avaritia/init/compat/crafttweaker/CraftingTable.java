package committee.nova.mods.avaritia.init.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.common.crafting.ISpecialRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.InfinityCatalystCraftRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapedTableCraftingRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.ShapelessTableCraftingRecipe;
import committee.nova.mods.avaritia.init.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/17 8:39
 * Version: 1.0
 */
@ZenCodeType.Name("mods.avaritia.CraftingTable")
@ZenRegister
public class CraftingTable implements IRecipeManager<ISpecialRecipe> {
    private static final CraftingTable INSTANCE = new CraftingTable();

    @ZenCodeType.Method
    public static void addShaped(String name, int tier, IItemStack output, IIngredient[][] inputs) {
        var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
        if (tier > 4 || tier < 0) {
            tier = 0;
            CraftTweakerAPI.getLogger(Static.MOD_ID).error("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
        }
        int height = inputs.length;
        int width = 0;
        for (var row : inputs) {
            if (width < row.length) {
                width = row.length;
            }
        }
        var ingredients = NonNullList.withSize(height * width, Ingredient.EMPTY);

        for (int a = 0; a < height; a++) {
            for (int b = 0; b < inputs[a].length; b++) {
                var iing = inputs[a][b];
                var ing = iing.asVanillaIngredient();
                int i = a * width + b;
                ingredients.set(i, ing);

            }
        }

        var recipe = new ShapedTableCraftingRecipe(id, width, height, ingredients, output.getInternal(), tier);
        recipe.setTransformers((x, y, stack) -> inputs[y][x].getRemainingItem(new MCItemStack(stack)).getInternal());

        CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
    }

    @ZenCodeType.Method
    public static void addShapeless(String name, IItemStack output, IIngredient[] inputs) {
        addShapeless(name, 0, output, inputs);
    }

    @ZenCodeType.Method
    public static void addShapeless(String name, int tier, IItemStack output, IIngredient[] inputs) {
        var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
        if (tier > 4 || tier < 0) {
            tier = 0;
            CraftTweakerAPI.getLogger(Static.MOD_ID).error("Unable to assign a tier to the Table Recipe for stack " + output.getCommandString() + ". Tier cannot be greater than 4 or less than 0.");
        }
        var recipe = new ShapelessTableCraftingRecipe(id, toIngredientsList(inputs), output.getInternal(), tier);

        recipe.setTransformers((slot, stack) -> inputs[slot].getRemainingItem(new MCItemStack(stack)).getInternal());

        CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
    }

    @ZenCodeType.Method
    public static void addCatalyst(String name, IIngredient[] inputs) {
        var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
        var recipe = new InfinityCatalystCraftRecipe(id, "default", toIngredientsList(inputs));

        recipe.setTransformers((slot, stack) -> inputs[slot].getRemainingItem(new MCItemStack(stack)).getInternal());

        CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, recipe));
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<>(INSTANCE, recipe -> recipe.getResultItem(RegistryAccess.EMPTY).is(stack.getInternal().getItem())));
    }

    private static NonNullList<Ingredient> toIngredientsList(IIngredient... ingredients) {
        return Arrays.stream(ingredients)
                .map(IIngredient::asVanillaIngredient)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Override
    public RecipeType<ISpecialRecipe> getRecipeType() {
        return ModRecipeTypes.CRAFTING_TABLE_RECIPE.get();
    }
}
