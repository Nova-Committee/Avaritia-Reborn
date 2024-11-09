package committee.nova.mods.avaritia.common.tile;

import committee.nova.mods.avaritia.api.common.tile.BaseTileEntity;
import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/10 00:46
 * @Description:
 */
public class SoulFarmLandTile extends BaseTileEntity {
    public SoulFarmLandTile(BlockPos pos, BlockState state) {
        super(ModTileEntities.soul_farmland_tile.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SoulFarmLandTile tile) {
        if (level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        Block aboveBlock = aboveState.getBlock();

        if (aboveBlock instanceof TallFlowerBlock) {
            return;
        }

        if (ModConfig.growth_soul_farmland.get() == 0.0) {
            return;
        }

        if (
                level.random.nextFloat() <= ModConfig.growth_soul_farmland.get()
        ) {
            if (aboveBlock instanceof BonemealableBlock growable && growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
                growable.performBonemeal(serverLevel, level.random, pos.above(), aboveState);
                level.levelEvent(2005, pos.above(), 0);
                ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
            }

            if (aboveBlock instanceof NetherWartBlock && aboveState.getValue(NetherWartBlock.AGE) < 3){
                aboveState.setValue(NetherWartBlock.AGE, aboveState.getValue(NetherWartBlock.AGE) + 1);
            }

            serverLevel.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1D, 0);
        }


    }
}
