package committee.nova.mods.avaritia.init.registry;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/12/22 01:54
 * @Description:
 */
public enum ModResourceBlocks {

    BLAZE(50, 1000),
    CRYSTAL(100, 2000),
    NEUTRON(8888, 8888),
    INFINITY(9999, 9999);

    public final float resistance, hardness;

    ModResourceBlocks(float resistance, float hardness){
        this.resistance = resistance;
        this.hardness = hardness;
    }
}
