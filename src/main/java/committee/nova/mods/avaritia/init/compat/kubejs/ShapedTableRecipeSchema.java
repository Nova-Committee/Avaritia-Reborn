package committee.nova.mods.avaritia.init.compat.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema;

import static dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema.*;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/9 22:47
 * @Description:
 */
public interface ShapedTableRecipeSchema {
    RecipeKey<Integer> TIER = NumberComponent.INT.key("tier").optional(0);
    RecipeSchema SCHEMA = new RecipeSchema(ShapedRecipeSchema.ShapedRecipeJS.class, ShapedRecipeSchema.ShapedRecipeJS::new, TIER, RESULT, PATTERN, KEY, KJS_MIRROR, KJS_SHRINK)
            .constructor(TIER, RESULT, PATTERN, KEY)
            .uniqueOutputId(RESULT);
}
