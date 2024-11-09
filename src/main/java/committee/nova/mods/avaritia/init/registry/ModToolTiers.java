package committee.nova.mods.avaritia.init.registry;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/31 10:33
 * Version: 1.0
 */
public enum ModToolTiers implements net.minecraft.world.item.Tier {
    CRYSTAL_SWORD(8888, 8888, 8888f, 50F, 0, () -> {
        return Ingredient.of(ModItems.crystal_matrix_ingot.get());
    }),
    CRYSTAL_PICKAXE(8888, 8888, 8888f, 8.0F, 0, () -> {
        return Ingredient.of(ModItems.crystal_matrix_ingot.get());
    }),
    BLAZE_PICKAXE(7777, 7777, 7777f, 10F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    BLAZE_SWORD(7777, 7777, 7777f, 25F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    BLAZE_SHOVEL(7777, 7777, 7777f, 10F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    BLAZE_HOE(7777, 7777, 7777f, 10F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    BLAZE_AXE(7777, 7777, 7777f, 35F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    INFINITY_PICKAXE(9999, 9999, 9999f, 50F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    INFINITY_SWORD(9999, 9999, 9999f, 1000F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    INFINITY_SHOVEL(9999, 9999, 9999f, 50F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    INFINITY_HOE(9999, 9999, 9999f, 50F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    }),
    INFINITY_AXE(9999, 9999, 9999f, 100F, 0, () -> {
        return Ingredient.of(ModItems.infinity_ingot.get());
    });


    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModToolTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
