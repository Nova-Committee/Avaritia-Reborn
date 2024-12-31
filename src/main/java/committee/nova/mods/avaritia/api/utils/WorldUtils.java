package committee.nova.mods.avaritia.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/28 20:10
 * @Description:
 */
public class WorldUtils {
    /**
     * 获取玩家当前位置的环境亮度
     *
     * @param player 当前玩家实体
     * @return 当前环境亮度（范围0-15）
     */
    public static int getEnvironmentBrightness(Player player) {
        int result = 0;
        if (player != null) {
            Level world = player.level();
            BlockPos pos = player.blockPosition();
            // 获取基础的天空光亮度和方块光亮度
            int skyLight = world.getBrightness(LightLayer.SKY, pos);
            int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
            // 获取世界时间、天气和维度的影响
            boolean isDay = world.isDay();
            boolean isRaining = world.isRaining();
            boolean isThundering = world.isThundering();
            boolean isUnderground = !world.canSeeSky(pos);
            // 判断世界维度（地表、下界、末地）
            if (world.dimension() == Level.OVERWORLD) {
                // 如果在地表
                if (!isUnderground) {
                    if (isDay) {
                        // 白天地表：最高亮度
                        result = isThundering ? 6 : isRaining ? 9 : 15;
                    } else {
                        // 夜晚地表
                        // 获取月相，0表示满月，4表示新月
                        int moonPhase = world.getMoonPhase();
                        result = getMoonBrightness(moonPhase, isThundering, isRaining);
                    }
                } else {
                    // 地下环境
                    // 没有光源时最黑，有光源则受距离影响
                    result = Math.max(Math.min(blockLight, 12), 0);
                }
            } else if (world.dimension() == Level.NETHER) {
                // 下界亮度较暗，但部分地方有熔岩光源
                // 近光源则亮度提升，但不会超过10
                result = Math.min(7 + blockLight / 2, 10);
            } else if (world.dimension() == Level.END) {
                // 末地亮度通常较暗
                // 即使贴近光源，末地的亮度上限设为10
                result = Math.min(6 + blockLight / 2, 10);
            } else {
                result = Math.max(skyLight, blockLight);
            }
        }
        // 其他维度或者无法判断的情况，返回环境和方块光的综合值
        return result;
    }

    /**
     * 根据月相、天气等条件获取夜间月光亮度
     *
     * @param moonPhase    月相（0到7，0为满月，4为新月）
     * @param isThundering 是否雷暴
     * @param isRaining    是否下雨
     * @return 夜间月光亮度
     */
    private static int getMoonBrightness(int moonPhase, boolean isThundering, boolean isRaining) {
        if (moonPhase == 0) {
            // 满月
            return isThundering ? 3 : isRaining ? 5 : 9;
        } else if (moonPhase == 4) {
            // 新月（最暗）
            return isThundering ? 1 : 2;
        } else {
            // 其他月相，亮度随月相变化逐渐减小
            int moonLight = 9 - moonPhase;
            return isThundering ? Math.max(moonLight - 3, 1) : isRaining ? Math.max(moonLight - 2, 1) : moonLight;
        }
    }
}
