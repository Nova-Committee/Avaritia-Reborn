//package committee.nova.mods.avaritia.common.entity;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.FireworkRocketEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.NotNull;
//
///**
// * @Project: Avaritia
// * @Author: cnlimiter
// * @CreateTime: 2024/11/9 17:21
// * @Description: <a href="https://github.com/yuoft/Endless/blob/master/src/main/java/com/yuo/endless/Entity/InfinityFireWorkEntity.java">...</a>
// */
//public class InfinityFireWorkEntity extends FireworkRocketEntity {
//
//
//    public InfinityFireWorkEntity(EntityType<? extends FireworkRocketEntity> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//    }
//
//
//    //有无爆炸数据
//    private boolean hasNbt() {
//        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
//        CompoundTag compoundnbt = itemstack.isEmpty() ? null : itemstack.getOrCreateTagElement("Fireworks");
//        ListTag listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
//        return listnbt != null && !listnbt.isEmpty();
//    }
//
//    //爆炸后攻击范围内生物
//    private void dealExplosionDamage() {
//        float f = 0.0F;
//        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
//        CompoundTag compoundnbt = itemstack.isEmpty() ? null : itemstack.getOrCreateTagElement("Fireworks");
//        ListTag listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
//        if (listnbt != null && !listnbt.isEmpty()) {
//            f = 5.0F + (float)(listnbt.size() * 2);
//        }
//
//        if (f > 0.0F) {
//            if (this.boostedEntity != null) {
//                this.boostedEntity.attackEntityFrom(DamageSource.causeFireworkDamage(this, this.getShooter()), this.damage + (float)(listnbt.size() * 2));
//            }
//
//            double d0 = 5.0D; //对5格范围内生物造成伤害
//            Vec3 vector3d = this.getOnPos().getCenter();
//
//            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(d0))) {
//                if (livingentity != this.boostedEntity && !(this.getDistanceSq(livingentity) > 25.0D)) {
//                    boolean flag = false;
//
//                    for(int i = 0; i < 2; ++i) { //是否被方块阻挡 阻挡无伤害
//                        Vector3d vector3d1 = new Vector3d(livingentity.getPosX(), livingentity.getPosYHeight(0.5D * (double)i), livingentity.getPosZ());
//                        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
//                        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
//                            flag = true;
//                            break;
//                        }
//                    }
//
//                    if (flag) { //伤害衰减
//                        float f1 = f * (float)Math.sqrt((this.damage - (double)this.getDistance(livingentity)) / 2.5D);
//                        livingentity.attackEntityFrom(DamageSource.causeFireworkDamage(this, this.getShooter()), f1);
//                    }
//                }
//            }
//        }
//
//    }
//}
