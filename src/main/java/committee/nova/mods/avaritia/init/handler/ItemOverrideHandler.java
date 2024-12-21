package committee.nova.mods.avaritia.init.handler;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.item.MatterClusterItem;
import committee.nova.mods.avaritia.common.item.tools.infinity.InfinityCrossBowItem;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/31 19:50
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = Static.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemOverrideHandler {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setPropertyOverride(ModItems.infinity_pickaxe.get(), Static.rl("hammer"), (itemStack, world, livingEntity, d) -> {
                return itemStack.getOrCreateTag().getBoolean("hammer") ? 1 : 0;
            });
            setPropertyOverride(ModItems.infinity_shovel.get(), Static.rl("destroyer"), (itemStack, world, livingEntity, d) -> {
                return itemStack.getOrCreateTag().getBoolean("destroyer") ? 1 : 0;
            });
            setPropertyOverride(ModItems.matter_cluster.get(), Static.rl("cap"), (itemStack, world, livingEntity, d) -> {
                return MatterClusterItem.getClusterSize(itemStack) == MatterClusterItem.CAPACITY ? 1 : 0;
            });

            setPropertyOverride(ModItems.infinity_bow.get(), Static.rl("pull"), (itemStack, world, livingEntity, d) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(itemStack);
                }
            });
            setPropertyOverride(ModItems.infinity_bow.get(), Static.rl("pulling"), (itemStack, world, livingEntity, d) -> {
                return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
            });

            setPropertyOverride(ModItems.infinity_bow.get(), Static.rl("tracer"), (itemStack, world, livingEntity, d) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return CrossbowItem.isCharged(itemStack) && itemStack.getOrCreateTag().getBoolean("tracer") ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(itemStack);
                }
            });
            setPropertyOverride(ModItems.infinity_bow.get(), Static.rl("tracing"), (itemStack, world, livingEntity, d) -> {
                return livingEntity != null && livingEntity.isUsingItem()
                        && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack)
                        && itemStack.getOrCreateTag().getBoolean("tracer")
                        ? 1.0F : 0.0F;
            });


            setPropertyOverride(ModItems.infinity_crossbow.get(), Static.rl("pull"), (itemStack, world, livingEntity, d) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return InfinityCrossBowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / InfinityCrossBowItem.getChargeTime();
                }
            });
            setPropertyOverride(Items.CROSSBOW, new ResourceLocation("pulling"), (itemStack, level, livingEntity, p_174608_) -> {
                return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !InfinityCrossBowItem.isCharged(itemStack) ? 1.0F : 0.0F;
            });
            setPropertyOverride(ModItems.infinity_crossbow.get(), Static.rl("charged"), (itemStack, world, livingEntity, d) -> {
                return InfinityCrossBowItem.isCharged(itemStack) ? 1.0F : 0.0F;
            });
            setPropertyOverride(ModItems.infinity_crossbow.get(), Static.rl("firework"), (itemStack, world, livingEntity, d) -> {
                return InfinityCrossBowItem.isCharged(itemStack) && InfinityCrossBowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
            });
        });


    }

    public static void setPropertyOverride(Item itemProvider, ResourceLocation override, ItemPropertyFunction propertyGetter) {
        ItemProperties.register(itemProvider.asItem(), override, propertyGetter);
    }
}
