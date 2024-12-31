package committee.nova.mods.avaritia.init.handler;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.iface.IFilterItem;
import committee.nova.mods.avaritia.common.capability.ItemFiltersProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * CapHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/7 1:48
 */
@Mod.EventBusSubscriber
public class CapHandler {

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
    }

    @SubscribeEvent
    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof IFilterItem)
            event.addCapability(new ResourceLocation(Static.MOD_ID, "filters"), new ItemFiltersProvider());
    }
}
