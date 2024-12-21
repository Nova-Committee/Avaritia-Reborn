package committee.nova.mods.avaritia.init.data.provider;

import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.init.data.ModDataGen;
import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Name: Avaritia-forge / ModDamageTypeTags
 * Author: cnlimiter
 * CreateTime: 2023/9/10 0:39
 * Description:
 */

public class ModDamageTypeTags extends TagsProvider<DamageType> {

    public ModDamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, Static.MOD_ID, helper);
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.INFINITY);
        this.tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).add(ModDamageTypes.INFINITY);
    }
}
