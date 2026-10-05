package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandWhitelist extends CommandBase
{
    private static final String __OBFID = "CL_00001186";

    public String getCommandName()
    {
        return "whitelist";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.whitelist.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();

            if (args[0].equals("on"))
            {
                minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
                func_152373_a(sender, this, "commands.whitelist.enabled", new Object[0]);
                return;
            }

            if (args[0].equals("off"))
            {
                minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
                func_152373_a(sender, this, "commands.whitelist.disabled", new Object[0]);
                return;
            }

            if (args[0].equals("list"))
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", new Object[] {Integer.valueOf(minecraftserver.getConfigurationManager().func_152598_l().length), Integer.valueOf(minecraftserver.getConfigurationManager().getAvailablePlayerDat().length)}));
                String[] astring1 = minecraftserver.getConfigurationManager().func_152598_l();
                sender.addChatMessage(new ChatComponentText(joinNiceString(astring1)));
                return;
            }

            GameProfile gameprofile;

            if (args[0].equals("add"))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
                }

                gameprofile = minecraftserver.func_152358_ax().func_152655_a(args[1]);

                if (gameprofile == null)
                {
                    throw new CommandException("commands.whitelist.add.failed", new Object[] {args[1]});
                }

                minecraftserver.getConfigurationManager().func_152601_d(gameprofile);
                func_152373_a(sender, this, "commands.whitelist.add.success", new Object[] {args[1]});
                return;
            }

            if (args[0].equals("remove"))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
                }

                gameprofile = minecraftserver.getConfigurationManager().func_152599_k().func_152706_a(args[1]);

                if (gameprofile == null)
                {
                    throw new CommandException("commands.whitelist.remove.failed", new Object[] {args[1]});
                }

                minecraftserver.getConfigurationManager().func_152597_c(gameprofile);
                func_152373_a(sender, this, "commands.whitelist.remove.success", new Object[] {args[1]});
                return;
            }

            if (args[0].equals("reload"))
            {
                minecraftserver.getConfigurationManager().loadWhiteList();
                func_152373_a(sender, this, "commands.whitelist.reloaded", new Object[0]);
                return;
            }
        }

        throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"on", "off", "list", "add", "remove", "reload"});
        }
        else
        {
            if (args.length == 2)
            {
                if (args[0].equals("remove"))
                {
                    return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().func_152598_l());
                }

                if (args[0].equals("add"))
                {
                    return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().func_152358_ax().func_152654_a());
                }
            }

            return null;
        }
    }
}