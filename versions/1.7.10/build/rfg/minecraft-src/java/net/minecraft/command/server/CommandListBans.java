package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandListBans extends CommandBase
{
    private static final String __OBFID = "CL_00000596";

    public String getCommandName()
    {
        return "banlist";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152689_b() || MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152689_b()) && super.canCommandSenderUseCommand(sender);
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.banlist.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1 && args[0].equalsIgnoreCase("ips"))
        {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.ips", new Object[] {Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152685_a().length)}));
            sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152685_a())));
        }
        else
        {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.players", new Object[] {Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152685_a().length)}));
            sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152685_a())));
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"players", "ips"}): null;
    }
}