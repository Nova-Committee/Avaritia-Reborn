package committee.nova.mods.avaritia.common.crafting.recipe;

import com.google.gson.JsonObject;
import committee.nova.mods.avaritia.api.common.crafting.ICompressorRecipe;
import committee.nova.mods.avaritia.api.common.crafting.ISpecialRecipe;
import committee.nova.mods.avaritia.init.registry.ModRecipeSerializers;
import committee.nova.mods.avaritia.init.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 17:40
 * Version: 1.0
 */
public class CompressorRecipe implements ISpecialRecipe, ICompressorRecipe {
    private final ResourceLocation recipeId;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final int inputCount;
    private final int timeRequire;


    public CompressorRecipe(ResourceLocation recipeId, Ingredient input, ItemStack output, int inputCount, int timeRequire) {
        this.recipeId = recipeId;
        this.inputs = NonNullList.of(Ingredient.EMPTY, input);
        this.output = output;
        this.inputCount = inputCount;
        this.timeRequire = timeRequire;

    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }


    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess p_267052_) {
        return this.output;
    }

    public @NotNull ItemStack getResultItem() {
        return this.output;
    }


    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public int getTimeCost() {
        return timeRequire;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COMPRESSOR_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.COMPRESSOR_RECIPE.get();
    }

    @Override
    public ItemStack assemble(IItemHandler inventory) {
        return this.output.copy();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container inv, @NotNull RegistryAccess p_267052_) {
        return this.output.copy();
    }

    @Override
    public boolean matches(IItemHandler inventory) {
        var input = inventory.getStackInSlot(0);

        return this.inputs.get(0).test(input);
    }

    @Override
    public boolean matches(@NotNull Container inv, @NotNull Level level) {
        return this.matches(new InvWrapper(inv));
    }

    @Override
    public int getInputCount() {
        return this.inputCount;
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {
        @Override
        public @NotNull CompressorRecipe fromJson(@NotNull ResourceLocation recipeId, JsonObject json) {
            var input = Ingredient.fromJson(json.getAsJsonObject("ingredient"));
            var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int inputCount = GsonHelper.getAsInt(json, "inputCount", 1000);
            int timeCost = GsonHelper.getAsInt(json, "timeCost");
            return new CompressorRecipe(recipeId, input, output, inputCount, timeCost);
        }

        @Override
        public CompressorRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            var input = Ingredient.fromNetwork(buffer);
            var output = buffer.readItem();
            int inputCount = buffer.readInt();
            int timeCost = buffer.readInt();

            return new CompressorRecipe(recipeId, input, output, inputCount, timeCost);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, CompressorRecipe recipe) {
            recipe.inputs.get(0).toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeInt(recipe.inputCount);
            buffer.writeInt(recipe.timeRequire);


        }
    }
}
