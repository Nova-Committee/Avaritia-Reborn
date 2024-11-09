package committee.nova.mods.avaritia.init.mixins;

import committee.nova.mods.avaritia.api.iface.IGrowingPlant;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * SugarCaneBlockMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/11/10 20:37
 */
@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin implements IGrowingPlant {
    @Unique
    @Override
    public Direction avaritia$getGrowthDirection() {
        return Direction.UP;
    }

    @Unique
    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean pIsClient) {
        if (this.avaritia$getConnectedPlantHeight(pLevel, pPos, pState.getBlock()) < this.avaritia$getMaxHeightAtPosition(
                pPos.getX(), pPos.getZ())) {
            return IGrowingPlant.super.isValidBonemealTarget(pLevel, pPos, pState, pIsClient);
        }
        return false;
    }

    @Unique
    @Override
    public void performBonemeal(@NotNull ServerLevel pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        if (pLevel.getBlockState(pPos.below(1)).is(ModBlocks.soul_farmland.get())) IGrowingPlant.super.performBonemeal(pLevel, pRandom, pPos, pState);
    }

    @Unique
    @Override
    public int avaritia$getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1 + random.nextInt(2);
    }

    @Unique
    @Override
    public boolean avaritia$canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Unique
    @Override
    public BlockState avaritia$getGrownBlockState(Block sourceBlock, BlockState sourceState) {
        return sourceBlock.defaultBlockState();
    }

    @Unique
    private int avaritia$getConnectedPlantHeight(BlockGetter blockGetter, BlockPos pos, Block block) {
        BlockPos pos1 = IGrowingPlant.getTopConnectedBlock(blockGetter, pos, block, avaritia$getGrowthDirection());
        BlockPos pos2 = IGrowingPlant.getTopConnectedBlock(blockGetter, pos, block, avaritia$getGrowthDirection().getOpposite());
        return Math.abs(pos1.getY() - pos2.getY());
    }

    @Unique
    private int avaritia$getMaxHeightAtPosition(int posX, int posZ) {
        // always use 0 seed, as client does not have access to world seed
        return 12 + WorldgenRandom.seedSlimeChunk(posX, posZ, 0, 987234911L).nextInt(5);
    }

}
