package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public class CommandPlaySound extends CommandBase
{
    private static final String __OBFID = "CL_00000774";

    public String getCommandName()
    {
        return "playsound";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.playsound.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
        else
        {
            byte b0 = 0;
            int i = b0 + 1;
            String s = args[b0];
            EntityPlayerMP entityplayermp = getPlayer(sender, args[i++]);
            double d0 = (double)entityplayermp.getPlayerCoordinates().posX;
            double d1 = (double)entityplayermp.getPlayerCoordinates().posY;
            double d2 = (double)entityplayermp.getPlayerCoordinates().posZ;
            double d3 = 1.0D;
            double d4 = 1.0D;
            double d5 = 0.0D;

            if (args.length > i)
            {
                d0 = func_110666_a(sender, d0, args[i++]);
            }

            if (args.length > i)
            {
                d1 = func_110665_a(sender, d1, args[i++], 0, 0);
            }

            if (args.length > i)
            {
                d2 = func_110666_a(sender, d2, args[i++]);
            }

            if (args.length > i)
            {
                d3 = parseDoubleBounded(sender, args[i++], 0.0D, 3.4028234663852886E38D);
            }

            if (args.length > i)
            {
                d4 = parseDoubleBounded(sender, args[i++], 0.0D, 2.0D);
            }

            if (args.length > i)
            {
                d5 = parseDoubleBounded(sender, args[i++], 0.0D, 1.0D);
            }

            double d6 = d3 > 1.0D ? d3 * 16.0D : 16.0D;
            double d7 = entityplayermp.getDistance(d0, d1, d2);

            if (d7 > d6)
            {
                if (d5 <= 0.0D)
                {
                    throw new CommandException("commands.playsound.playerTooFar", new Object[] {entityplayermp.getCommandSenderName()});
                }

                double d8 = d0 - entityplayermp.posX;
                double d9 = d1 - entityplayermp.posY;
                double d10 = d2 - entityplayermp.posZ;
                double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
                double d12 = entityplayermp.posX;
                double d13 = entityplayermp.posY;
                double d14 = entityplayermp.posZ;

                if (d11 > 0.0D)
                {
                    d12 += d8 / d11 * 2.0D;
                    d13 += d9 / d11 * 2.0D;
                    d14 += d10 / d11 * 2.0D;
                }

                entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(s, d12, d13, d14, (float)d5, (float)d4));
            }
            else
            {
                entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(s, d0, d1, d2, (float)d3, (float)d4));
            }

            func_152373_a(sender, this, "commands.playsound.success", new Object[] {s, entityplayermp.getCommandSenderName()});
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}