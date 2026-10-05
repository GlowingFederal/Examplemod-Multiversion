package com.glowingfederal.examplemod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;



@Mod(modid = ExampleMod.MODID, name = "ExampleMod", version = BuildVersion.VERSION)
public final class ExampleMod {
    public static final String MODID = "examplemod";
    public static Block exampleBlock;
    public static Item exampleItem;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        exampleBlock = new ExampleBlock();
        exampleItem = new Item().setUnlocalizedName("example_item")
                .setTextureName("examplemod:example_item").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(exampleBlock, "example_block");
        GameRegistry.registerItem(exampleItem, "example_item");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ExampleCommand());
    }
}
