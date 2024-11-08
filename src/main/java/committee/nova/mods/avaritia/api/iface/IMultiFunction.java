package committee.nova.mods.avaritia.api.iface;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/4 00:16
 * @Description:
 */
public interface IMultiFunction {
    default int getFunc(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("avaritia_func")) return 0;
        return stack.getOrCreateTag().getInt("avaritia_func");
    }

    default void switchFunc(@NotNull Level world, Player player, @NotNull InteractionHand hand, int func, Component funcName) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tags = stack.getOrCreateTag();
        tags.putInt("avaritia_func", func);
        if(!world.isClientSide && player instanceof ServerPlayer serverPlayer) serverPlayer.sendSystemMessage(Component.translatable("tooltip.avaritia.switch").append(funcName), true);
        player.swing(hand);
    }
}
