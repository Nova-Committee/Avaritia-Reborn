mods.avaritia.CraftingTable.addShaped("diamond_block", 4, <item:minecraft:diamond_block>,
[
    [],
    [],
    [],
    [<item:minecraft:coal_block>, <item:minecraft:coal_block>, <item:minecraft:coal_block>],
    [<item:minecraft:coal_block>, <item:minecraft:diamond>, <item:minecraft:coal_block>],
    [<item:minecraft:coal_block>, <item:minecraft:coal_block>, <item:minecraft:coal_block>],
    [],
    [],
    [],
]

);
mods.avaritia.CraftingTable.addShapeless("diamond_blocks", <item:minecraft:diamond_block>,
[
    <item:minecraft:coal_block>, <item:minecraft:coal_block>, <item:minecraft:coal_block>,
    <item:minecraft:coal_block>, <item:minecraft:diamond>, <item:minecraft:coal_block>,
    <item:minecraft:coal_block>, <item:minecraft:coal_block>, <item:minecraft:coal_block>
]
);

mods.avaritia.Compressor.addRecipe("diamond_blocks2", <item:minecraft:coal_block>, <item:minecraft:diamond>, 2000, 240);