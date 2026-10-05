package com.glowingfederal.examplemod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ClientProxy extends CommonProxy {
    @Override public void registerModels() {
        register(ExampleMod.exampleItem, "example_item");
        register(Item.getItemFromBlock(ExampleMod.exampleBlock), "example_block");
    }
    private void register(Item item, String name) {
        ModelResourceLocation model = new ModelResourceLocation("examplemod:" + name, "inventory");
        net.minecraft.client.Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, model);
    }
}
