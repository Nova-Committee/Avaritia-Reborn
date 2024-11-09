package committee.nova.mods.avaritia.api.model;

import net.minecraft.network.chat.Component;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/9 14:50
 * @Description:
 */
public record SuperFunction(int id, Component name) {

    public static final SuperFunction DEFAULT = new SuperFunction(0, Component.translatable("tooltip.function.default"));
}
