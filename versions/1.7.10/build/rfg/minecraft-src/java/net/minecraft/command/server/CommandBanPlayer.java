package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

public class CommandBanPlayer extends CommandBase
{
    private static final String __OBFID = "CL_00000165";

    public String getCommandName()
    {
        return "ban";
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
        return "commands.ban.usage";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152689_b() && super.canCommandSenderUseCommand(sender);
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1 && args[0].length() > 0)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(args[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.ban.failed", new Object[] {args[0]});
            }
            else
            {
                String s = null;

                if (args.length >= 2)
                {
                    s = func_147178_a(sender, args, 1).getUnformattedText();
                }

                UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, sender.getCommandSenderName(), (Date)null, s);
                minecraftserver.getConfigurationManager().func_152608_h().func_152687_a(userlistbansentry);
                EntityPlayerMP entityplayermp = minecraftserver.getConfigurationManager().func_152612_a(args[0]);

                if (entityplayermp != null)
                {
                    entityplayermp.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
                }

                func_152373_a(sender, this, "commands.ban.success", new Object[] {args[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}