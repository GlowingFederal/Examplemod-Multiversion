package com.glowingfederal.examplemod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;


@Mod(modid = ExampleMod.MODID, name = "ExampleMod", version = BuildVersion.VERSION)
public final class ExampleMod {
    public static final String MODID = "examplemod";
    public static Block exampleBlock;
    public static Item exampleItem;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        int blockId = config.getBlock("example_block", 3000).getInt();
        int itemId = config.getItem("example_item", 12000).getInt();
        config.save();
        exampleBlock = new ExampleBlock(blockId);
        exampleItem = new Item(itemId).setUnlocalizedName("example_item")
                .setTextureName("examplemod:example_item").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerBlock(exampleBlock, "example_block");
        GameRegistry.registerItem(exampleItem, "example_item");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ExampleCommand());
    }
}
