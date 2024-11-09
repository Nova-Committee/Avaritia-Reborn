package committee.nova.mods.avaritia.common.block.misc;

import committee.nova.mods.avaritia.api.common.block.BaseBlock;
import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
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
public class SoulFarmLandBlock extends BaseBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public SoulFarmLandBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(0.6F)
                .randomTicks()
                .sound(SoundType.GRAVEL));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (pFacing == Direction.UP && !pState.canSurvive(pLevel, pCurrentPos)) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
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
        }
    }
}
