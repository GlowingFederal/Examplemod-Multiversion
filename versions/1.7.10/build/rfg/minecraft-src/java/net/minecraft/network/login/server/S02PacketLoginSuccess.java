package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess extends Packet
{
    private GameProfile field_149602_a;
    private static final String __OBFID = "CL_00001375";

    public S02PacketLoginSuccess() {}

    public S02PacketLoginSuccess(GameProfile profileIn)
    {
        this.field_149602_a = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        String s = data.readStringFromBuffer(36);
        String s1 = data.readStringFromBuffer(16);
        UUID uuid = UUID.fromString(s);
        this.field_149602_a = new GameProfile(uuid, s1);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        UUID uuid = this.field_149602_a.getId();
        data.writeStringToBuffer(uuid == null ? "" : uuid.toString());
        data.writeStringToBuffer(this.field_149602_a.getName());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleLoginSuccess(this);
    }

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return true;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginClient)handler);
    }
}