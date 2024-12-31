package committee.nova.mods.avaritia.init.registry;

import committee.nova.mods.avaritia.common.capability.ItemFilters;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * ModCaps
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/7 1:48
 */
public class ModCaps {
    public static final Capability<ItemFilters> ITEM_FILTERS = CapabilityManager.get(new CapabilityToken<>() {});


}
