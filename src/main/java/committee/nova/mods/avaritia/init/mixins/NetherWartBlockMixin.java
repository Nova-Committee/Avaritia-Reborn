package committee.nova.mods.avaritia.init.mixins;

import committee.nova.mods.avaritia.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * NetherWartBlockMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/2 20:37
 */
@Mixin(NetherWartBlock.class)
public abstract class NetherWartBlockMixin {

    @Inject(
            method = "mayPlaceOn",
            at = @At("HEAD"),
            cancellable = true
    )
    public void avaritia$mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        if (pState.is(ModBlocks.soul_farmland.get())) cir.setReturnValue(true);
    }

}
