package committee.nova.mods.avaritia.common.block.craft;

import committee.nova.mods.avaritia.api.common.block.BaseTileEntityBlock;
import committee.nova.mods.avaritia.common.tile.TierCraftTile;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModCraftTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 7:38
 * Version: 1.0
 */
public class TierCraftTableBlock extends BaseTileEntityBlock {
    ModCraftTier tier;
    public TierCraftTableBlock(ModCraftTier tier) {
        super(MapColor.METAL, tier.sound, tier.hardness, tier.resistance, true);
        this.tier = tier;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            var tile = level.getBlockEntity(pos);
            if (tile instanceof TierCraftTile table) {
                NetworkHooks.openScreen(serverPlayer, table, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var tile = level.getBlockEntity(pos);

            if (tile instanceof TierCraftTile table) {
                Containers.dropContents(level, pos, table.getInventory().getStacks());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TierCraftTile(pos, state);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // 遍历方向列表，检查每个方向的方块状态
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.relative(direction);
            if (level.getBlockState(offsetPos).is( ModBlocks.infinity.get())) {
                return 15;
            }
        }
        return this.tier.lightLevel;
    }
}
