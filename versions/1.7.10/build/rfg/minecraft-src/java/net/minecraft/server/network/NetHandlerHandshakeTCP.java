package net.minecraft.server.network;

import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer
{
    private final MinecraftServer field_147387_a;
    private final NetworkManager field_147386_b;
    private static final String __OBFID = "CL_00001456";

    public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager)
    {
        this.field_147387_a = serverIn;
        this.field_147386_b = netManager;
    }

    /**
     * There are two recognized intentions for initiating a handshake: logging in and acquiring server status. The
     * NetworkManager's protocol will be reconfigured according to the specified intention, although a login-intention
     * must pass a versioncheck or receive a disconnect otherwise
     */
    public void processHandshake(C00Handshake packetIn)
    {
        if (!FMLCommonHandler.instance().shouldAllowPlayerLogins())
        {
            ChatComponentText chatcomponenttext = new ChatComponentText("Server is still starting! Please wait before reconnecting.");
            this.field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext), new GenericFutureListener[0]);
            this.field_147386_b.closeChannel(chatcomponenttext);
            return;
        }

        switch (NetHandlerHandshakeTCP.SwitchEnumConnectionState.field_151291_a[packetIn.func_149594_c().ordinal()])
        {
            case 1:
                this.field_147386_b.setConnectionState(EnumConnectionState.LOGIN);
                ChatComponentText chatcomponenttext;

                if (packetIn.func_149595_d() > 5)
                {
                    chatcomponenttext = new ChatComponentText("Outdated server! I\'m still on 1.7.10");
                    this.field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext), new GenericFutureListener[0]);
                    this.field_147386_b.closeChannel(chatcomponenttext);
                }
                else if (packetIn.func_149595_d() < 5)
                {
                    chatcomponenttext = new ChatComponentText("Outdated client! Please use 1.7.10");
                    this.field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext), new GenericFutureListener[0]);
                    this.field_147386_b.closeChannel(chatcomponenttext);
                }
                else
                {
                    this.field_147386_b.setNetHandler(new NetHandlerLoginServer(this.field_147387_a, this.field_147386_b));
                }

                break;
            case 2:
                this.field_147386_b.setConnectionState(EnumConnectionState.STATUS);
                this.field_147386_b.setNetHandler(new NetHandlerStatusServer(this.field_147387_a, this.field_147386_b));
                break;
            default:
                throw new UnsupportedOperationException("Invalid intention " + packetIn.func_149594_c());
        }
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(IChatComponent reason) {}

    /**
     * Allows validation of the connection state transition. Parameters: from, to (connection state). Typically throws
     * IllegalStateException or UnsupportedOperationException if validation fails
     */
    public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
    {
        if (newState != EnumConnectionState.LOGIN && newState != EnumConnectionState.STATUS)
        {
            throw new UnsupportedOperationException("Invalid state " + newState);
        }
    }

    /**
     * For scheduled network tasks. Used in NetHandlerPlayServer to send keep-alive packets and in NetHandlerLoginServer
     * for a login-timeout
     */
    public void onNetworkTick() {}

    static final class SwitchEnumConnectionState
        {
            static final int[] field_151291_a = new int[EnumConnectionState.values().length];
            private static final String __OBFID = "CL_00001457";

            static
            {
                try
                {
                    field_151291_a[EnumConnectionState.LOGIN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_151291_a[EnumConnectionState.STATUS.ordinal()] = 2;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}