package com.glowingfederal.examplemod;

import com.glowingfederal.examplemod.domain.ExampleGreeting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

final class ExampleCommand extends CommandBase {
    @Override public String getCommandName() { return "examplemod"; }
    @Override public String getCommandUsage(ICommandSender sender) { return "/examplemod"; }
    @Override public int getRequiredPermissionLevel() { return 0; }
    @Override public void processCommand(ICommandSender sender, String[] args) {
        String text = ExampleGreeting.create(sender.getCommandSenderName());
        sender.addChatMessage(new net.minecraft.util.ChatComponentText(text));
    }
}
