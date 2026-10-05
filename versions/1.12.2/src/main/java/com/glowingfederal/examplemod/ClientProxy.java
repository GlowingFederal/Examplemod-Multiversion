package com.glowingfederal.examplemod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ClientProxy extends CommonProxy {
    @Override public void registerModels() {
        register(ExampleMod.exampleItem, "example_item");
        register(Item.getItemFromBlock(ExampleMod.exampleBlock), "example_block");
    }
    private void register(Item item, String name) {
        ModelResourceLocation model = new ModelResourceLocation("examplemod:" + name, "inventory");
        net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0, model);
    }
}
