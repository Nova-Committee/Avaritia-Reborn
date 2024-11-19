package committee.nova.mods.avaritia.api.iface;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/10 01:32
 * @Description:
 */
public interface IGrowingPlant extends BonemealableBlock {

    public static BlockPos getTopConnectedBlock(BlockGetter level, BlockPos pos, Block block, Direction direction) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        BlockState blockstate;
        do {
            mutable.move(direction);
            blockstate = level.getBlockState(mutable);
        } while (blockstate.is(block));
        return mutable.move(direction.getOpposite());
    }

    Direction avaritia$getGrowthDirection();

    @Override
    public default boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean pIsClient) {
        BlockPos headPos = this.getHeadPos(pLevel, pPos, pState.getBlock());
        return this.avaritia$canGrowInto(pLevel.getBlockState(headPos.relative(avaritia$getGrowthDirection())));
    }

    @Override
    public default boolean isBonemealSuccess(@NotNull Level pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return true;
    }

    @Override
    public default void performBonemeal(@NotNull ServerLevel pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        BlockPos topPos = this.getHeadPos(pLevel, pPos, pState.getBlock());
        this.performBonemealTop(pLevel, pRandom, topPos, pState);
    }

    private void performBonemealTop(ServerLevel level, RandomSource randomSource, BlockPos topPos, BlockState sourceState) {
        BlockPos blockpos = topPos.relative(avaritia$getGrowthDirection());
        int j = this.avaritia$getBlocksToGrowWhenBonemealed(randomSource);
        for (int k = 0; k < j && this.avaritia$canGrowInto(level.getBlockState(blockpos)); ++k) {
            level.setBlockAndUpdate(blockpos, this.avaritia$getGrownBlockState(sourceState.getBlock(), sourceState));
            blockpos = blockpos.relative(avaritia$getGrowthDirection());
        }
    }

    private BlockPos getHeadPos(BlockGetter level, BlockPos blockPos, Block block) {
        return getTopConnectedBlock(level, blockPos, block, avaritia$getGrowthDirection());
    }

    public int avaritia$getBlocksToGrowWhenBonemealed(RandomSource random);

    public boolean avaritia$canGrowInto(BlockState state);

    public BlockState avaritia$getGrownBlockState(Block sourceBlock, BlockState sourceState);
}
