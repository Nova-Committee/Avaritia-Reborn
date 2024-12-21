package committee.nova.mods.avaritia.init.data;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.init.data.provider.ModDamageTypeTags;
import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static committee.nova.mods.avaritia.init.registry.ModDamageTypes.DAMAGE_BUILDER;

/**
 * Name: Avaritia-forge / ModRegistries
 * Author: cnlimiter
 * CreateTime: 2023/9/10 0:40
 * Description:
 */

public class ModRegistries extends DatapackBuiltinEntriesProvider {

    public ModRegistries(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future, DAMAGE_BUILDER, Set.of("minecraft", Static.MOD_ID));
    }
}
