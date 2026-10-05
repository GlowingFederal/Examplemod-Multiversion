package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandOp extends CommandBase
{
    private static final String __OBFID = "CL_00000694";

    public String getCommandName()
    {
        return "op";
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
        return "commands.op.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 1 && args[0].length() > 0)
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(args[0]);

            if (gameprofile == null)
            {
                throw new CommandException("commands.op.failed", new Object[] {args[0]});
            }
            else
            {
                minecraftserver.getConfigurationManager().func_152605_a(gameprofile);
                func_152373_a(sender, this, "commands.op.success", new Object[] {args[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            String s = args[args.length - 1];
            ArrayList arraylist = new ArrayList();
            GameProfile[] agameprofile = MinecraftServer.getServer().func_152357_F();
            int i = agameprofile.length;

            for (int j = 0; j < i; ++j)
            {
                GameProfile gameprofile = agameprofile[j];

                if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(gameprofile) && doesStringStartWith(s, gameprofile.getName()))
                {
                    arraylist.add(gameprofile.getName());
                }
            }

            return arraylist;
        }
        else
        {
            return null;
        }
    }
}