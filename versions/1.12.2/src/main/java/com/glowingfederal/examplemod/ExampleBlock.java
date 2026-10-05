package com.glowingfederal.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

final class ExampleBlock extends Block {
    ExampleBlock() {
        super(Material.ROCK);
        setTranslationKey("example_block");
        setHardness(1.5F);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        
    }
}
