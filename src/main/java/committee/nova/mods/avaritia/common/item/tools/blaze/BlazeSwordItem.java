package committee.nova.mods.avaritia.common.item.tools.blaze;

import committee.nova.mods.avaritia.api.iface.ISwitchable;
import committee.nova.mods.avaritia.api.iface.ITooltip;
import committee.nova.mods.avaritia.api.iface.InitEnchantItem;
import committee.nova.mods.avaritia.common.entity.FireBallEntity;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia.init.registry.ModToolTiers;
import committee.nova.mods.avaritia.init.registry.ModTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 20:00
 * Version: 1.0
 */
public class BlazeSwordItem extends SwordItem implements ITooltip, ISwitchable, InitEnchantItem {
    private final String name;
    public BlazeSwordItem(String name) {
        super(ModToolTiers.BLAZE_SWORD, 0, -2.4f,
                new Properties()
                        .rarity(ModRarities.EPIC)
                        .stacksTo(1)
                        .fireResistant());

        this.name = name;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public int getInitEnchantLevel(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.FIRE_ASPECT ? 10 : 0;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                @NotNull TooltipFlag isAdvanced) {
        tooltipComponents.add(ModTooltips.INIT_ENCHANT.args(Enchantments.FIRE_ASPECT.getFullname(10)).build());
        this.appendTooltip(stack, level, tooltipComponents, isAdvanced, name);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        var heldItem = player.getItemInHand(hand);
        if (!level.isClientSide) {
            FireBallEntity fireBallEntity = ModEntities.FIRE_BALL.get().create(level);
            if (fireBallEntity != null){
                fireBallEntity.setOwner(player);
                fireBallEntity.setPos(player.getX(), player.getEyeY() + 0.1, player.getZ());
                fireBallEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(fireBallEntity);
            }

        }
        level.playSound(player, player.getOnPos(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
        return InteractionResultHolder.success(heldItem);
    }
}