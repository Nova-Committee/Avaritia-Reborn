package committee.nova.mods.avaritia.common.item.tools.infinity;

import committee.nova.mods.avaritia.api.iface.IMultiFunction;
import committee.nova.mods.avaritia.api.iface.InitEnchantItem;
import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import committee.nova.mods.avaritia.init.config.ModConfig;
import committee.nova.mods.avaritia.init.registry.*;
import committee.nova.mods.avaritia.util.ToolUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
 * Date: 2022/4/2 19:41
 * Version: 1.0
 */
public class InfinitySwordItem extends SwordItem implements IMultiFunction, InitEnchantItem {
    public InfinitySwordItem() {
        super(ModToolTiers.INFINITY_SWORD, 0, 0F, (new Properties())
                .rarity(ModRarities.COSMIC)
                .stacksTo(1)
                .fireResistant());
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity victim) {
        var level = player.level();
        var endlessDamage = ModConfig.isSwordAttackEndless.get();
        if (!level.isClientSide) {
            ToolUtils.sweepAttack(level, player, victim);//横扫
            if (victim instanceof EnderDragon dragon ) {
                victim.setInvulnerable(false);//取消无敌
                dragon.hurt(dragon.head, player.damageSources().source(ModDamageTypes.INFINITY, player, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
            } else if (victim instanceof Player pvp) {
                if (ToolUtils.isInfinite(pvp)) {
                    // 玩家身着无尽甲则只造成爆炸伤害
                    pvp.level().explode(player, pvp.getBlockX(), pvp.getBlockY(), pvp.getBlockZ(), 25.0f, Level.ExplosionInteraction.MOB);
                    return true;//直接返回
                } else {
                    victim.setInvulnerable(false);
                    victim.hurt(player.damageSources().source(ModDamageTypes.INFINITY, player, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
                }

            } else {
                victim.setInvulnerable(false);
                victim.hurt(player.damageSources().source(ModDamageTypes.INFINITY, player, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
            }

            if (endlessDamage) {
                if (victim.isAlive()) {
                    victim.remove(Entity.RemovalReason.DISCARDED);//修正死亡
                }
            }
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity victim, LivingEntity livingEntity) {
        var level = livingEntity.level();
        var endlessDamage = ModConfig.isSwordAttackEndless.get();
        if (!level.isClientSide && livingEntity instanceof Player player) {
            ToolUtils.sweepAttack(level, livingEntity, victim);//横扫
            if (victim instanceof EnderDragon dragon ) {
                victim.setInvulnerable(false);//取消无敌
                dragon.hurt(dragon.head, player.damageSources().source(ModDamageTypes.INFINITY, player, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
            } else if (victim instanceof Player pvp) {
                if (ToolUtils.isInfinite(pvp)) {
                    // 玩家身着无尽甲则只造成爆炸伤害
                    pvp.level().explode(livingEntity, pvp.getBlockX(), pvp.getBlockY(), pvp.getBlockZ(), 25.0f, Level.ExplosionInteraction.MOB);
                    return true;//直接返回
                } else {
                    victim.setInvulnerable(false);
                    victim.hurt(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
                }

            } else {
                victim.setInvulnerable(false);
                victim.hurt(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), endlessDamage ? Float.MAX_VALUE : ModToolTiers.INFINITY_SWORD.getAttackDamageBonus());
            }

            victim.lastHurtByPlayerTime = 60;
            victim.getCombatTracker().recordDamage(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim), victim.getHealth());


            if (endlessDamage) {
                victim.setHealth(0);//设置血量为零
                victim.die(livingEntity.damageSources().source(ModDamageTypes.INFINITY, livingEntity, victim));//设置死亡
                if (victim.isAlive()) {
                    victim.remove(Entity.RemovalReason.DISCARDED);//修正死亡
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        var heldItem = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ToolUtils.aoeAttack(player, ModConfig.swordAttackRange.get(), ModConfig.swordRangeDamage.get(), ModConfig.isSwordAttackAnimal.get(), ModConfig.isSwordAttackLightning.get());
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
        }
        level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 5.0f);
        return InteractionResultHolder.success(heldItem);
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return ImmortalItemEntity.create(ModEntities.IMMORTAL.get(), level, location.getX(), location.getY(), location.getZ(), stack);
    }

    @Override
    public int getInitEnchantLevel(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.MOB_LOOTING ? 10 : 0;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                @NotNull TooltipFlag isAdvanced) {
        tooltipComponents.add(ModTooltips.INIT_ENCHANT.args(Enchantments.MOB_LOOTING.getFullname(10)).build());
    }
}
