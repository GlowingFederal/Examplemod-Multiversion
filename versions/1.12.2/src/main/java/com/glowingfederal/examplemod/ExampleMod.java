package com.glowingfederal.examplemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ExampleMod.MODID, name = "ExampleMod", version = BuildVersion.VERSION)
public final class ExampleMod {
    public static final String MODID = "examplemod";
    public static Block exampleBlock;
    public static Item exampleItem;
    @SidedProxy(clientSide = "com.glowingfederal.examplemod.ClientProxy", serverSide = "com.glowingfederal.examplemod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        exampleBlock = new ExampleBlock();
        exampleItem = new Item().setUnlocalizedName("example_item").setCreativeTab(CreativeTabs.tabMisc);
        exampleBlock.setRegistryName(MODID, "example_block");
        exampleItem.setRegistryName(MODID, "example_item");
        GameRegistry.findRegistry(Block.class).register(exampleBlock);
        GameRegistry.findRegistry(Item.class).register(exampleItem);
        GameRegistry.findRegistry(Item.class).register(new net.minecraft.item.ItemBlock(exampleBlock)
                .setRegistryName(exampleBlock.getRegistryName()));
    }
    @Mod.EventHandler public void init(FMLInitializationEvent event) { proxy.registerModels(); }
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ExampleCommand());
    }
}
