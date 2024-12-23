package committee.nova.mods.avaritia.init.compat.jei.category;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static committee.nova.mods.avaritia.init.compat.jei.utils.AnvilRecipeMaker.findLevelsCost;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/23 19:51
 * @Description:
 */
public class ExtremeAnvilRecipeCategory implements IRecipeCategory<IJeiAnvilRecipe> {
    public static final RecipeType<IJeiAnvilRecipe> RECIPE_TYPE =
            RecipeType.create(Static.MOD_ID, "anvil", IJeiAnvilRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Static.MOD_ID, "textures/gui/jei/extreme_anvil_jei.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final String leftSlotName = "leftSlot";
    private final String rightSlotName = "rightSlot";

    public ExtremeAnvilRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(TEXTURE, 0, 0, 170, 64)
                .build();
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.extreme_anvil.get()));
    }

    @Override
    public @NotNull RecipeType<IJeiAnvilRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return ModBlocks.extreme_anvil.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiAnvilRecipe recipe, @NotNull IFocusGroup focuses) {
        List<ItemStack> leftInputs = recipe.getLeftInputs();
        List<ItemStack> rightInputs = recipe.getRightInputs();
        List<ItemStack> outputs = recipe.getOutputs();

        IRecipeSlotBuilder leftInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 23, 23)
                .addItemStacks(leftInputs)
                .setSlotName(leftSlotName);

        IRecipeSlotBuilder rightInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 72, 23)
                .addItemStacks(rightInputs)
                .setSlotName(rightSlotName);

        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 23)
                .addItemStacks(outputs);

        if (leftInputs.size() == rightInputs.size()) {
            if (leftInputs.size() == outputs.size()) {
                builder.createFocusLink(leftInputSlot, rightInputSlot, outputSlot);
            }
        } else if (leftInputs.size() == outputs.size() && rightInputs.size() == 1) {
            builder.createFocusLink(leftInputSlot, outputSlot);
        } else if (rightInputs.size() == outputs.size() && leftInputs.size() == 1) {
            builder.createFocusLink(rightInputSlot, outputSlot);
        }
    }

    @Override
    public void draw(@NotNull IJeiAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Optional<ItemStack> leftStack = recipeSlotsView.findSlotByName(leftSlotName)
                .flatMap(IRecipeSlotView::getDisplayedItemStack);

        Optional<ItemStack> rightStack = recipeSlotsView.findSlotByName(rightSlotName)
                .flatMap(IRecipeSlotView::getDisplayedItemStack);

        if (leftStack.isEmpty() || rightStack.isEmpty()) {
            return;
        }

        int cost = findLevelsCost(leftStack.get(), rightStack.get());
        String costText = cost < 0 ? "err" : Integer.toString(cost);
        String text = I18n.get("container.repair.cost", costText);

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        // Show red if the player doesn't have enough levels
        int mainColor = playerHasEnoughLevels(player, cost) ? 0xFF80FF20 : 0xFFFF6060;
        drawRepairCost(minecraft, guiGraphics, text, mainColor);
    }

    private static boolean playerHasEnoughLevels(@Nullable LocalPlayer player, int cost) {
        if (player == null) {
            return true;
        }
        if (player.isCreative()) {
            return true;
        }
        return cost < 40 && cost <= player.experienceLevel;
    }

    private void drawRepairCost(Minecraft minecraft, GuiGraphics guiGraphics, String text, int mainColor) {
        int width = minecraft.font.width(text);
        int x = getWidth() - 2 - width;
        int y = 27;
        guiGraphics.drawString(minecraft.font, text, x, y, mainColor);
    }


}
