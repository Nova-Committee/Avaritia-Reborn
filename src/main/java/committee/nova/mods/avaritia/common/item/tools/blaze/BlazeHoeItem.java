package committee.nova.mods.avaritia.common.item.tools.blaze;

import committee.nova.mods.avaritia.api.iface.ISwitchable;
import committee.nova.mods.avaritia.api.iface.ITooltip;
import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia.init.registry.ModToolTiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 20:00
 * Version: 1.0
 */
public class BlazeHoeItem extends HoeItem implements ITooltip, ISwitchable {
    private final String name;
    public BlazeHoeItem(String name) {
        super(ModToolTiers.BLAZE_SWORD, 0, -2.4f,
                new Properties()
                        .rarity(ModRarities.EPIC)
                        .stacksTo(1)
                        .fireResistant());

        this.name = name;
    }
    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        this.appendTooltip(pStack, pLevel, pTooltipComponents, pIsAdvanced, name);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            switchMode(world, player, hand);
            return InteractionResultHolder.success(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        var level = pContext.getLevel();
        var blockpos = pContext.getClickedPos();
        var blockstate = level.getBlockState(blockpos);
        if (blockstate.is(Blocks.SOUL_SAND)){
            level.setBlockAndUpdate(blockpos, Blocks.SOUL_SOIL.defaultBlockState());
            return InteractionResult.SUCCESS;
        } else if (blockstate.is(Blocks.SOUL_SOIL)){
            level.setBlockAndUpdate(blockpos, ModBlocks.soul_farmland.get().defaultBlockState());
            return InteractionResult.SUCCESS;
        } else return super.useOn(pContext);
    }
}
