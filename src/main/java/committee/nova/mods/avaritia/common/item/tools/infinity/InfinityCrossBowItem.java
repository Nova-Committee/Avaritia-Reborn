package committee.nova.mods.avaritia.common.item.tools.infinity;

import com.google.common.collect.Lists;
import committee.nova.mods.avaritia.api.iface.ITooltip;
import committee.nova.mods.avaritia.common.entity.ImmortalItemEntity;
import committee.nova.mods.avaritia.common.entity.arrow.HeavenSubArrowEntity;
import committee.nova.mods.avaritia.init.registry.ModEntities;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/4/2 20:07
 * Version: 1.0
 * from <a href="https://github.com/yuoft/Endless/blob/master/src/main/java/com/yuo/endless/Items/Tool/InfinityCrossBow.java">...</a>
 */
public class InfinityCrossBowItem extends CrossbowItem implements ITooltip {
    public static final Predicate<ItemStack> ARROWS = (stack) ->
            stack.is(ItemTags.ARROWS) || stack.getItem() == Items.FIREWORK_ROCKET;

    private boolean isLoadingStart = false;

    private boolean isLoadingMiddle = false;

    public InfinityCrossBowItem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(ModRarities.COSMIC)
                .fireResistant()
        );
    }

    public static int getChargeTime() {
        return 25 - 5 * 3;//快速装填3
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药 无尽矢 普通箭矢 烟花火箭
     */
    private static ItemStack findArrow(LivingEntity living) {
        if (living instanceof Player player) {
            ItemStack heldAmmo = getHeldAmmo(player, ARROWS);
            if (!heldAmmo.isEmpty()) return heldAmmo;
            else {
                for (NonNullList<ItemStack> e : player.getInventory().compartments) {
                    for (ItemStack stack1 : e) {
                        if (stack1.is(Items.ARROW)) return stack1;
                        if (stack1.getItem() == Items.FIREWORK_ROCKET) return stack1;
                    }
                }
                return player.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getHeldAmmo(LivingEntity living, Predicate<ItemStack> isAmmo) {
        if (isAmmo.test(living.getOffhandItem())) {
            return living.getOffhandItem();
        } else {
            return isAmmo.test(living.getMainHandItem()) ? living.getOffhandItem() : ItemStack.EMPTY;
        }
    }

    /**
     * 获取弹药速度
     *
     * @param stack 弩
     * @return 速度
     */
    private static float getSpeed(ItemStack stack) {
        return stack.getItem() == Items.CROSSBOW && containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public static void fireProjectiles(Level worldIn, LivingEntity shooter, ItemStack stack, float velocityIn, float inaccuracyIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.random); //声音大小

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof Player player && player.isCreative();
            if (!itemstack.isEmpty()) {
                if (list.size() <= 3) {
                    if (i == 0) {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
                    } else if (i == 1) {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
                    } else {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
                    }
                } else { // 无尽箭矢 扇形射出大量箭矢 中间为无尽箭
                    {
                        fireProjectile(worldIn, shooter, stack, new ItemStack(Items.ARROW), afloat[i < 10 ? 1 : 2], flag, velocityIn, inaccuracyIn, getArrowAngle(i, i < 10));
                    }
                }
            } else {
                fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
            }
        }

        fireProjectilesAfter(worldIn, shooter, stack);
    }

    /**
     * 获取箭矢散射角度
     *
     * @param i    箭矢序数
     * @param flag 是否偏向左边
     * @return 角度
     */
    private static float getArrowAngle(int i, boolean flag) {
        return flag ? -(45f - i * 4.5f) : (i - 10) * 4.5f;
    }

    private static float[] getRandomSoundPitches(RandomSource rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomSoundPitch(flag, rand), getRandomSoundPitch(!flag, rand)};
    }

    private static float getRandomSoundPitch(boolean flagIn, RandomSource rand) {
        float f = flagIn ? 0.63F : 0.43F;
        return 1.0F / (rand.nextFloat() * 0.5F + 1.8F) + f;
    }

    /**
     * 获取装填的弹药
     *
     * @param pCrossbowStack 弩
     * @return 弹药列表
     */
    private static List<ItemStack> getChargedProjectiles(ItemStack pCrossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag1 = listtag.getCompound(i);
                list.add(ItemStack.of(compoundtag1));
            }
        }
        return list;
    }

    //修改玩家状态
    private static void fireProjectilesAfter(Level worldIn, LivingEntity shooter, ItemStack stack) {
        if (shooter instanceof ServerPlayer serverPlayer) {
            if (!worldIn.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverPlayer, stack);
            }
            serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    //清除弩上的弹药
    private static void clearProjectiles(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }
    }

    //射出一发
    private static void fireProjectile(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle) {
        if (!pLevel.isClientSide) {
            boolean flag = pAmmoStack.is(Items.FIREWORK_ROCKET);
            Projectile projectile;
            if (flag) {
                projectile = new FireworkRocketEntity(pLevel, pAmmoStack, pShooter, pShooter.getX(), pShooter.getEyeY() - (double) 0.15F, pShooter.getZ(), true);
            } else {
                projectile = createArrow(pLevel, pShooter, pCrossbowStack, pAmmoStack);
                ((AbstractArrow) projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            if (pShooter instanceof CrossbowAttackMob crossbowattackmob) {
                crossbowattackmob.shootCrossbowProjectile(Objects.requireNonNull(crossbowattackmob.getTarget()), pCrossbowStack, projectile, pProjectileAngle);
            } else {
                Vec3 vec31 = pShooter.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(pProjectileAngle * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = pShooter.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), pVelocity, pInaccuracy);
            }

            pLevel.addFreshEntity(projectile);
            pLevel.playSound(null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }
    }

    //创建投掷物实体
    private static AbstractArrow createArrow(Level worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        AbstractArrow arrow = null;
        if (ammo.isEmpty()) { //弹药为空发射普通箭矢
            ItemStack stack = new ItemStack(Items.ARROW);
            ArrowItem arrowitem = (ArrowItem) (stack.getItem() instanceof ArrowItem ? stack.getItem() : Items.ARROW);
            arrow = arrowitem.createArrow(worldIn, ammo, shooter);
            arrow.setBaseDamage(20);
            arrow.setPierceLevel((byte) 2);
        } else {
            arrow = HeavenSubArrowEntity.create(worldIn, shooter.getX(), shooter.getY() + 1.2, shooter.getZ());
            arrow.setOwner(shooter);
            arrow.setPierceLevel((byte) 5);
        }
        if (shooter instanceof Player) {
            arrow.setCritArrow(true);
        } //暴击粒子
        arrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        arrow.setShotFromCrossbow(true);

        return arrow;
    }

    //使用程度
    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float) useTime / (float) getChargeTime();
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        boolean flag = entityIn instanceof Player player && player.isCreative();
        ItemStack itemstack = findArrow(entityIn); //无弹药 发射一发普通箭
        int j = itemstack.isEmpty() ? 1 :
                (itemstack.is(ItemTags.ARROWS) || itemstack.getItem() == Items.FIREWORK_ROCKET ? 3 : 21);
        ItemStack itemstack1 = itemstack.copy();

        for (int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!deleteStack(entityIn, stack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 消耗库存弹药
     *
     * @param living    实体
     * @param stack     弩
     * @param itemStack 弹药
     * @param flag0     弹药数量是否超过一
     * @param flag1     是否创造模式
     * @return true
     */
    private static boolean deleteStack(LivingEntity living, ItemStack stack, ItemStack itemStack, boolean flag0, boolean flag1) {
        boolean flag = flag1 && itemStack.getItem() instanceof ArrowItem;  //普通弹药被消耗
        ItemStack itemstack;
        if (!flag && !flag1 && !flag0) {
            itemstack = itemStack.split(1);
            if (itemStack.isEmpty() && living instanceof Player player) {
                player.getInventory().removeItem(itemStack);
            }
        } else {
            itemstack = itemStack.copy();
        }

        addChargedProjectile(stack, itemstack);
        return true;
    }

    //为弩添加弹药数据
    private static void addChargedProjectile(ItemStack crossbow, ItemStack projectile) {
        CompoundTag compoundnbt = crossbow.getOrCreateTag();
        ListTag listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListTag();
        }

        CompoundTag compoundnbt1 = new CompoundTag();
        projectile.save(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 99;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return ImmortalItemEntity.create(ModEntities.IMMORTAL.get(), level, location.getX(), location.getY(), location.getZ(), stack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return getChargeTime() + 3; //使用时间
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isCharged(itemstack)) { //弹药以装填
            fireProjectiles(level, player, itemstack, getSpeed(itemstack), 1.0F);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (!findArrow(player).isEmpty()) { //玩家有弹药
            if (!isCharged(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                player.startUsingItem(hand);
            }
            return InteractionResultHolder.consume(itemstack);
        } else if (findArrow(player).isEmpty() && !isCharged(itemstack)) { //无弹药依然触发装填
            this.isLoadingStart = false;
            this.isLoadingMiddle = false;
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level pLevel, @NotNull LivingEntity entity, int pTimeLeft) {
        int i = this.getUseDuration(stack) - pTimeLeft;
        float f = getCharge(i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(entity, stack)) {
            setCharged(stack, true);
            SoundSource soundcategory = entity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (entity.random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }


}
