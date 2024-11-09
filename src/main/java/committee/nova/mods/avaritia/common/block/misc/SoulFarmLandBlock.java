package committee.nova.mods.avaritia.common.block.misc;

import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/5 00:55
 * @Description:
 */
public class SoulFarmLandBlock extends Block {
    public SoulFarmLandBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(0.6F)
                .randomTicks()
                .sound(SoundType.GRAVEL));
    }

    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!level.isClientSide) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            Block aboveBlock = aboveState.getBlock();

            if (aboveBlock instanceof TallFlowerBlock) {
                return;
            }

            if (ModConfig.growth_soul_farmland.get() == 0.0) {
                return;
            }

            if (aboveBlock instanceof BonemealableBlock growable
                    && level.random.nextFloat() <= ModConfig.growth_soul_farmland.get()
            ) {
                if (growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                    growable.performBonemeal(level, level.random, pos.above(), aboveState);
                    level.levelEvent(2005, pos.above(), 0);
                    ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
                }
            }

            if (aboveBlock instanceof NetherWartBlock && aboveState.getValue(NetherWartBlock.AGE) < 3){
                aboveState.setValue(NetherWartBlock.AGE, aboveState.getValue(NetherWartBlock.AGE) + 1);
            }
        }
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            return ModBlocks.soul_farmland.get().defaultBlockState();
        }
        return null;
    }

    @Override
    public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Direction facing, net.minecraftforge.common.@NotNull IPlantable plantable) {
        return true;
    }
}
