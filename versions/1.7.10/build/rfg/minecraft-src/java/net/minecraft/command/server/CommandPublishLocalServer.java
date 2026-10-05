package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;

public class CommandPublishLocalServer extends CommandBase
{
    private static final String __OBFID = "CL_00000799";

    public String getCommandName()
    {
        return "publish";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.publish.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        String s = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);

        if (s != null)
        {
            func_152373_a(sender, this, "commands.publish.started", new Object[] {s});
        }
        else
        {
            func_152373_a(sender, this, "commands.publish.failed", new Object[0]);
        }
    }
}