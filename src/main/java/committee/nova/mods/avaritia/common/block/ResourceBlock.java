package committee.nova.mods.avaritia.common.block;

import committee.nova.mods.avaritia.api.common.block.BaseBlock;
import committee.nova.mods.avaritia.init.registry.ModResourceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * Name: Avaritia-forge / ResourceBlock
 * Author: cnlimiter
 * CreateTime: 2023/8/13 13:20
 * Description:
 */

public class ResourceBlock extends BaseBlock {
    public ResourceBlock(ModResourceBlocks type) {
        super(MapColor.METAL, SoundType.METAL, type.hardness, type.resistance);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 20.0f;
    }


}
