ServerEvents.recipes(

    event => {
        const { avaritia } = event.recipes;
        // 无尽工作台
        avaritia.shaped_table(
            // 无序配方是 avaritia.shapeless_table
            4,
            "avaritia:infinity_sword",
            [
                "       I ",
                "      III",
                "     III ",
                "    III  ",
                " C III   ",
                "  CII    ",
                "  NC     ",
                " N  C    ",
                "X        ",
            ],
            {
                C: "avaritia:crystal_matrix_ingot",
                I: "avaritia:infinity_ingot",
                N: "avaritia:neutron_ingot",
                X: "avaritia:infinity_catalyst",
            }
        );

        // 中子态素压缩机
        avaritia.compressor("#forge:ingots/copper", Item.of("avaritia:singularity", '{Id:"avaritia:copper"}'))
            .inputCount(2000)
            .timeCost(240)
        ;

        // 更改无尽催化剂的配方
        // 由于自定义奇点的存在，无尽催化剂的配方是根据加载的奇点动态变化的，你可以自定义添加除奇点以外的物品，且此配方类型只能产出无尽催化剂，更改 result 无法更改產出物。
        avaritia.infinity_catalyst(
            [
                "minecraft:emerald_block",
                "avaritia:crystal_matrix_ingot",
                "avaritia:neutron_ingot",
                "avaritia:cosmic_meatballs",
                "avaritia:ultimate_stew",
                "avaritia:endest_pearl",
                "avaritia:record_fragment",
            ]
        );

        //custom版本
        event.custom({
            type: 'avaritia:shaped_table',//shapeless is avaritia:shapeless_table
            tier: 4,
            pattern: [
                "       IA",
                "      III",
                "     III ",
                "    III  ",
                " C III   ",
                "  CII    ",
                "  NC     ",
                " N  C    ",
                "X        "
            ],
            key: {
                A: [
                    Item.of('minecraft:enchanted_book').enchant('minecraft:silk_touch', 1).strongNBT()
                ],
                C: [
                    {item: 'avaritia:crystal_matrix_ingot'}
                ],
                I: [
                    {item: 'avaritia:infinity_ingot'}
                ],
                N: [
                    {item: 'avaritia:neutron_ingot'}
                ],
                X: [
                    {item: 'avaritia:infinity_catalyst'}
                ]
            },
            result: {item: 'avaritia:infinity_sword'}
        })
        event.custom({
            type: 'avaritia:compressor',
            ingredient: { tag: 'forge:ingots/copper' },
            result: { item: 'avaritia:singularity', count: 2 , nbt: {Id: 'avaritia:copper'}},
            inputCount: 2000,
            timeCost: 300
        })
        event.custom({
            type: 'avaritia:infinity_catalyst',
            ingredients: [
                "minecraft:emerald_block",
                "avaritia:crystal_matrix_ingot",
                "avaritia:neutron_ingot",
                "avaritia:cosmic_meatballs",
                "avaritia:ultimate_stew",
                "avaritia:endest_pearl",
                "avaritia:record_fragment",
            ]
        })
        console.log('Hello! The avaritia recipe event has fired!')
    }
)