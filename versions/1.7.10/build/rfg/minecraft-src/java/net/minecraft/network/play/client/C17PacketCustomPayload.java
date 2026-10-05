package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C17PacketCustomPayload extends Packet
{
    private String field_149562_a;
    private int field_149560_b;
    private byte[] field_149561_c;
    private static final String __OBFID = "CL_00001356";

    public C17PacketCustomPayload() {}

    @SideOnly(Side.CLIENT)
    public C17PacketCustomPayload(String p_i45248_1_, ByteBuf p_i45248_2_)
    {
        this(p_i45248_1_, p_i45248_2_.array());
    }

    @SideOnly(Side.CLIENT)
    public C17PacketCustomPayload(String p_i45249_1_, byte[] p_i45249_2_)
    {
        this.field_149562_a = p_i45249_1_;
        this.field_149561_c = p_i45249_2_;

        if (p_i45249_2_ != null)
        {
            this.field_149560_b = p_i45249_2_.length;

            if (this.field_149560_b >= 32767)
            {
                throw new IllegalArgumentException("Payload may not be larger than 32k");
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149562_a = data.readStringFromBuffer(20);
        this.field_149560_b = data.readShort();

        if (this.field_149560_b > 0 && this.field_149560_b < 32767)
        {
            this.field_149561_c = new byte[this.field_149560_b];
            data.readBytes(this.field_149561_c);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149562_a);
        data.writeShort((short)this.field_149560_b);

        if (this.field_149561_c != null)
        {
            data.writeBytes(this.field_149561_c);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processVanilla250Packet(this);
    }

    public String func_149559_c()
    {
        return this.field_149562_a;
    }

    public byte[] func_149558_e()
    {
        return this.field_149561_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}