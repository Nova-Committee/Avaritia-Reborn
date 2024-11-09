package committee.nova.mods.avaritia.api.iface;

import committee.nova.mods.avaritia.api.model.SuperFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/4 00:16
 * @Description:
 */
public interface IMultiFunction {
    //public List<SuperFunction> getFunctions();
    default int getFunc(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("avaritia_func")) return 0;
        return stack.getOrCreateTag().getInt("avaritia_func");
    }

    default void switchFunc(@NotNull Level world, Player player, @NotNull InteractionHand hand, SuperFunction func) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tags = stack.getOrCreateTag();
        tags.putInt("avaritia_func", func.id());
        if(!world.isClientSide && player instanceof ServerPlayer serverPlayer)
            serverPlayer.sendSystemMessage(Component.translatable("tooltip.avaritia.switch").append(func.name()), true);
        player.swing(hand);
    }
}
