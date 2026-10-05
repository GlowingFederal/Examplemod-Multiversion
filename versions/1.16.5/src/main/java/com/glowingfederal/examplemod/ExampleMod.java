package com.glowingfederal.examplemod;

import com.glowingfederal.examplemod.domain.ExampleGreeting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MODID)
public final class ExampleMod {
    public static final String MODID = "examplemod";
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(AbstractBlock.Properties.of(net.minecraft.block.material.Material.STONE).strength(1.5F)));
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().tab(net.minecraft.item.ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(net.minecraft.item.ItemGroup.TAB_MISC)));

    public ExampleMod() {
        net.minecraftforge.eventbus.api.IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(net.minecraft.command.Commands.literal("examplemod").executes(context -> {
            String text = ExampleGreeting.create(context.getSource().getTextName());
            context.getSource().sendSuccess(new net.minecraft.util.text.StringTextComponent(text), false);
            return 1;
        }));
    }

}
