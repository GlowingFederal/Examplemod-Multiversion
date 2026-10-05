package com.glowingfederal.examplemod;

import com.glowingfederal.examplemod.domain.ExampleGreeting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

final class ExampleCommand extends CommandBase {
    @Override public String getName() { return "examplemod"; }
    @Override public String getUsage(ICommandSender sender) { return "/examplemod"; }
    @Override public int getRequiredPermissionLevel() { return 0; }
    @Override public void execute(net.minecraft.server.MinecraftServer server, ICommandSender sender, String[] args) {
        String text = ExampleGreeting.create(sender.getName());
        sender.sendMessage(new net.minecraft.util.text.TextComponentString(text));
    }
}
