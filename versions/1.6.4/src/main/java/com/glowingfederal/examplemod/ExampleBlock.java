package com.glowingfederal.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

final class ExampleBlock extends Block {
    ExampleBlock(int id) {
        super(id, Material.rock);
        setUnlocalizedName("example_block");
        setHardness(1.5F);
        setCreativeTab(CreativeTabs.tabBlock);
        setTextureName("examplemod:example_block");
    }
}
