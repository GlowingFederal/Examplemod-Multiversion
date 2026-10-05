package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class C00PacketLoginStart extends Packet
{
    private GameProfile field_149305_a;
    private static final String __OBFID = "CL_00001379";

    public C00PacketLoginStart() {}

    public C00PacketLoginStart(GameProfile profileIn)
    {
        this.field_149305_a = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149305_a = new GameProfile((UUID)null, data.readStringFromBuffer(16));
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149305_a.getName());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginServer handler)
    {
        handler.processLoginStart(this);
    }

    public GameProfile func_149304_c()
    {
        return this.field_149305_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginServer)handler);
    }
}