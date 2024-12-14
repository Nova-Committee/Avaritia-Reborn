package committee.nova.mods.avaritia.init.compat.kubejs;

import committee.nova.mods.avaritia.init.registry.ModRecipeSerializers;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

/**
 * Name: Avaritia-forge / KubeJSAvaritiaPlugin
 * Author: cnlimiter
 * CreateTime: 2023/9/17 0:49
 * Description:
 */

public class KubeJSAvaritiaPlugin extends KubeJSPlugin {

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ModRecipeSerializers.SHAPED_CRAFT_SERIALIZER.getId(), ShapedTableRecipeSchema.SCHEMA);
        event.register(ModRecipeSerializers.SHAPELESS_CRAFT_SERIALIZER.getId(), ShapelessTableRecipeSchema.SCHEMA);
        event.register(ModRecipeSerializers.COMPRESSOR_SERIALIZER.getId(), CompressRecipeSchema.SCHEMA);
        event.register(ModRecipeSerializers.INFINITY_CATALYST_CRAFT_SERIALIZER.getId(), InfinityCatalystRecipeSchema.SCHEMA);
        event.register(ModRecipeSerializers.ETERNAL_SINGULARITY_CRAFT_SERIALIZER.getId(), InfinityCatalystRecipeSchema.SCHEMA);
    }
}
